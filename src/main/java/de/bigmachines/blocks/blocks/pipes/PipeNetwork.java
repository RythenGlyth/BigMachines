package de.bigmachines.blocks.blocks.pipes;

import de.bigmachines.utils.BlockHelper;
import de.bigmachines.utils.DebugHelper;
import de.bigmachines.utils.NBTHelper;
import de.bigmachines.utils.classes.Pair;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class PipeNetwork {
	private final TileEntityPipeBase root;
	private final Set<Connection<TileEntityPipeBase>> connections = new HashSet<>();
	private final Set<Connection<TileEntity>> fModules = new HashSet<>(); // foreign modules = sources and sinks
	private final Capability<?> c;
	
	//private Map<TileEntityPipeBase, Map<List<TileEntityPipeBase>, FluidStack>> currentContents = new HashMap<>();
	public NetworkContents currentContents = new NetworkContents();
	
	protected PipeNetwork(final Capability<?> capability, final TileEntityPipeBase root) {
		c = capability;
		this.root = root;
	}
	
	protected TileEntityPipeBase getRoot() {
		return root;
	}
	
	public void mergeInto(final TileEntityPipeBase merger1, final TileEntityPipeBase merger2, final PipeNetwork other) {
		other.connections.addAll(connections);
		other.connections.add(new Connection<TileEntityPipeBase>(merger1, merger2));
		for (final Connection<TileEntityPipeBase> pipe : connections) {
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
			connections.add(new Connection<>(inserter, pipe));
		else
			System.out.println("attempting to insert null pipe: " + inserter + ", " + pipe);
	}
	
	/**
	 * Adds a module *that is not a pipe* to this network.
	 *
	 * @param fModule the TileEntityPipeBase of the pipe that was destroyed
	 */
	public void addModule(@Nullable final TileEntityPipeBase inserter, @Nullable final TileEntity fModule) {
		if (inserter == null || fModule == null) return;
		if (BlockHelper.getConnectingFace(inserter.getPos(), fModule.getPos()) == null) return;
		else if (fModule.hasCapability(c, BlockHelper.getConnectingFace(inserter.getPos(), fModule.getPos())))
			fModules.add(new Connection<>(inserter, fModule));
	}
	
	/**
	 * To call when a pipe of this network is destroyed.
	 *
	 * @param pipe the TileEntityPipeBase of the pipe that was destroyed
	 */
	public void remove(final TileEntityPipeBase pipe) {
		if (pipe == null) return;
		
		fModules.removeIf(connection -> connection.a.equals(pipe) || connection.b.equals(pipe));
		connections.removeIf(connection -> connection.a.equals(pipe) || connection.b.equals(pipe));
		currentContents.remove(pipe);
		// TODO what if the path contains the pipe but the fluid isn't there yet
		
		//if (pipe.equals(root)) { // if the pipe to remove is the root, we have to do some fiddling:
		// new roots -> their children
		final HashMap<TileEntityPipeBase, Set<TileEntityPipeBase>> roots = divideIntoGroups(pipe);
		for (final Map.Entry<TileEntityPipeBase, Set<TileEntityPipeBase>> subtree : roots.entrySet()) {
			// create a new network for this root (this root is subtree.getKey()):
			final PipeNetwork subnetwork = new PipeNetwork(c, subtree.getKey());
			subtree.getKey().setNetwork(subnetwork);
			
			for (final Connection<TileEntityPipeBase> conn : connections) {
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
	
	/**
	 * Called whenever the root node receives a tick. Updates the *entire* network, moves on all fluids
	 * and inserts new ones / fill adjacent tanks
	 */
	public void update() {
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("beginning update with:");
		
		System.out.println(currentContents);
		System.out.println("moving fluids once:");
		moveFluidsOneTick();
		System.out.println("after moving: ");
		System.out.println(currentContents);
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		System.out.println("inserting new fluids:");
		final Map<Pair<TileEntity, TileEntityPipeBase>, FluidStack> inserters = inserters();
		System.out.println("found " + inserters.size() + " inserters");
		for (Map.Entry<Pair<TileEntity, TileEntityPipeBase>, FluidStack> inserter : inserters.entrySet()) {
			final TileEntityPipeBase inserterPipe = inserter.getKey().y; // the pipe that is adjacent to the source
			final FluidStack fluidDrained = insertVia(inserterPipe, inserter.getKey().x);
			List<Pair<FluidStack, NetworkContents.Path>> drained = distributeFluidIntoSinks(inserterPipe, fluidDrained);
			System.out.println("inserter: " + inserterPipe + " drains " + fluidDrained);
			System.out.println("found " + drained.size() + " sinks");
			
			for (Pair<FluidStack, NetworkContents.Path> drainedFluidWithPath : drained) {
				int drainedAmount = currentContents.add(inserterPipe, drainedFluidWithPath.y, drainedFluidWithPath.x);
				System.out.println("drained " + drainedAmount);
				drainSource(inserter.getKey().x, drainedAmount, inserterPipe);
				// TODO what if the first one drains fully + actually drain the source
			}
		}
		
		System.out.println("after inserting: ");
		System.out.println(currentContents);
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	}
	
	private void updated(Set<TileEntityPipeBase> differingPipes) {
		// TODO call this method
		for (TileEntityPipeBase pipe : differingPipes)
			pipe.updated();
	}
	
	public void drainSource(final TileEntity source, final int amount, final TileEntityPipeBase targetPipe) {
		final IFluidHandler handler = (IFluidHandler) source.getCapability(c,
				  BlockHelper.getConnectingFace(source.getPos(), targetPipe.getPos()));
		handler.drain(amount, true);
	}
	
	private void moveFluidsOneTick() {
		// TODO remove all paths with fluids that have 0 amount
		
		final NetworkContents nextContents = new NetworkContents();
		
		for (final Map.Entry<TileEntityPipeBase, Map<NetworkContents.Path, FluidStack>> currentContent : currentContents.entrySet()) {
			// for every pipe that currently contains something
			for (final Map.Entry<NetworkContents.Path, FluidStack> fluidInPipe : currentContent.getValue().entrySet()) {
				// for every fluid that is in this pipe currently
				if (fluidInPipe.getKey().isEmpty()) { // fill the fluid into target
					FluidStack fluid = fluidInPipe.getValue();
					TileEntity target = fluidInPipe.getKey().getTarget();
					IFluidHandler sink = target.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
							  BlockHelper.getConnectingFace(currentContent.getKey().getPos(), target.getPos()));
					int filled = sink.fill(fluid, true);
					if (filled < fluid.amount) { // if the pipe couldn't be drained entirely
						fluid.amount -= filled;
						// add the rest back to the same pipe
						nextContents.add(currentContent.getKey(), fluidInPipe.getKey(), fluid);
					}
				} else {
					NetworkContents.Path currentPath = new NetworkContents.Path(fluidInPipe.getKey());
					final TileEntityPipeBase nextTile = currentPath.remove(0); // from here on it's nextPath not currentPath
					nextContents.add(nextTile, currentPath, fluidInPipe.getValue());
				}
			}
		}
		
		currentContents = nextContents;
	}
	
	/*
	 * This table could either be generated via a shortest-first, random or round-robin algorithm.
	 * We have to check for:
	 *  - sink capacity, whether the sinks accepts this type of fluid,
	 * 		maybe we even need a 3d array for every fluid present in the pipe system
	 *  - pipe throughput
	 *  - wether all connections are valid (FIXME blacklisted fluids??)
	 *
	 * TODO: test for chunkloading issues
	 */
	
	/**
	 * *After* the removal of the root pipe (or multiple pipes?) from the network, this checks which pipes are still connected, and which new
	 * networks we may need. For this I do a depth-search starting at different nodes until all nodes are found, and return a map with each
	 * new root mapped to its children.
	 *
	 * Note that the key of each entry is always contained in the entry as well, this means that the children of each subnetwork contain
	 * their root as well.
	 *
	 * There is another special case: if removedPipe is null, this method will simply return a map with the root of this network mapped to
	 * all children.
	 *
	 * @param removedPipe which pipe was removed, or null if the network didn't change
	 */
	@Nonnull
	private HashMap<TileEntityPipeBase, Set<TileEntityPipeBase>> divideIntoGroups(@Nullable TileEntityPipeBase removedPipe) {
		final Set<TileEntityPipeBase> unknown = new HashSet<>();
		for (final Connection<TileEntityPipeBase> connection : connections) {
			if (!connection.a.equals(removedPipe)) unknown.add(connection.a);
			if (!connection.b.equals(removedPipe)) unknown.add(connection.b);
		}
		
		final HashMap<TileEntityPipeBase, Set<TileEntityPipeBase>> groups = new HashMap<>();
		
		if (removedPipe == null) {
			groups.put(getRoot(), unknown);
			return groups;
		}
		
		BFSearcher ds;
		while (!unknown.isEmpty()) {
			try {
				final TileEntityPipeBase next = unknown.iterator().next();
				ds = new BFSearcher(next);
				// validator: if path is empty: this is the very first node, insert it
				// else check if the node is connected (or blacklisted / ...)
				ds.setValidator((path, to) -> {
					if (to.equals(removedPipe)) return false;
					else return path.size() == 0 || to.isConnectedTo(path.get(path.size() - 1));
				});
				ds.discover();
				groups.put(next, ds.foundConnections.keySet());
				unknown.removeAll(ds.foundConnections.keySet());
			} catch (final NoSuchElementException ignored) {
			} // there are no more elements
		}
		
		return groups;
	}
	
	// TODO don't extract and directly insert into myself if I can extract & insert
	// TODO maybe change all path specifications from List to Set because the shortest
	//	will never use the same pipe twice
	// TODO maybe this is the wrong way: I shouldn't check for the shortest but all ways
	//  and add up the fluids I can transport over each which may be more than I can
	//  over the shortest only
	// TODO Source - - - - Sink
	//				   ^ if there is still something in this pipe we can begin inserting now
	
	/**
	 * This distributes the specified fluid inserted via the source pipe to all available sinks It does check 1. whether all pipes along the
	 * path are connected 2. how much those pipes can transport of this fluid 3. whether the sink can take the fluid
	 * <p>
	 * Every valid sink is automatically added to the fluid network's contents (but not drained from the source) as well as to a map that is
	 * returned. Using this array every calculated fluid movement can actually be executed.
	 *
	 * @param source where we want to insert the fluid
	 * @param fluid  which fluid we want to insert
	 * @return a
	 */
	@NotNull
	private List<Pair<FluidStack, NetworkContents.Path>> distributeFluidIntoSinks(final TileEntityPipeBase source, final FluidStack fluid) {
		System.out.println("distributing start");
		final BFSearcher sinkSearcher = new BFSearcher(source);
		
		// which new targets were found & added to the network contents during search
		final List<Pair<FluidStack, NetworkContents.Path>> targets = new ArrayList<>();
		
		sinkSearcher.setValidator(new PathValidator(fluid));
		
		sinkSearcher.discover();
		System.out.println(sinkSearcher.foundConnections.size() + " connections found:");
		DebugHelper.printMap(sinkSearcher.foundConnections);
		
		for (Connection<TileEntity> moduleConnection : fModules) {
			final Pair<TileEntityPipeBase, TileEntity> sorted = sortConnection(moduleConnection);
			TileEntityPipeBase sinkInserter = sorted.x;
			TileEntity sink = sorted.y;
			
			if (sinkInserter.getAttachment(BlockHelper.getConnectingFace(sinkInserter.getPos(), sink.getPos())).canExtract()) {
				System.out.println("inserter: " + sinkInserter + ", sink:" + sink);
				
				// FIXME equal out if there are multiple sources set to IN & OUT
				
				final IFluidHandler handler = (IFluidHandler) sink.getCapability(c,
						  BlockHelper.getConnectingFace(sinkInserter.getPos(), sink.getPos()));
				final int maxSink = handler.fill(fluid, false);
				
				// the sink searcher is for one specific source block (see constructor)
				// foundConnections maps sink -> path to sink
				if (sinkSearcher.foundConnections.containsKey(sinkInserter)) {
					System.out.println("found sink " + sinkInserter.getPos());
					// FIXME this finds connections that are 0 long
					// the path to the inserting pipe:
					NetworkContents.Path connection = sinkSearcher.foundConnections.get(sinkInserter);
					connection.add(sinkInserter); // TODO does this fit here?
					System.out.println(connection);
					if (connection.size() > 0) {
						System.out.println("conection > 0");
						FluidStack transported = fluid.copy();
						transported.amount = Math.min(canTransport(connection, transported), maxSink);
						connection.remove(0); // remove the first tile because this is the one we're currently in
						
						// the newly found
						connection.setTarget(sink);
						targets.add(new Pair<FluidStack, NetworkContents.Path>(transported, connection));
						//currentContents.add(source, connection, transported);
					} else System.out.println("connection = 0");
				}
				System.out.println("searched for sink");
			}
		}
		System.out.println("distribitung end: " + targets.size());
		return targets;
	}
	
	@Nullable
	public FluidStack getContents(@Nullable final TileEntityPipeBase pipe) {
		return currentContents.getContents(pipe);
	}
	
	public int fill(final TileEntityPipeBase pipe, final FluidStack resource, final boolean doFill) {
		distributeFluidIntoSinks(pipe, resource);
		// TODO route inserted fluids
		// maybe I will simply search for fluids without a target every tick and route them then
		// because this way they will get split up if its too much fluid in the pipe for one run
		return 0;
	}
	
	private class PathValidator implements ConnectionValidator {
		
		private final FluidStack fluidStack;
		
		private PathValidator(final FluidStack fluidStack) {
			this.fluidStack = fluidStack;
		}
		
		@Override
		public boolean isValid(@Nonnull final NetworkContents.Path path, @Nonnull final TileEntityPipeBase to) {
			if (canTransport(to, path.size(), fluidStack) == 0) return false;
			if (path.size() == 0) return true;
			return to.isConnectedTo(path.get(-1)) && to.canInsertIn(path.get(-1));
		}
	}
	
	/**
	 * Iterates all available inserters and creates a map that checks which fluids
	 * these inserters can insert.
	 *
	 * @return a map that maps (tank, inserter) -> fluid that can be inserted
	 */
	Map<Pair<TileEntity, TileEntityPipeBase>, FluidStack> inserters() {
		Map<Pair<TileEntity, TileEntityPipeBase>, FluidStack> inserters = new HashMap<>();
		
		for (Connection<TileEntity> module : fModules) {
			final Pair<TileEntityPipeBase, TileEntity> sorted = sortConnection(module);
			final FluidStack inserted = insertVia(sorted.x, sorted.y);
			if (inserted == null || inserted.amount == 0) continue;
			inserters.put(sorted.swap(), inserted);
			// TODO distribute their contents, do this every tick,
			// 	advance the fluid contents every tick
			// 	make the pipes aware of their contents and save the contents to nbt
		}
		
		return inserters;
	}
	
	/**
	 * This tests what fluid stack and how much of it the specified source *could* insert via the specified inserter. It is tested:
	 * 1. whether they are connected (if not, null is returned).
	 * 2. whether the pipe has the correct attachment set to extract (if not, null is returned).
	 * 3. how much the source can drain
	 * 4. _if the pipe can transport the drained fluid, and how much of it_
	 *
	 * @param inserter the pipe that is connected to something that is not a pipe
	 * @param source   something that is not a pipe
	 * @return what fluid is imported and how much of it, null if it can't insert anything
	 */
	@Nullable
	public FluidStack insertVia(@Nullable final TileEntityPipeBase inserter, @Nullable final TileEntity source) {
		if (inserter == null || source == null) return null;
		final EnumFacing connectingFace = BlockHelper.getConnectingFace(inserter.getPos(), source.getPos());
		if (connectingFace == null) return null;
		if (inserter.hasAttachment(connectingFace)) {
			if (!inserter.getAttachment(connectingFace).canInsert()) return null;
			else {
				final IFluidHandler handler = (IFluidHandler) source.getCapability(c, connectingFace.getOpposite());
				FluidStack inserted = handler.drain(inserter.maxContents(), false);
				if (inserted == null) return null;
				inserted.amount = canTransport(inserter, 0, inserted);
				return inserted;
			}
		} else return null;
	}
	
	@Nonnull
	public static Pair<TileEntityPipeBase, TileEntity> sortConnection(@Nullable Connection<TileEntity> conn) throws InvalidPairException {
		if (conn == null) return new Pair<>(null, null);
		TileEntityPipeBase pipe;
		TileEntity module;
		if (conn.a instanceof TileEntityPipeBase && !(conn.b instanceof TileEntityPipeBase)) {
			pipe = (TileEntityPipeBase) conn.a;
			module = conn.b;
		} else if (conn.b instanceof TileEntityPipeBase && !(conn.a instanceof TileEntityPipeBase)) {
			pipe = (TileEntityPipeBase) conn.b;
			module = conn.a;
		} else throw new InvalidPairException();
		
		return new Pair<TileEntityPipeBase, TileEntity>(pipe, module);
	}
	
	/**
	 * How much of the fluid stack can be transported along the path
	 *
	 * @param path       which path the fluid should take
	 * @param fluidStack which fluid should be moved
	 */
	int canTransport(final NetworkContents.Path path, final FluidStack fluidStack) {
		int amount = fluidStack.amount;
		for (int i = 0; i < path.size(); i++) {
			// for every tile in the path, check if it limits the throughput
			amount = Math.min(amount, canTransport(path.get(i), i, fluidStack));
			if (amount <= 0) return 0;
		}
		return amount;
	}
	
	/**
	 * how much of "added" FluidStack we can add to a pipe if it contains contents.
	 * <p>
	 * The specified pipe's contents is never read, we only need it to check it's maximum capacity
	 *
	 * @param pipe     which pipe (type) we are talking about
	 * @param contents what the pipe already contains
	 * @param added    what we want to add
	 * @return how much space in the pipe is occupied
	 */
	int occupiedSpaceInPipe(final TileEntityPipeBase pipe, final FluidStack contents, final FluidStack added) {
		if (contents == null) return 0; // no fluid in the pipe
		if (contents.isFluidEqual(added)) return contents.amount; // the fluids can merge
		else return pipe.maxContents(); // the fluids can't merge
	}
	
	int canTransport(final TileEntityPipeBase pipe, final int ticksAhead, final FluidStack fluidStack) {
		if (ticksAhead == 0) { // the current tick
			if (currentContents.containsKey(pipe)) {
				// how much we can transport at most
				int transported = Math.min(fluidStack.amount, pipe.maxContents());
				for (final FluidStack currentContent : currentContents.get(pipe).values()) {
					// for each fluid with path that is currently in the pipe, subtract it's limit from the
					// maximum amount we can transport
					// at this point we shouldn't have to worry about any fluids that can't merge
					// I SAID SHOULDN'T
					transported -= occupiedSpaceInPipe(pipe, currentContent, fluidStack);
				}
				//if (transported < 0) throw new RuntimeException("pipe overflow @ " + pipe.getPos());
				return Math.max(0, transported);
				//return canTransport(pipe, currentContents.get(pipe).x, fluidStack);
			} else return Math.min(fluidStack.amount, pipe.maxContents());
		} else {
			// declare a new variable that stores how much of the fluidStack we can transport at most
			int transported = Math.min(fluidStack.amount, pipe.maxContents());
			// for everything that is currently in the network:
			for (Map<NetworkContents.Path, FluidStack> fluidsInPipe : currentContents.values()) {
				// for every fluid in this pipe:
				for (final Map.Entry<NetworkContents.Path, FluidStack> fluidInPipe : fluidsInPipe.entrySet()) {
					// check where this fluid is going, it may be splitting up, that's why we have a list<> here.
					final NetworkContents.Path path = fluidInPipe.getKey();
					// each individual path
					System.out.println("ticks ahead: " + ticksAhead);
					System.out.println("path size: " + path.size());
					if (ticksAhead > path.size()) continue; // this fluid / path pair is gone before the specified tick
					else if (ticksAhead == path.size()) {
						if (path.get(path.size() - 1).equals(pipe)) {
							// the fluid reaches the very last pipe (the inserter in the specified tick)
							transported -= occupiedSpaceInPipe(pipe, fluidInPipe.getValue(), fluidStack);
						} else continue;
					} else { // the fluid is still in the system
						if (path.contains(pipe) && path.get(ticksAhead).equals(pipe))
							// the fluid is in the pipe we're checking for at the tick we want
							transported -= occupiedSpaceInPipe(pipe, fluidInPipe.getValue(), fluidStack);
						// return how much can fit
					}
				}
				//if (transported < 0) throw new RuntimeException("pipe overflow @ " + pipe.getPos());
				fluidStack.amount = Math.max(0, transported);
			}
			return fluidStack.amount;
		}
	}
	
	void debugInfo(final TileEntityPipeBase home) {
		System.out.println("===============================================");
		System.out.println("network with capability " + c);
		if (root == null) System.out.println("root is null");
		else System.out.println("root " + root.hashCode() + " @ " + root);
		System.out.println("connections:");
		for (final Connection<TileEntityPipeBase> pipe : connections)
			System.out.println(" x " + pipe);
		System.out.println("fModules:");
		for (final Connection<TileEntity> module : fModules)
			System.out.println(" x " + module);
		System.out.println("===============================================");
		
		final BFSearcher ds = new BFSearcher(home);
		System.out.println("starting discovery");
		ds.discover();
		System.out.println("finished discovery");
		
		DebugHelper.printMapSortedByValueProperty(ds.foundConnections, TileEntityPipeBase::toString, NetworkContents.Path::size);
		
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
		for (final Connection<TileEntityPipeBase> pipe : connections) {
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
		data.setTag("contentList", currentContents.contentCompound());
		
		System.out.println(data);
		// TODO test, read contentList when recreating the network from nbt
		
		return data;
	}
	
	/**
	 * This is a class that does a Breadth-First-Search starting at the specified node.
	 * Imagine the pipe network like a tree:
	 * *search* root           <- this does not have to be the network root
	 * /  \
	 * A   C
	 * /\  /\	Q <- this is some spare, unconnected node
	 * D E F G
	 * |
	 * H
	 *
	 * 1. the search root is scanned for any children nodes (A and C). If they are valid (the validator returns true), they are added to both
	 * the foundConnections and unknown map (see discovered()). both those maps hold each discovered node and the path that leads to it, with
	 * the difference being that foundConnections are all connections that were found and unknown holds all nodes that still need to be
	 * scanned / indexed for their children.
	 * 2. the next node in the unknown map is scanned for children with each being added again to both
	 * maps if it is valid. The path to these children nodes consists of the path to their parent node + the parent node itself.
	 * 3. this is
	 * repeated until there are now more unknown nodes. At this point every node of the network with the shortest path to it should be in
	 * foundConnections.
	 *
	 * In the end with the above tree we would have something like this:
	 * foundConnections = {
	 * search root -> [],
	 * A -> [search root],
	 * B -> [search root],
	 * D -> [search root, A],
	 * E -> [search root, A],
	 * F -> [search root, C],
	 * G -> [search root, C],
	 * H -> [search root, F]
	 * }
	 * unknown = {}
	 */
	protected final class BFSearcher {
		// tepb block -> path
		protected final Map<TileEntityPipeBase, NetworkContents.Path> foundConnections = new HashMap<>();
		// a new node to search from and the path to it
		private final Map<TileEntityPipeBase, NetworkContents.Path> unknown = new HashMap<>();
		
		private ConnectionValidator validator;
		
		private static final long MAX_RUNS = 1000;
		
		protected BFSearcher(final TileEntityPipeBase searchRoot) {
			foundConnections.put(searchRoot, new NetworkContents.Path(searchRoot));
			unknown.put(searchRoot, new NetworkContents.Path(searchRoot));
			validator = (path, to) -> true;
		}
		
		protected void setValidator(@Nonnull final ConnectionValidator validator) {
			this.validator = validator;
		}
		
		private void discover() {
			long run = 0;
			// FIXME hardcoded max runs shouldn't be in production
			while (!unknown.isEmpty() && run < MAX_RUNS) {
				final Map.Entry<TileEntityPipeBase, NetworkContents.Path> search = unknown.entrySet().iterator().next();
				unknown.remove(search.getKey());
				for (final Connection<TileEntityPipeBase> connection : PipeNetwork.this.connections) {
					final NetworkContents.Path path = new NetworkContents.Path(search.getValue());
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
			if (run == MAX_RUNS) System.out.println("max runs reached");
		}
		
		private void discovered(final NetworkContents.Path path, final TileEntityPipeBase node) {
			final int distance = path.size();
			if (validator.isValid(path, node)) {
				boolean isShorter = true;
				if (foundConnections.containsKey(node)) {
					NetworkContents.Path lastPath = foundConnections.get(node);
					// FIXME there should never be an entry with null path?
					isShorter = lastPath != null && distance < lastPath.size();
				}
				if (isShorter) {
					foundConnections.put(node, path);
					unknown.put(node, path);
				}
			}
		}
		
	}
	
	@FunctionalInterface
	private interface ConnectionValidator {
		boolean isValid(final NetworkContents.Path path, final TileEntityPipeBase to);
	}
	
	private static class Connection<T extends TileEntity> {
		private final T a;
		private final T b;
		
		Connection(final T a, final T b) {
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
	
	public static class InvalidPairException extends RuntimeException {
	
	}
	
}