package de.bigmachines.multiblocks.validators;

import de.bigmachines.multiblocks.MultiblockBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.Template;

public class MultiblockValidatorTemplate extends MultiblockValidator {
	
	public MultiblockValidatorTemplate(MultiblockBase multiblock) {
		super(multiblock);
	}

	@Override
	public CheckMultiBlockResponse checkMultiBlock(World world, BlockPos pos) {
		return new CheckMultiBlockResponse(false, null);
	}
	
}
