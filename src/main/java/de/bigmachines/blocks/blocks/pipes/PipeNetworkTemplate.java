package de.bigmachines.blocks.blocks.pipes;

import de.bigmachines.utils.classes.Pair;
import net.minecraft.tileentity.TileEntity;
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
    final Set<Pair<BlockPos, BlockPos>> modules = new HashSet<>();

    protected PipeNetworkTemplate(Capability c, BlockPos rootPos) {
        this.c = c;
        this.rootPos = rootPos;
    }

    protected boolean addPipe(Pair<BlockPos, BlockPos> pipe) {
        return pipes.add(pipe);
    }
    protected boolean addModule(Pair<BlockPos, BlockPos> mod) {
        return modules.add(mod);
    }

    @Nonnull
    protected PipeNetwork realize(@Nonnull final World world) {
        final PipeNetwork network = new PipeNetwork(c, (TileEntityPipeBase) world.getTileEntity(rootPos));

        for (Pair<BlockPos, BlockPos> pipe : pipes)
            network.insert((TileEntityPipeBase) world.getTileEntity(pipe.x), (TileEntityPipeBase) world.getTileEntity(pipe.y));

        for (Pair<BlockPos, BlockPos> module : modules) {
            TileEntity x = world.getTileEntity(module.x);
            TileEntity y = world.getTileEntity(module.y);
            if (x instanceof TileEntityPipeBase && !(y instanceof TileEntityPipeBase))
                network.addModule((TileEntityPipeBase) x, y);
            else if (!(x instanceof TileEntityPipeBase) && y instanceof TileEntityPipeBase)
                network.addModule((TileEntityPipeBase) y, x);
            else
                throw new RuntimeException("wrong module @ " + module.x + " and " + module.y);
        }

        return network;
    }

}
