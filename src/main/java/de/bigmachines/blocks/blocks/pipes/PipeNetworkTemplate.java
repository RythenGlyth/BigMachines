package de.bigmachines.blocks.blocks.pipes;

import de.bigmachines.utils.NBTHelper;
import de.bigmachines.utils.classes.Pair;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
    /**
     * Reconstructs a network based on an nbt compound that was generated for it.
     *
     * @param compound an nbt compound as returned by #rootCompound()
     */
    protected static PipeNetworkTemplate genTemplate(final Capability<?> c, final BlockPos root, final NBTTagCompound compound) {
        PipeNetworkTemplate template = new PipeNetworkTemplate(c, root);

        // type 10 for NBTTagCompound
        NBTTagList pipeList = compound.getTagList("pipeList", 10);
        NBTTagList moduleList = compound.getTagList("moduleList", 10);

        for (int i = 0; i < pipeList.tagCount(); i++) {
            NBTTagCompound connection = pipeList.getCompoundTagAt(i);
            Pair<BlockPos, BlockPos> conn = new Pair<>(NBTHelper.readTagToBlockPos(connection.getCompoundTag("a")),
                    NBTHelper.readTagToBlockPos(connection.getCompoundTag("b")));
            template.addPipe(conn);
        }

        for (int i = 0; i < moduleList.tagCount(); i++) {
            NBTTagCompound module = moduleList.getCompoundTagAt(i);
            Pair<BlockPos, BlockPos> mod = new Pair<>(NBTHelper.readTagToBlockPos(module.getCompoundTag("a")),
                    NBTHelper.readTagToBlockPos(module.getCompoundTag("b")));
            template.addModule(mod);
        }

        return template;

        // TODO testing
        // TODO try-catch all the casting
    }

