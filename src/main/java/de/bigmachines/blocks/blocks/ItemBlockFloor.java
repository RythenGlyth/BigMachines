package de.bigmachines.blocks.blocks;

import de.bigmachines.blocks.ItemBlockBase;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockFloor extends ItemBlockBase {

	public ItemBlockFloor(Block block) {
		super(block);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack itemstack = player.getHeldItem(hand);

        if (!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack)) {
        	IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();
            BlockPos newPos = pos;
            
            if((block != this.block || (facing != (BlockFloor.EnumBlockHalf.TOP.equals(iblockstate.getValue(BlockFloor.HALF)) ? EnumFacing.DOWN : EnumFacing.UP))) && !block.isReplaceable(worldIn, pos)) {
            	iblockstate = worldIn.getBlockState(pos.offset(facing));
                block = iblockstate.getBlock();
                newPos = pos.offset(facing);
            }
            
            if(block == this.block) {
            	int i = ((Integer)iblockstate.getValue(BlockFloor.LAYERS)).intValue();
            	if(i < 8) {
            		IBlockState iblockstate1 = iblockstate.withProperty(BlockFloor.LAYERS, Integer.valueOf(i + 1));
                    AxisAlignedBB axisalignedbb = iblockstate1.getCollisionBoundingBox(worldIn, newPos);
                    
                    if (axisalignedbb != Block.NULL_AABB && worldIn.checkNoEntityCollision(axisalignedbb.offset(newPos)) && worldIn.setBlockState(newPos, iblockstate1, 10)) {
                        SoundType soundtype = this.block.getSoundType(iblockstate1, worldIn, newPos, player);
                        worldIn.playSound(player, newPos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

                        if (player instanceof EntityPlayerMP) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, newPos, itemstack);
                        }

                        itemstack.shrink(1);
                        return EnumActionResult.SUCCESS;
                    }
            	}
            }
    		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        } else {
            return EnumActionResult.FAIL;
        }
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
        IBlockState state = worldIn.getBlockState(pos);
        if(state.getBlock() == this.block && side == (BlockFloor.EnumBlockHalf.TOP.equals(state.getValue(BlockFloor.HALF)) ? EnumFacing.DOWN : EnumFacing.UP) && ((Integer)state.getValue(BlockFloor.LAYERS)) < 8) {
        	return true;
        } else {
            state = worldIn.getBlockState(pos.offset(side));
            if(state.getBlock() == this.block && ((Integer)state.getValue(BlockFloor.LAYERS)) < 8) {
            	return true;
            }
        }
        return super.canPlaceBlockOnSide(worldIn, pos, side, player, stack);
	}
	
}
