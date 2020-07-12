package de.bigmachines.multiblocks;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

public abstract class MultiblockBase implements IMultiblock {
	
	public ResourceLocation id;
	
	public MultiblockBase(ResourceLocation id) {
		this.id = id;
	}
	
	@Override
	public ResourceLocation getID() {
		return id;
	}
	
	@Override
	public Vec3i getSize() {
		return null;
	}
	
	protected Template getTemplate() {
		return null;
	}
	
}
