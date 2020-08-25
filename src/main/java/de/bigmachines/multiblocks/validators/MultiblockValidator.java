package de.bigmachines.multiblocks.validators;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.meta.When;

import de.bigmachines.multiblocks.MultiblockBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class MultiblockValidator {
	
	public final MultiblockBase multiblock;
	
	public MultiblockValidator(MultiblockBase multiblock) {
		this.multiblock = multiblock;
	}
	
	public abstract CheckMultiBlockResponse checkMultiBlock(World world, BlockPos pos);
	
	public static class CheckMultiBlockResponse {
		
		public final boolean isMultiblock;
		
		@Nullable
		public final BlockPos origin;
		
		public CheckMultiBlockResponse(boolean isMultiblock, BlockPos origin) {
			this.isMultiblock = isMultiblock;
			this.origin = origin;
		}
		
	}
	
}
