package de.bigmachines.blocks.blocks.pipes;

import de.bigmachines.utils.BlockHelper;
import de.bigmachines.utils.DebugHelper;
import de.bigmachines.utils.NBTHelper;
import de.bigmachines.utils.classes.Pair;
import de.bigmachines.utils.classes.Path;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class PipeNetwork {
	
	private final TileEntityPipeBase root;
	private final Set<Connection<TileEntityPipeBase>> connections = new HashSet<>(4); // initial capacity 4
	private final Set<Connection<TileEntity>> fModules = new HashSet<>(2); // foreign modules = sources and sinks
	private final Capability<?> c;
	
	//private Map<TileEntityPipeBase, Map<List<TileEntityPipeBase>, FluidStack>> currentContents = new HashMap<>();
	public NetworkContents currentContents = new NetworkContents();
	
	protected PipeNetwork(final Capability<?> capability, @Nonnull final TileEntityPipeBase root) {
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
	public void insertPipe(@Nullable final TileEntityPipeBase inserter, @Nullable final TileEntityPipeBase pipe) {
		if (inserter != null && pipe != null)
			connections.add(new Connection<>(inserter, pipe));
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
					subnetwork.insertPipe(conn.a, conn.b);
				} else if (subtree.getValue().contains(conn.b)) {
					subnetwork.insertPipe(conn.a, conn.b);
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
		NetworkContents preSnapshot = currentContents.lazyClone();
		
		moveFluidsOneTick();
		
		final Map<Pair<TileEntity, TileEntityPipeBase>, FluidStack> inserters = inserters();
		for (Map.Entry<Pair<TileEntity, TileEntityPipeBase>, FluidStack> inserter : inserters.entrySet()) {
			final TileEntityPipeBase inserterPipe = inserter.getKey().y; // the pipe that is adjacent to the source
			final FluidStack fluidDrained = insertVia(inserterPipe, inserter.getKey().x).copy();
			List<Pair<FluidStack, Path>> drained = distributeFluidIntoSinks(inserterPipe, fluidDrained);
			
			for (Pair<FluidStack, Path> drainedFluidWithPath : drained) {
				int drainedAmount = currentContents.add(inserterPipe, drainedFluidWithPath.y, drainedFluidWithPath.x);
				if (drainedAmount > 0) {
					drainSource(inserter.getKey().x, drainedAmount, inserterPipe);
					fluidDrained.amount -= drainedAmount;
					if (fluidDrained.amount <= 0) break; // if the first sink takes all available fluid
				}
			}
		}
		
		updated(currentContents.differentFluids(preSnapshot));
	}
	
	private static void updated(Set<TileEntityPipeBase> pipes) {
		for (TileEntityPipeBase pipe : pipes)
			pipe.updated();
	}
	
	/**
	 * Drains the specified source by the specified amount. The targetPipe is only needed for the connecting block facing, but never used.
	 *
	 * @param source     the source to drain
	 * @param amount     how much to drain
	 * @param targetPipe used to calculate from which facing to extract
	 */
	public void drainSource(final TileEntity source, final int amount, final TileEntityPipeBase targetPipe) {
		final IFluidHandler handler = (IFluidHandler) source.getCapability(c,
				  BlockHelper.getConnectingFace(source.getPos(), targetPipe.getPos()));
		handler.drain(amount, true);
	}
	
	private void moveFluidsOneTick() {
		final NetworkContents nextContents = new NetworkContents();
		
		for (final Map.Entry<TileEntityPipeBase, Map<Path, FluidStack>> currentContent : currentContents.entrySet()) {
			// for every pipe that currently contains something
			
			if (currentContent.getValue().containsKey(null)) {
				
				// TODO reroute
				// TODO when moving something, check if the next path is free
				// re-route this particular fluid, it might have either been added manually
				// or part of its earlier path was destroyed
				FluidStack fluidInPipe = currentContent.getValue().get(null);
				List<Pair<FluidStack, Path>> sinks = distributeFluidIntoSinks(currentContent.getKey(), fluidInPipe);
				for (Pair<FluidStack, Path> sink : sinks) {
					sink.x.amount = Math.min(sink.x.amount, fluidInPipe.amount); // only insert as much as is in this pipe
					int drained = currentContents.add(currentContent.getKey(), sink.y, sink.x);
					// remove how much was drained from the remaining fluid.
					fluidInPipe.amount -= drained;
					if (fluidInPipe.amount <= 0) break;
				}
			}
			
			for (final Map.Entry<Path, FluidStack> fluidInPipe : currentContent.getValue().entrySet()) {
				
				if (fluidInPipe.getValue().amount == 0) continue;
				
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
					Path currentPath = new Path(fluidInPipe.getKey());
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
	 *  - whether all connections are valid
	 * FIXME blacklisted fluids
	 *
	 * TODO: test for chunkloading issues
	 */
	
	/**
	 * *After* the removal of the root pipe (or multiple pipes?) from the network, this checks which pipes are still connected, and which new
	 * networks we may need. For this I do a depth-search starting at different nodes until all nodes are found, and return a map with each
	 * new root mapped to its children.
	 * <p>
	 * Note that the key of each entry is always contained in the entry as well, this means that the children of each subnetwork contain
	 * their root as well.
	 * <p>
	 * There is another special case: if removedPipe is null, this method will simply return a map with the root of this network mapped to
	 * all children.
	 *
	 * @param removedPipe which pipe was removed, or null if the network didn't change
	 */
	@Nonnull
	private HashMap<TileEntityPipeBase, Set<TileEntityPipeBase>> divideIntoGroups(@Nullable TileEntityPipeBase removedPipe) {
		final Set<TileEntityPipeBase> unknown = new HashSet<>(4);
		for (final Connection<TileEntityPipeBase> connection : connections) {
			if (!connection.a.equals(removedPipe)) unknown.add(connection.a);
			if (!connection.b.equals(removedPipe)) unknown.add(connection.b);
		}
		
		final HashMap<TileEntityPipeBase, Set<TileEntityPipeBase>> groups = new HashMap<>(2);
		
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
					else return path.isEmpty() || to.isConnectedTo(path.get(path.size() - 1));
				});
				ds.discover();
				groups.put(next, ds.foundConnections.keySet());
				unknown.removeAll(ds.foundConnections.keySet());
			} catch (final NoSuchElementException ignored) {
			} // there are no more elements
		}
		
		return groups;
	}
	
	// TODO maybe change all path specifications from List to Set because the shortest
	//	will never use the same pipe twice
	// TODO maybe this is the wrong way: I shouldn't check for the shortest but all ways
	//  and add up the fluids I can transport over each which may be more than I can
	//  over the shortest only
	
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
	@Nonnull
	private List<Pair<FluidStack, Path>> distributeFluidIntoSinks(final TileEntityPipeBase source, final FluidStack fluid) {
		final BFSearcher sinkSearcher = new BFSearcher(source);
		
		// which new targets were found & added to the network contents during search
		final List<Pair<FluidStack, Path>> targets = new ArrayList<>(8);
		
		sinkSearcher.setValidator(new PathValidator(fluid));
		
		sinkSearcher.discover();
		DebugHelper.printMap(sinkSearcher.foundConnections);
		
		for (Connection<TileEntity> moduleConnection : fModules) {
			final Pair<TileEntityPipeBase, TileEntity> sorted = sortConnection(moduleConnection);
			TileEntityPipeBase sinkInserter = sorted.x;
			TileEntity sink = sorted.y;
			
			if (sinkInserter.getAttachment(BlockHelper.getConnectingFace(sinkInserter.getPos(), sink.getPos())).canExtract()) {
				
				// FIXME equal out if there are multiple sources set to IN & OUT
				
				final IFluidHandler handler = (IFluidHandler) sink.getCapability(c,
						  BlockHelper.getConnectingFace(sinkInserter.getPos(), sink.getPos()));
				final int maxSink = handler.fill(fluid, false);
				
				// the sink searcher is for one specific source block (see constructor)
				// foundConnections maps sink -> path to sink
				if (sinkSearcher.foundConnections.containsKey(sinkInserter)) {
					// the path to the inserting pipe:
					Path connection = sinkSearcher.foundConnections.get(sinkInserter);
					connection.add(sinkInserter);
					if (!connection.isEmpty()) {
						FluidStack transported = fluid.copy();
						transported.amount = Math.min(canTransport(connection, transported), maxSink);
						connection.remove(0); // remove the first tile because this is the one we're currently in
						
						// the newly found
						connection.setTarget(sink);
						targets.add(new Pair<FluidStack, Path>(transported, connection));
						//currentContents.add(source, connection, transported);
					} //else System.out.println("connection = 0");
				}
			}
		}
		return targets;
	}
	
	@Nullable
	public FluidStack getContents(@Nullable final TileEntityPipeBase pipe) {
		return currentContents.getContents(pipe);
	}
	
	public int fill(final TileEntityPipeBase pipe, final FluidStack resource, final boolean doFill) {
		FluidStack pipeContents = getContents(pipe);
		if (pipeContents == null || pipeContents.amount == 0) {
			FluidStack inserted = resource.copy();
			inserted.amount = Math.min(resource.amount, pipe.maxContents());
			if (doFill)
				currentContents.add(pipe, null, inserted);
			return inserted.amount;
		} else if (pipeContents.isFluidEqual(resource)) {
			FluidStack inserted = resource.copy();
			inserted.amount = Math.min(resource.amount, pipe.maxContents() - pipeContents.amount);
			if (doFill)
				currentContents.add(pipe, null, inserted);
			return inserted.amount;
		} else return 0; // no free space
	}
	
	private final class PathValidator implements ConnectionValidator {
		
		private final FluidStack fluidStack;
		
		private PathValidator(final FluidStack fluidStack) {
			this.fluidStack = fluidStack;
		}
		
		@Override
		public boolean isValid(@Nonnull final Path path, @Nonnull final TileEntityPipeBase to) {
			if (currentContents.canTransport(to, path.size(), fluidStack) == 0) return false;
			if (path.isEmpty()) return true;
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
		Map<Pair<TileEntity, TileEntityPipeBase>, FluidStack> inserters = new HashMap<>(8);
		
		for (Connection<TileEntity> module : fModules) {
			final Pair<TileEntityPipeBase, TileEntity> sorted = sortConnection(module);
			final FluidStack inserted = insertVia(sorted.x, sorted.y);
			if (inserted == null || inserted.amount == 0) continue;
			inserters.put(sorted.swap(), inserted);
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
			if (inserter.getAttachment(connectingFace).canInsert()) {
				final IFluidHandler handler = (IFluidHandler) source.getCapability(c, connectingFace.getOpposite());
				FluidStack inserted = handler.drain(inserter.maxContents(), false);
				if (inserted == null) return null;
				inserted.amount = currentContents.canTransport(inserter, 0, inserted);
				return inserted;
			} else return null;
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
	int canTransport(final Path path, final FluidStack fluidStack) {
		int amount = fluidStack.amount;
		final int pathSize = path.size();
		for (int i = 0; i < pathSize; i++) {
			// for every tile in the path, check if it limits the throughput
			amount = Math.min(amount, currentContents.canTransport(path.get(i), i, fluidStack));
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
	static int occupiedSpaceInPipe(final TileEntityPipeBase pipe, final FluidStack contents, final FluidStack added) {
		if (contents == null) return 0; // no fluid in the pipe
		if (contents.isFluidEqual(added)) return contents.amount; // the fluids can merge
		else return pipe.maxContents(); // the fluids can't merge
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
		
		DebugHelper.printMapSortedByValueProperty(ds.foundConnections, TileEntityPipeBase::toString, Path::size);
		
		System.out.println("===============================================");
	}
	
	public static PipeNetwork readFromNBT(final Capability<?> capability, World world, final BlockPos rootPos, final NBTTagCompound nbt) {
		if (world.isRemote) return null; // only create new network on server
		
		final NBTTagCompound networkTag = nbt.getCompoundTag("network");
		if (networkTag.hasKey("root")) { // rood tag might not be needed
			final NBTTagCompound networkRootTag = networkTag.getCompoundTag("root");
			
			PipeNetwork network = new PipeNetwork(capability, (TileEntityPipeBase) world.getTileEntity(rootPos));
			// from here on network != null
			
			if (networkTag.hasKey("data")) {
				
				final NBTTagCompound networkDataTag = networkTag.getCompoundTag("data");
				
				final NBTTagList pipeList = networkDataTag.getTagList("pipeList", 10);
				for (int i = 0; i < pipeList.tagCount(); i++) {
					final NBTTagCompound connection = pipeList.getCompoundTagAt(i);
					final BlockPos a = NBTHelper.readTagToBlockPos(connection.getCompoundTag("a"));
					final BlockPos b = NBTHelper.readTagToBlockPos(connection.getCompoundTag("b"));
					network.insertPipe((TileEntityPipeBase) world.getTileEntity(a),
							  (TileEntityPipeBase) world.getTileEntity(b));
				}
				
				final NBTTagList moduleList = networkDataTag.getTagList("moduleList", 10);
				for (int i = 0; i < moduleList.tagCount(); i++) {
					final NBTTagCompound module = moduleList.getCompoundTagAt(i);
					final TileEntity a = world.getTileEntity(
							  NBTHelper.readTagToBlockPos(module.getCompoundTag("a")));
					final TileEntity b = world.getTileEntity(
							  NBTHelper.readTagToBlockPos(module.getCompoundTag("b")));
					if (a instanceof TileEntityPipeBase && !(b instanceof TileEntityPipeBase))
						network.addModule((TileEntityPipeBase) a, b);
					else if (!(a instanceof TileEntityPipeBase) && b instanceof TileEntityPipeBase)
						network.addModule((TileEntityPipeBase) b, a);
					else
						throw new RuntimeException("wrong module @ " + a + " and " + b);
				}
				
				final NBTTagList contentList = networkDataTag.getTagList("contentList", 10);
				network.currentContents = NetworkContents.readContentFromNBT(world, contentList);
				
				return network;
				
			} else throw new RuntimeException("missing dataTag");
			
		} else throw new RuntimeException("missing root tag");
		
	}
	
	/**
	 * Generates a compound that can be stored on the root element and contains all data to restore this network.
	 *
	 * @return a compound with every connection of this system.
	 */
	public NBTTagCompound writeToNBT() {
		if (getRoot().getWorld().isRemote) return new NBTTagCompound();
		
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
	 * <p>
	 * 1. the search root is scanned for any children nodes (A and C). If they are valid (the validator returns true), they are added to both
	 * the foundConnections and unknown map (see discovered()). both those maps hold each discovered node and the path that leads to it, with
	 * the difference being that foundConnections are all connections that were found and unknown holds all nodes that still need to be
	 * scanned / indexed for their children.
	 * 2. the next node in the unknown map is scanned for children with each being added again to both
	 * maps if it is valid. The path to these children nodes consists of the path to their parent node + the parent node itself.
	 * 3. this is
	 * repeated until there are now more unknown nodes. At this point every node of the network with the shortest path to it should be in
	 * foundConnections.
	 * <p>
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
	protected class BFSearcher {
		
		// tepb block -> path
		protected final Map<TileEntityPipeBase, Path> foundConnections = new HashMap<>(4);
		// a new node to search from and the path to it
		private final Map<TileEntityPipeBase, Path> unknown = new HashMap<>(8);
		
		private ConnectionValidator validator;
		
		private static final long MAX_RUNS = 1000;
		
		protected BFSearcher(final TileEntityPipeBase searchRoot) {
			foundConnections.put(searchRoot, new Path(searchRoot));
			unknown.put(searchRoot, new Path(searchRoot));
			validator = (path, to) -> true;
		}
		
		protected void setValidator(@Nonnull final ConnectionValidator validator) {
			this.validator = validator;
		}
		
		private void discover() {
			long run = 0;
			// FIXME hardcoded max runs shouldn't be in production
			while (!unknown.isEmpty() && run < MAX_RUNS) {
				final Map.Entry<TileEntityPipeBase, Path> search = unknown.entrySet().iterator().next();
				unknown.remove(search.getKey());
				for (final Connection<TileEntityPipeBase> connection : PipeNetwork.this.connections) {
					final Path path = new Path(search.getValue());
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
		
		private void discovered(final Path path, final TileEntityPipeBase node) {
			final int distance = path.size();
			if (validator.isValid(path, node)) {
				boolean isShorter = true;
				if (foundConnections.containsKey(node)) {
					Path lastPath = foundConnections.get(node);
					// no null check needed here as maps can't contain null keys or values
					isShorter = distance < lastPath.size();
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
		
		boolean isValid(final Path path, final TileEntityPipeBase to);
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