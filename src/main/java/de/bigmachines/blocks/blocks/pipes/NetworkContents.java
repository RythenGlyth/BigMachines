package de.bigmachines.blocks.blocks.pipes;

import de.bigmachines.blocks.blocks.pipes.fluidpipe.TileEntityFluidPipe;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.*;

public class NetworkContents implements Cloneable {
	
	// what is currently in the network:
	// which pipe -> contains which fluid and where this fluid should go
	// (it is possible for a fluid to later split up, that's why it's stored
	// in a map mapping the path -> the fluidstack along this path)
	// the paths listed here are the paths that are left, not the entire path
	// and should be truncated every time the fluid moves on
	// the paths start with the next tile in the path, not the pipe the fluid
	// is currently in
	private final Map<TileEntityPipeBase, Map<Path, FluidStack>> contents = new HashMap<>();
	
	public void remove(final TileEntityPipeBase pipe) {
		contents.remove(pipe);
	}
	
	public boolean containsKey(final TileEntityPipeBase inserterPipe) {
		return contents.containsKey(inserterPipe);
	}
	
	public Map<Path, FluidStack> get(final TileEntityPipeBase pipe) {
		return contents.get(pipe);
	}
	
	/**
	 * Adds the specified fluid to the provided pipe:
	 * 1. If the pipe does not contain anything, simply add the fluid
	 * 2. If the pipe does contain something, check if it can be merged and
	 * a) if the already contained fluid and the newly added fluid share the
	 * same path, merge them
	 * b) if the already contained fluid has a different path, store them
	 * individually to save their individual path
	 *
	 * The path always begins with the next tile (where the fluid should go next).
	 * If this is not the case, the method throws a RuntimeException.
	 *
	 * If any of the arguments is null, nothing is added and 0 is returned.
	 *
	 * @param pipe       which pipe to insert in
	 * @param path       which path the fluid should be routed on
	 * @param fluidStack which fluid should be inserted, will never be changed
	 * @return how much of the fluid was added, 0 if none was added.
	 * @throws RuntimeException if the specified path contains the pipe itself.
	 */
	public int add(@Nullable final TileEntityPipeBase pipe, @Nullable final Path path,
	               @Nullable final FluidStack fluidStack) throws RuntimeException {
		if (pipe == null || path == null || fluidStack == null) return 0;
		if (path.contains(pipe)) throw new RuntimeException("path contains the pipe itself");
		if (contents.containsKey(pipe)) {
			Map<Path, FluidStack> fluidsWithPaths = contents.get(pipe); // every fluid that is in this pipe
			int freeSpaceInPipe = pipe.maxContents();
			for (FluidStack fluidWithPath : fluidsWithPaths.values()) {
				freeSpaceInPipe -= fluidWithPath.amount;
				if (freeSpaceInPipe < 0) return 0;
			}
			
			FluidStack inserted = fluidStack.copy();
			inserted.amount = Math.min(fluidStack.amount, freeSpaceInPipe);
			if (fluidsWithPaths.containsKey(path)) {
				fluidsWithPaths.get(path).amount += inserted.amount;
			} else {
				fluidsWithPaths.put(path, inserted);
			}
			return inserted.amount;
		} else {
			Map<Path, FluidStack> newContent = new HashMap<>();
			FluidStack inserted = fluidStack.copy();
			inserted.amount = Math.min(fluidStack.amount, pipe.maxContents());
			newContent.put(path, inserted);
			contents.put(pipe, newContent);
			return inserted.amount;
		}
	}
	
	@Override
	public String toString() {
		if (contents.size() == 0) return "empty contents";
		final StringBuilder toString = new StringBuilder();
		final String lineSep = System.getProperty("line.separator");
		for (Map.Entry<TileEntityPipeBase, Map<Path, FluidStack>> content : contents.entrySet()) {
			toString.append(" - pipe @ " + content.getKey() + " contains:");
			toString.append(lineSep);
			for (Map.Entry<Path, FluidStack> fluidWithPath : content.getValue().entrySet()) {
				toString.append("   -> " + fluidWithPath.getKey() + " and fluid " + fluidWithPath.getValue());
				toString.append(lineSep);
			}
		}
		return toString.substring(0, toString.length() - lineSep.length());
	}
	
	@Override
	protected Object clone() {
		NetworkContents cloned = new NetworkContents();
		cloned.contents.putAll(this.contents);
		return cloned;
	}
	
	public Collection<Map<Path, FluidStack>> values() {
		return contents.values();
	}
	
	protected Set<TileEntityPipeBase> diff(final NetworkContents other) {
		Set<TileEntityPipeBase> diff = new HashSet<>();
		
		// TODO
		
		return diff;
	}
	
	@Nullable
	public FluidStack drain(final TileEntityFluidPipe pipe, final int maxDrain, final boolean doDrain) {
		if (pipe == null) return null;
		if (get(pipe) == null) return null;
		FluidStack drained = null;
		// iterate over each fluid with path
		for (FluidStack fluid : get(pipe).values()) {
			if (drained == null) {
				// in the first step drain the first fluid with path
				drained = fluid.copy();
				drained.amount = Math.min(fluid.amount, maxDrain); // but only as much as fits in maxDrain
				if (doDrain) fluid.amount -= drained.amount; // actually remove it from the pipe
			} else {
				int drainedAmount = Math.min(maxDrain - drained.amount, fluid.amount); // how much we still can fit
				drained.amount += drainedAmount;
				if (doDrain) fluid.amount -= drainedAmount; // actually remove it from the pipe
			}
		}
		return drained;
	}
	
	protected FluidStack getContents(final TileEntityPipeBase pipe) {
		if (pipe == null) return null;
		if (get(pipe) == null) return null;
		FluidStack contents = null;
		for (FluidStack fluid : get(pipe).values()) {
			if (contents == null) contents = fluid.copy();
			else contents.amount += fluid.amount;
		}
		return contents;
	}
	
	public static class Path {
		private final List<TileEntityPipeBase> path = new ArrayList<>();
		
		public Path() {
		
		}
		
		public Path(final List<TileEntityPipeBase> path) {
			if (path != null)
				this.path.addAll(path);
		}
		
		public Path(final Path path) {
			if (path != null)
				this.path.addAll(path.path); // lol
		}
		
		/**
		 * Wraps the List.get() method with a little extra:
		 * If the index is negative, this number is returned starting at the end,
		 * e. g. get(-1) returns the last element.
		 *
		 * @param index which index to return (positives start at the start, negatives at the end)
		 * @return the element at the specified index
		 */
		public TileEntityPipeBase get(final int index) {
			if (index >= 0) return path.get(index);
			else return path.get(size() + index);
		}
		
		public boolean contains(final TileEntityPipeBase pipe) {
			return path.contains(pipe);
		}
		
		public boolean equals(Object other) {
			if (other == null) return false;
			if (this == other) return true;
			if (other instanceof Path) {
				return ((Path) other).path.equals(path);
			}
			return false;
		}
		
		public int hashCode() {
			return path.hashCode();
		}
		
		public TileEntityPipeBase remove(final int i) {
			return path.remove(i);
		}
		
		@Override
		public String toString() {
			if (path.size() == 0) return "empty path";
			String toString = "";
			for (TileEntityPipeBase pipe : path)
				toString += " -> " + pipe.getPos();
			return toString.substring(4);
		}
		
		public int size() {
			return path.size();
		}
		
		public void add(final TileEntityPipeBase a) {
			// TODO throw exception if pipe specified twice?
			path.add(a);
		}
	}
}
