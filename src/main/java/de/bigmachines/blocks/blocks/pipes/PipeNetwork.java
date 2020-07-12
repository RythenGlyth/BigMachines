package de.bigmachines.blocks.blocks.pipes;

import de.bigmachines.utils.BlockHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import java.util.*;

public class PipeNetwork {
	private TileEntityPipeBase root;
	private final Set<Connection<TileEntityPipeBase>> pipes = new HashSet<>();
	private final Set<Connection<TileEntity>> fModules = new HashSet<>(); // foreign modules = sources and sinks
	private final Capability c;

	protected PipeNetwork(final Capability c, TileEntityPipeBase root) {
		this.c = c;
		this.root = root;
	}

	/**
	 * Merges this pipe network into another,
	 * @param other
	 */
	public void mergeInto(TileEntityPipeBase merger1, TileEntityPipeBase merger2, PipeNetwork other) {
		other.pipes.addAll(pipes);
		other.pipes.add(new Connection<TileEntityPipeBase>(merger1, merger2));
		for (final Connection<TileEntityPipeBase> pipe : pipes) {
			((TileEntityPipeBase) pipe.a).setNetwork(other);
			((TileEntityPipeBase) pipe.b).setNetwork(other);
		}
	}

	/**
	 * Adds a pipe pair to this network, as in makes it recognizable for the system.
	 *
	 * @param pipe has to be directly adjacent as well as connected to the system
	 */
	public void insert(TileEntityPipeBase inserter, TileEntityPipeBase pipe) {
		pipes.add(new Connection<TileEntityPipeBase>(inserter, pipe));
	}

	/**
	 * Adds a module *that is not a pipe* to this network.
	 * @param fModule
	 */
	public void addModule(TileEntityPipeBase inserter, TileEntity fModule) {
		fModules.add(new Connection<TileEntity>(inserter, fModule));
	}

	/**
	 * To call when a pipe of this network is destroyed.
	 * @param pipe
	 */
	public void remove(TileEntityPipeBase pipe) {

		Set<TileEntityPipeBase> pipes = null;
		if (pipe.equals(root)) {
			pipes = new HashSet<>();
			for (Connection<TileEntityPipeBase> c : this.pipes) {
				pipes.add(c.a);
				pipes.add(c.b);
			}
			pipes.remove(pipe);
		}

		fModules.removeIf(connection -> {
			return connection.a.equals(pipe) || connection.b.equals(pipe);
		});
		this.pipes.removeIf (connection -> {
			return connection.a.equals(pipe) || connection.b.equals(pipe);
		});

		if (pipe.equals(root)) {
			//root = getNewRoot();
			HashMap<TileEntityPipeBase, Set<Connection<TileEntityPipeBase>>> roots = divideIntoGroups(pipes);
			for (Map.Entry<TileEntityPipeBase, Set<Connection<TileEntityPipeBase>>> subtree : roots.entrySet()) {
				System.out.println("Creating a new network @ " + subtree.getKey().getPos());
				PipeNetwork subnetwork = new PipeNetwork(c, subtree.getKey());
				subtree.getKey().setNetwork(subnetwork);
				for (Connection<TileEntityPipeBase> child : subtree.getValue()) {
					subnetwork.insert(child.a, child.b);
					if (!child.a.getNetwork().equals(subnetwork)) child.a.setNetwork(subnetwork);
					System.out.println(" - " + child.a.getPos());
					if (!child.b.getNetwork().equals(subnetwork)) child.b.setNetwork(subnetwork);
					System.out.println(" - " + child.b.getPos());
				}
			}
		}
		// TODO update root on nbt on all pipes

	}

	private HashMap<TileEntityPipeBase, Set<Connection<TileEntityPipeBase>>> divideIntoGroups(Set<TileEntityPipeBase> knownPipes) {
		Set<TileEntityPipeBase> unknown = new HashSet<>(knownPipes);
		for (Connection<TileEntityPipeBase> c : pipes) {
			unknown.add(c.a);
			unknown.add(c.b);
		}
		HashMap<TileEntityPipeBase, Set<Connection<TileEntityPipeBase>>> groups = new HashMap<>();

		DepthSearch ds;
		while (!unknown.isEmpty()) {
			try {
				TileEntityPipeBase next = unknown.iterator().next();
				ds = new DepthSearch(next);
				ds.discover();
				groups.put(next, ds.connections);
				unknown.removeAll(ds.lengths.keySet());
			} catch (NoSuchElementException ex) {} // there are no more elements
		}

		return groups;
	}

	public void clearAdjacentModules(TileEntityPipeBase pipe) {
		clearAdjacentModules(pipe.getWorld(), pipe.getPos());
	}

	/**
	 * Clears all *modules* adjacent to the pipe at the provided position
	 * @param pos
	 */
	public void clearAdjacentModules(World world, BlockPos pos) {
		for (EnumFacing side : EnumFacing.values()) {
			BlockHelper.getAdjacentTileEntity(world, pos, side);
		}
	}

	void debugInfo(TileEntityPipeBase home) {
		System.out.println("===============================================");
		System.out.println("network with capability " + c);
		System.out.println("root " + root.hashCode() + " @ " + root.getPos());
		System.out.println("pipes:");
		for (Connection<TileEntityPipeBase> pipe : pipes)
			System.out.println(" x " + pipe.a.getPos() + " <-> " + pipe.b.getPos());
		System.out.println("fModules:");
		for (Connection<TileEntity> module : fModules)
			System.out.println(" x " + module.a.getPos() + " <-> " + module.b.getPos());
		System.out.println("===============================================");

		DepthSearch ds = new DepthSearch(home);
		ds.discover();

		List<Map.Entry<TileEntityPipeBase, Integer>> entries = new LinkedList(ds.lengths.entrySet());
		Collections.sort(entries, new Comparator() {
		    public int compare(Object o1, Object o2) {
				return ((Map.Entry<TileEntityPipeBase, Integer>) o1).getValue()
					- ((Map.Entry<TileEntityPipeBase, Integer>) o2).getValue();
			}
		});
		HashMap<TileEntityPipeBase, Integer> sortedLengths = new LinkedHashMap();
		for (Map.Entry<TileEntityPipeBase, Integer> entry : entries)
			System.out.println(entry.getKey().getPos() + ", " + entry.getValue() + " away");
		System.out.println("===============================================");
	}

	private class DepthSearch {
		private final TileEntityPipeBase searchRoot;
		private final Map<TileEntityPipeBase, Integer> lengths = new HashMap<>();
		private final Set<Connection<TileEntityPipeBase>> connections = new HashSet<>();

		// a new node to search from and the distance to it
		private final Map<TileEntityPipeBase, Integer> unknown = new HashMap<>();

		private static final long MAX_RUNS = 1000000;

		private DepthSearch(TileEntityPipeBase searchRoot) {
			this.searchRoot = searchRoot;
			lengths.put(searchRoot, 0);
			unknown.put(searchRoot, 0);
		}

		private void discover() {
			long run = 0;
			while (!unknown.isEmpty() && run < MAX_RUNS) {
				Map.Entry<TileEntityPipeBase, Integer> search = unknown.entrySet().iterator().next();
				unknown.remove(search.getKey());
				for (Connection<TileEntityPipeBase> pipe : pipes)
						 if (pipe.a.equals(search.getKey())) discovered(pipe.a, pipe.b, search.getValue() + 1);
					else if (pipe.b.equals(search.getKey())) discovered(pipe.b, pipe.a, search.getValue() + 1);
				run++;
			}
		}

		private void discovered(TileEntityPipeBase inserter, TileEntityPipeBase node, int distance) {
			if (!lengths.containsKey(node) || lengths.get(node) > distance) {
				lengths.put(node, distance);
				unknown.put(node, distance);
				connections.add(new Connection(inserter, node));
			}
		}
	}

	private class Connection<T extends TileEntity> {
		private final T a;
		private final T b;

		private Connection(T a, T b) {
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

		public boolean equals(Object other) {
			if (this == other) return true;
			if (other == null) return false;
			if (other instanceof Connection) {
				Connection c = (Connection) other;
				return a.equals(c.a) && b.equals(c.b);
			}
			return false;
		}
	}

}
