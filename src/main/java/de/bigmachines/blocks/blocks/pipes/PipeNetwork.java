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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
	 * FIXME doesn't discover the outer one on this setup
	 *     |                    |
	 *     |                    |
	 * - - o - -      ->    - - + - -
	 *     |                    |
	 *     |                    |
	 * also place step 1, then save and quit, relog and place step 2, root is null.
	 *
	 * also:
	 * + - +             + - +
	 * |   |             |   |
	 * + - o - +      -> + - + - +
	 *     |   |             |   |
	 *     + - +             + - +
	 *
	 *
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
	public void insert(@Nullable final TileEntityPipeBase inserter, @Nullable final TileEntityPipeBase pipe) {
		// FIXME why did it import a null pipe on world reload?
		if (inserter != null && pipe != null)
			pipes.add(new Connection<>(inserter, pipe));
		else
			System.out.println("attempting to insert null pipe: " + inserter + ", " + pipe);
	}

	/**
	 * Adds a module *that is not a pipe* to this network.
	 *
	 * @param fModule the TileEntityPipeBase of the pipe that was destroyed
	 */
	public void addModule(final TileEntityPipeBase inserter, final TileEntity fModule) {
		fModules.add(new Connection<>(inserter, fModule));
	}

	/**
	 * To call when a pipe of this network is destroyed.
	 *
	 * @param pipe the TileEntityPipeBase of the pipe that was destroyed
	 */
	public void remove(final TileEntityPipeBase pipe) {

/*j
		Set<TileEntityPipeBase> remainingPipes = null;
		if (pipe.equals(root)) {
			remainingPipes = new HashSet<>();
			for (final Connection<TileEntityPipeBase> conn : this.pipes) {
				remainingPipes.add(conn.a);
				remainingPipes.add(conn.b);
			}
			remainingPipes.remove(pipe);
		}
*/

		fModules.removeIf(connection -> connection.a.equals(pipe) || connection.b.equals(pipe));
		this.pipes.removeIf(connection -> connection.a.equals(pipe) || connection.b.equals(pipe));

		if (pipe.equals(root)) { // if the pipe to remove is the root, we have to do some fiddling:
			System.out.println("splitting up the network:");
			// new roots -> their children
			final HashMap<TileEntityPipeBase, Set<TileEntityPipeBase>> roots = divideIntoGroups(pipe);
			System.out.println(roots.size() + " subs:");
			for (final Map.Entry<TileEntityPipeBase, Set<TileEntityPipeBase>> subtree : roots.entrySet()) {
				System.out.println(" - " + subtree.getKey() + " with " + subtree.getValue());
				// create a new network for this root (this root is subtree.getKey()):
				final PipeNetwork subnetwork = new PipeNetwork(c, subtree.getKey());
				subtree.getKey().setNetwork(subnetwork);

				/*
				for (final TileEntityPipeBase child : subtree.getValue()) {
					subnetwork.insert(child.a, child.b);
					if (!child.a.getNetwork().equals(subnetwork)) child.a.setNetwork(subnetwork);
					if (!child.b.getNetwork().equals(subnetwork)) child.b.setNetwork(subnetwork);
				}
				*/
				for (final Connection<TileEntityPipeBase> conn : pipes) {
					if (subtree.getValue().contains(conn.a)) {
						subnetwork.insert(conn.a, conn.b);
					} else if (subtree.getValue().contains(conn.b)) {
						subnetwork.insert(conn.a, conn.b);
					}
				}
				
				for (final TileEntityPipeBase children : subtree.getValue())
					children.setNetwork(subnetwork);
			}
		}

	}

	/*
	 * TODO I can't run the entire transport in one update() tick / multiple updates accumulate over time
	 *  possible solution: store for each pipe the fluid it currently holds + where this fluid has to go
	 *  when creating the destination table first calculate, how much fluid is still in the system, and then try,
	 *  how much the sinks can take from here on
	 *
	 * concept:
	 * on every update() on the root node, I scan the entire network for in- and outputs and create
	 * a double[amountOfSources][amountOfSinks] like this:
	 *      \ to  Module D	Module E Module F
	 * insert
	 * from
	 * Module A		.8			.2		0 // insert .8 into D then it's full, put the rest into E
	 * Module B		0			1		0 // B's shortest connection is to E
	 * Module C		0			.6		.4 // next one is E, it's already filled by partly by B, put the rest into F
	 *
	 * Every column should add up to 0 (no more sinks available) ... 1 (source fully exhausted)
	 *
	 * And then, in one run
	 *
	 * This table could either be generated via a shortest-first, random or round-robin algorithm.
	 * We have to check for:
	 *  - sink capacity, whether the sinks accepts this type of fluid,
	 * 		maybe we even need a 3d array for every fluid present in the pipe system
	 *  - pipe throughput
	 *  - wether all connections are valid (FIXME blacklisted fluids??)
	 *
	 * TODO: test for chunkloading issues
	 *
	 * To do this I may have to change the internal data structure to not save connections but only which pipes are in the network.
	 */
	
	/**
	 * *After* the removal of the root pipe (or multiple pipes?) from the network, this checks which pipes are still connected,
	 * and which new networks we may need.
	 * For this I do a depth-search starting at different nodes until all nodes are found,
	 * and return a map with each new root mapped to its children.
	 *
	 * Note that the key of each entry is always contained in the entry as well,
	 * this means that the children of each subnetwork contain their root as well.
	 *
	 * There is another special case: if removedPipe is null, this method will simply return a map
	 * with the root of this network mapped to all children.
	 *
	 * @param removedPipe which pipe was removed, or null if the network didn't change
	 */
	@Nonnull
	private HashMap<TileEntityPipeBase, Set<TileEntityPipeBase>> divideIntoGroups(@Nullable TileEntityPipeBase removedPipe) {
		final Set<TileEntityPipeBase> unknown = new HashSet<>();
		for (final Connection<TileEntityPipeBase> connection : pipes) {
			if (!connection.a.equals(removedPipe)) unknown.add(connection.a);
			if (!connection.b.equals(removedPipe)) unknown.add(connection.b);
		}
		
		final HashMap<TileEntityPipeBase, Set<TileEntityPipeBase>> groups = new HashMap<>();
		
		if (removedPipe == null) {
			groups.put(getRoot(), unknown);
			return groups;
		}

		DepthSearch ds;
		while (!unknown.isEmpty()) {
			try {
				final TileEntityPipeBase next = unknown.iterator().next();
				ds = new DepthSearch(next);
				// validator: if path is empty: this is the very first node, insert it
				// else check if the node is connected (or blacklisted / ...)
				ds.setValidator((path, to) -> {
					if (to.equals(removedPipe)) return false;
					else
						return path.size() == 0 || to.isConnectedTo(path.get(path.size() - 1));
				});
				ds.discover();
				groups.put(next, ds.connections.keySet());
				unknown.removeAll(ds.connections.keySet());
			} catch (final NoSuchElementException ignored) {
			} // there are no more elements
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

	// TODO don't extract and directly insert into myself if I can extract & insert
	void enumerateSinks(final TileEntityPipeBase source) {

	}

	void debugInfo(final TileEntityPipeBase home) {
		System.out.println("===============================================");
		System.out.println("network with capability " + c);
		if (root == null) System.out.println("root is null");
		else System.out.println("root " + root.hashCode() + " @ " + root);
		System.out.println("connections:");
		for (final Connection<TileEntityPipeBase> pipe : pipes)
			System.out.println(" x " + pipe);
		System.out.println("fModules:");
		for (final Connection<TileEntity> module : fModules)
			System.out.println(" x " + module);
		System.out.println("===============================================");

		final DepthSearch ds = new DepthSearch(home);
		ds.discover();
		
		DebugHelper.printMapSortedByValueProperty(ds.connections, TileEntityPipeBase::toString, List::size);

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
		// tepb block -> path
		private final Map<TileEntityPipeBase, List<TileEntityPipeBase>> connections = new HashMap<>();
		// a new node to search from and the path to it
		private final Map<TileEntityPipeBase, List<TileEntityPipeBase>> unknown = new HashMap<>();

		private ConnectionValidator validator;

		private static final long MAX_RUNS = 1000000;

		private DepthSearch(final TileEntityPipeBase searchRoot) {
			connections.put(searchRoot, new ArrayList<TileEntityPipeBase>());
			unknown.put(searchRoot, new ArrayList<TileEntityPipeBase>());
			validator = (path, to) -> true;
		}

		public void setValidator(final ConnectionValidator validator) {
			this.validator = validator;
		}

		private void discover() {
			long run = 0;
			// FIXME hardcoded max runs shouldn't be in production
			while (!unknown.isEmpty() && run < MAX_RUNS) {
				final Map.Entry<TileEntityPipeBase, List<TileEntityPipeBase>> search = unknown.entrySet().iterator().next();
				unknown.remove(search.getKey());
				for (final Connection<TileEntityPipeBase> connection : pipes) {
					final List<TileEntityPipeBase> path = new ArrayList<>(search.getValue());
					// since there is no premise whether connections get swapped out
					// (see Connection#<init>)
					// we have to check which one is the actual inserter and which one isn't inserted yet
					if (connection.a.equals(search.getKey())) { // a is the inserter
						path.add(connection.a);
						discovered(path, connection.b);
					} else if (connection.b.equals(search.getKey())) { // b is the inserter
						path.add(connection.b);
						discovered(path, connection.a);
					}
				}
				run++;
			}
		}

		private void discovered(final List<TileEntityPipeBase> path, final TileEntityPipeBase node) {
			final int distance = path.size();
			if (validator.isValid(path, node) && !connections.containsKey(node) || connections.get(node).size() > distance) {
				connections.put(node, path);
				unknown.put(node, path);
			}
		}

	}

	@FunctionalInterface
	private interface ConnectionValidator {
		boolean isValid(final List<TileEntityPipeBase> path, final TileEntityPipeBase to);
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
		
		public String toString() {
			return a.getPos() + " <-> " + b.getPos();
		}
		
	}

}