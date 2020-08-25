package de.bigmachines.utils.classes;

import de.bigmachines.blocks.blocks.pipes.TileEntityPipeBase;
import de.bigmachines.utils.NBTHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Path {
	
	private final List<TileEntityPipeBase> path = new ArrayList<>();
	private TileEntity target;
	
	private Path() {
		target = null;
	}
	
	public Path(final TileEntity target) {
		this.target = target;
	}
	
	public Path(final List<TileEntityPipeBase> path, final TileEntity target) {
		if (path != null)
			this.path.addAll(path);
		this.target = target;
	}
	
	public Path(final Path path) {
		if (path != null)
			this.path.addAll(path.path); // lol
		this.target = path.target;
	}
	
	public void setTarget(@Nullable final TileEntity target) {
		this.target = target;
	}
	
	public TileEntity getTarget() {
		return target;
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
	
	/**
	 * Removes the specified pipe and all subsequent ones.
	 *
	 * @param pipe
	 */
	public void truncate(final TileEntityPipeBase pipe) {
		final int firstPipe = path.indexOf(pipe);
		if (firstPipe == -1) return;
		path.subList(firstPipe, path.size()).clear();
	}
	
	public boolean equals(Object other) {
		if (other == null) return false;
		if (this == other) return true;
		if (other instanceof Path) {
			Path otherPath = (Path) other;
			return path.equals(otherPath.path) && target.equals(otherPath.target);
		}
		return false;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setTag("target", NBTHelper.writeBlockPosToTag(target.getPos()));
		NBTTagList pathTag = new NBTTagList();
		for (TileEntityPipeBase pipe : path)
			pathTag.appendTag(NBTHelper.writeBlockPosToTag(pipe.getPos()));
		nbt.setTag("path", pathTag);
		// TODO path compression (e. g. south times 10)
		return nbt;
	}
	
	@Nonnull
	public static Path readFromNBT(@Nonnull World world, @Nonnull NBTTagCompound nbt) {
		BlockPos targetPos = NBTHelper.readTagToBlockPos(nbt.getCompoundTag("target"));
		Path path = new Path(world.getTileEntity(targetPos));
		
		NBTTagList pathTag = nbt.getTagList("path", 10);
		for (int i = 0; i < pathTag.tagCount(); i++)
			path.add((TileEntityPipeBase) world.getTileEntity(NBTHelper.readTagToBlockPos(pathTag.getCompoundTagAt(i))));
		
		return path;
	}
	
	public int hashCode() {
		return path.hashCode();
	}
	
	public TileEntityPipeBase remove(final int i) {
		return path.remove(i);
	}
	
	@Override
	public String toString() {
		if (path.size() == 0) return "empty path with target " + target.getPos();
		StringBuilder toString = new StringBuilder(64);
		for (TileEntityPipeBase pipe : path)
			toString.append(" -> ").append(pipe.getPos());
		return toString.substring(4) + " with target " + target.getPos();
	}
	
	public int size() {
		return path.size();
	}
	
	public boolean isEmpty() {
		return path.isEmpty();
	}
	
	public void add(final TileEntityPipeBase a) {
		// TODO throw exception if pipe specified twice?
		path.add(a);
	}
}
