package de.bigmachines.blocks.blocks.pipes;

import net.minecraftforge.fluids.FluidStack;

import java.util.*;

public class NetworkContents {
	
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
	 * @param pipe       which pipe to insert in
	 * @param path       which path the fluid should be routed on
	 * @param fluidStack which fluid should be inserted
	 * @return how much of the fluid was added, 0 if none was added.
	 */
	public int add(final TileEntityPipeBase pipe, final Path path, final FluidStack fluidStack) {
		if (contents.containsKey(pipe)) {
			if (contents.get(pipe).containsKey(path)) {
				// TODO merge
			} else {
				// TODO merge
				contents.get(pipe).put(path, fluidStack);
			}
		} else {
			Map<Path, FluidStack> newContent = new HashMap<>();
			newContent.put(path, fluidStack);
			contents.put(pipe, newContent);
		}
	}
	
	public Collection<Map<Path, FluidStack>> values() {
		return contents.values();
	}
	
	public static class Path {
		private final List<TileEntityPipeBase> path = new ArrayList<>();
		
		public Path() {
		
		}
		
		public Path(List<TileEntityPipeBase> path) {
			path.addAll(path);
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
	}
}
