package de.bigmachines.world;

import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.JsonDeserializer;

import de.bigmachines.world.WorldGeneratorStructure.Generator;

public class ModWorldGenerator implements IWorldGenerator {

	public static final List<WorldGeneratorBase> generators = new ArrayList<>();
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		for(WorldGeneratorBase generator : generators) {
			generator.generateChunk(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
		}
	}

	public static void addGenerator(WorldGeneratorBase generator) {
		generators.add(generator);
	}
	
	public static BlockPos getGroundFromBelow(World world, int x, int z) {
		BlockPos pos = new BlockPos(x, 1, z);
		for(; (pos.getY() <= 255) && !(world.getBlockState(pos).getBlock() instanceof BlockAir); pos = pos.add(0, 1, 0)) {}
		return pos;
	}
	
	public static BlockPos getGroundFromAbove(World world, int x, int z) {
		BlockPos pos = new BlockPos(x, 255, z);
		for(; (pos.getY() > 0) && (
				(world.getBlockState(pos).getBlock() instanceof BlockAir)
				 || (world.getBlockState(pos).getBlock().isReplaceable(world, pos))
				 || (world.getBlockState(pos).getBlock() instanceof BlockLog)
				 || (world.getBlockState(pos).getBlock() instanceof BlockLeaves)
				); pos = pos.add(0, -1, 0)) {}
		return pos.add(0, 1, 0);
	}
	
}
