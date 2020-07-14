package de.bigmachines.blocks.blocks.pipes;

import de.bigmachines.utils.BlockHelper;
import de.bigmachines.utils.DebugHelper;
import de.bigmachines.utils.NBTHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import java.util.*;

public class PipeNetwork {
	private final TileEntityPipeBase root;
	private final Set<Connection<TileEntityPipeBase>> pipes = new HashSet<>();
	private final Set<Connection<TileEntity>> fModules = new HashSet<>(); // foreign modules = sources and sinks
	private final Capability<?> c;

	protected PipeNetwork(final Capability<?> capability, final TileEntityPipeBase root) {
		c = capability;
		this.root = root;
	}

	protected TileEntityPipeBase getRoot() {
		return root;
	}

	/**
	 * Merges this pipe network into another,
	 */
	public void mergeInto(final TileEntityPipeBase merger1, final TileEntityPipeBase merger2, final PipeNetwork other) {
		other.pipes.addAll(pipes);
		other.pipes.add(new Connection<TileEntityPipeBase>(merger1, merger2));
		for (final Connection<TileEntityPipeBase> pipe : pipes) {
			pipe.a.setNetwork(other);
			pipe.b.setNetwork(other);
		}
	}

	/**
	 * Adds a pipe pair to this network, as in makes it recognizable for the system.
	 *
	 * @param pipe has to be directly adjacent as well as connected to the system
	 */
	public void insert(final TileEntityPipeBase inserter, final TileEntityPipeBase pipe) {
		pipes.add(new Connection<>(inserter, pipe));
	}

	/**
	 * Adds a module *that is not a pipe* to this network.
	 * @param fModule the TileEntityPipeBase of the pipe that was destroyed
	 */
	public void addModule(final TileEntityPipeBase inserter, final TileEntity fModule) {
		fModules.add(new Connection<>(inserter, fModule));
	}

	/**
	 * To call when a pipe of this network is destroyed.
	 * @param pipe the TileEntityPipeBase of the pipe that was destroyed
	 */
	public void remove(final TileEntityPipeBase pipe) {

		Set<TileEntityPipeBase> remainingPipes = null;
		if (pipe.equals(root)) {
			remainingPipes = new HashSet<>();
			for (final Connection<TileEntityPipeBase> conn : this.pipes) {
				remainingPipes.add(conn.a);
				remainingPipes.add(conn.b);
			}
			remainingPipes.remove(pipe);
		}

		fModules.removeIf(connection -> connection.a.equals(pipe) || connection.b.equals(pipe));
		this.pipes.removeIf (connection -> connection.a.equals(pipe) || connection.b.equals(pipe));

		if (pipe.equals(root)) {
			//root = getNewRoot();
			final HashMap<TileEntityPipeBase, Set<Connection<TileEntityPipeBase>>> roots = divideIntoGroups(remainingPipes);
			for (final Map.Entry<TileEntityPipeBase, Set<Connection<TileEntityPipeBase>>> subtree : roots.entrySet()) {
				final PipeNetwork subnetwork = new PipeNetwork(c, subtree.getKey());
				subtree.getKey().setNetwork(subnetwork);
				for (final Connection<TileEntityPipeBase> child : subtree.getValue()) {
					subnetwork.insert(child.a, child.b);
					if (!child.a.getNetwork().equals(subnetwork)) child.a.setNetwork(subnetwork);
					if (!child.b.getNetwork().equals(subnetwork)) child.b.setNetwork(subnetwork);
				}
			}
		}

	}

	private HashMap<TileEntityPipeBase, Set<Connection<TileEntityPipeBase>>> divideIntoGroups(final Set<TileEntityPipeBase> knownPipes) {
		final Set<TileEntityPipeBase> unknown = new HashSet<>(knownPipes);
		for (final Connection<TileEntityPipeBase> c : pipes) {
			unknown.add(c.a);
			unknown.add(c.b);
		}
		final HashMap<TileEntityPipeBase, Set<Connection<TileEntityPipeBase>>> groups = new HashMap<>();

		DepthSearch ds;
		while (!unknown.isEmpty()) {
			try {
				final TileEntityPipeBase next = unknown.iterator().next();
				ds = new DepthSearch(next);
				ds.discover();
				groups.put(next, ds.connections);
				unknown.removeAll(ds.lengths.keySet());
			} catch (final NoSuchElementException ignored) {} // there are no more elements
		}

		return groups;
	}

	public void clearAdjacentModules(final TileEntityPipeBase pipe) {
		clearAdjacentModules(pipe.getWorld(), pipe.getPos());
	}

	/**
	 * Clears all *modules* adjacent to the pipe at the provided position
	 */
	public void clearAdjacentModules(final World world, final BlockPos pos) {
		for (final EnumFacing side : EnumFacing.values()) {
			BlockHelper.getAdjacentTileEntity(world, pos, side);
		}
	}

	void debugInfo(final TileEntityPipeBase home) {
		System.out.println("===============================================");
		System.out.println("network with capability " + c);
		if (root == null) System.out.println("root is null");
		else System.out.println("root " + root.hashCode() + " @ " + root.getPos());
		System.out.println("pipes:");
		for (final Connection<TileEntityPipeBase> pipe : pipes)
			System.out.println(" x " + pipe.a.getPos() + " <-> " + pipe.b.getPos());
		System.out.println("fModules:");
		for (final Connection<TileEntity> module : fModules)
			System.out.println(" x " + module.a.getPos() + " <-> " + module.b.getPos());
		System.out.println("===============================================");

		final DepthSearch ds = new DepthSearch(home);
		ds.discover();

		DebugHelper.printMapSortedByValue(ds.lengths, entity -> entity.getPos().toString());

		System.out.println("===============================================");
	}

	/**
	 * Generates a compound that can be stored on the root element and contains all data to restore this network.
	 *
	 * @return a compound with every connection of this system.
	 */
	protected NBTTagCompound rootCompound() {
		final NBTTagCompound data = new NBTTagCompound();

		final NBTTagList pipeList = new NBTTagList();
		for (final Connection<TileEntityPipeBase> pipe : pipes) {
			final NBTTagCompound connection = new NBTTagCompound();
			connection.setTag("a", NBTHelper.writeBlockPosToTag(pipe.a.getPos()));
			connection.setTag("b", NBTHelper.writeBlockPosToTag(pipe.b.getPos()));
			pipeList.appendTag(connection);
		}

		final NBTTagList moduleList = new NBTTagList();
		for (final Connection<TileEntity> module : fModules) {
			final NBTTagCompound extension = new NBTTagCompound();
			extension.setTag("a", NBTHelper.writeBlockPosToTag(module.a.getPos()));
			extension.setTag("b", NBTHelper.writeBlockPosToTag(module.b.getPos()));
			moduleList.appendTag(extension);
		}

		data.setTag("pipeList", pipeList);
		data.setTag("moduleList", moduleList);

		return data;
	}

	private class DepthSearch {
		private final Map<TileEntityPipeBase, Integer> lengths = new HashMap<>();
		private final Set<Connection<TileEntityPipeBase>> connections = new HashSet<>();

		// a new node to search from and the distance to it
		private final Map<TileEntityPipeBase, Integer> unknown = new HashMap<>();

		private static final long MAX_RUNS = 1000000;

		private DepthSearch(final TileEntityPipeBase searchRoot) {
			lengths.put(searchRoot, 0);
			unknown.put(searchRoot, 0);
		}

		private void discover() {
			long run = 0;
			while (!unknown.isEmpty() && run < MAX_RUNS) {
				final Map.Entry<TileEntityPipeBase, Integer> search = unknown.entrySet().iterator().next();
				unknown.remove(search.getKey());
				for (final Connection<TileEntityPipeBase> pipe : pipes)
						 if (pipe.a.equals(search.getKey())) discovered(pipe.a, pipe.b, search.getValue() + 1);
					else if (pipe.b.equals(search.getKey())) discovered(pipe.b, pipe.a, search.getValue() + 1);
				run++;
			}
		}

		private void discovered(final TileEntityPipeBase inserter, final TileEntityPipeBase node, final int distance) {
			if (!lengths.containsKey(node) || lengths.get(node) > distance) {
				lengths.put(node, distance);
				unknown.put(node, distance);
				connections.add(new Connection<>(inserter, node));
			}
		}
	}

	private static class Connection<T extends TileEntity> {
		private final T a;
		private final T b;

		private Connection(final T a, final T b) {
			if (a.getPos().getX() < b.getPos().getX()) {
				this.a = a;
				this.b = b;
			} else if (a.getPos().getX() == b.getPos().getX()) {
				if (a.getPos().getY() < b.getPos().getY()) {
					this.a = a;
					this.b = b;
				} else if (a.getPos().getY() == b.getPos().getY()) {
					if (a.getPos().getZ() < b.getPos().getZ()) {
						this.a = a;
						this.b = b;
					} else if (a.getPos().getZ() == b.getPos().getZ()) {
						throw new RuntimeException("connection between identical tes");
					} else {
						this.a = b;
						this.b = a;
					}
				} else {
					this.a = b;
					this.b = a;
				}
			} else {
				this.a = b;
				this.b = a;
			}
		}

		public boolean equals(final Object other) {
			if (this == other) return true;
			if (other == null) return false;
			if (other instanceof Connection) {
				final Connection<?> c = (Connection<?>) other;
				return a.equals(c.a) && b.equals(c.b);
			}
			return false;
		}
	}

}
