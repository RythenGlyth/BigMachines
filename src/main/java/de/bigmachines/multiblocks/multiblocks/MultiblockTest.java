package de.bigmachines.multiblocks.multiblocks;

import de.bigmachines.Reference;
import de.bigmachines.multiblocks.MultiblockBase;
import de.bigmachines.multiblocks.validators.MultiblockValidator;
import de.bigmachines.multiblocks.validators.MultiblockValidatorTemplate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class MultiblockTest extends MultiblockBase {
	
	MultiblockValidatorTemplate validator;
	
	public MultiblockTest() {
		super(new ResourceLocation(Reference.MOD_ID, "multiblocks/test"));
		this.validator = new MultiblockValidatorTemplate(this);
	}

	@Override
	public Vec3i getSize() {
		return null;
	}

	@Override
	public boolean createStructure(World world, BlockPos pos, EnumFacing side, EntityPlayer player) {
		return false;
	}

	@Override
	public boolean breakStructure(World world, BlockPos pos, EntityPlayer player) {
		return false;
	}
	
	@Override
	public MultiblockValidator getValidator() {
		return validator;
	}
	
}
