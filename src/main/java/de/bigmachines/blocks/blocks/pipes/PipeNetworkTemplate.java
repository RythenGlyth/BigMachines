package de.bigmachines.blocks.blocks.pipes;

import de.bigmachines.utils.classes.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class PipeNetworkTemplate {
    final BlockPos rootPos;
    final Capability c;
    final Set<Pair<BlockPos, BlockPos>> pipes = new HashSet<>();

    protected PipeNetworkTemplate(Capability c, BlockPos rootPos) {
        this.c = c;
        this.rootPos = rootPos;
    }

    protected void addPipe(Pair<BlockPos, BlockPos> pipe) {
        pipes.add(pipe);
    }

    @Nonnull
    protected PipeNetwork realize(@Nonnull final World world) {
        final PipeNetwork network = new PipeNetwork(c, (TileEntityPipeBase) world.getTileEntity(rootPos));


        return network;
    }

}
