package de.bigmachines.network.messages;

import de.bigmachines.blocks.blocks.pipes.TileEntityPipeBase;
import de.bigmachines.blocks.blocks.pipes.TileEntityPipeBase.PipeAttachment;
import de.bigmachines.utils.classes.RedstoneMode;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageChangePipeAttachmentMode implements IMessage {
	
	public BlockPos pos;
	public EnumFacing side;
	public RedstoneMode redstoneMode;
	public boolean whitelist;
	public boolean canExtract;
	public boolean canInsert;
	
	public MessageChangePipeAttachmentMode(BlockPos pos, EnumFacing side, RedstoneMode redstoneMode, boolean whitelist, boolean canExtract, boolean canInsert) {
		this.pos = pos;
		this.side = side;
		this.redstoneMode = redstoneMode;
		this.whitelist = whitelist;
		this.canExtract = canExtract;
		this.canInsert = canInsert;
	}
	
	public MessageChangePipeAttachmentMode() {
		
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		int x = buf.readInt();
		int y = buf.readInt();
		int z = buf.readInt();
		this.pos = new BlockPos(x, y, z);
		this.side = EnumFacing.getFront(buf.readByte());
		this.redstoneMode = RedstoneMode.values()[buf.readByte()];
		this.whitelist = buf.readBoolean();
		this.canExtract = buf.readBoolean();
		this.canInsert = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeByte(side.getIndex());
		buf.writeByte(redstoneMode.ordinal());
		buf.writeBoolean(whitelist);
		buf.writeBoolean(canExtract);
		buf.writeBoolean(canInsert);
	}
	
	public static class Handler implements IMessageHandler<MessageChangePipeAttachmentMode, IMessage>{

		@Override
		public IMessage onMessage(MessageChangePipeAttachmentMode message, MessageContext ctx) {
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
				TileEntity tile = ctx.getServerHandler().player.getServerWorld().getTileEntity(message.pos);
				if(tile instanceof TileEntityPipeBase) {
					TileEntityPipeBase tileEntityPipeBase = (TileEntityPipeBase) tile;
					PipeAttachment attachment = tileEntityPipeBase.getAttachment(message.side);
					attachment.setWhitelist(message.whitelist);
					attachment.setRedstoneMode(message.redstoneMode);
					attachment.setCanExtract(message.canExtract);
					attachment.setCanInsert(message.canInsert);
					tileEntityPipeBase.updated();
				}
			});
			return null;
		}
		
	}
	
	
	
}
