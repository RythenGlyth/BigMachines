package de.bigmachines.world;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class WorldGeneratorMineable extends WorldGeneratorBase {
	
	private WorldGenMinable generator;
	
	private IBlockState blockState;
	private int maxPerChunk;
	private int minHeight;
	private int maxHeight;

	public WorldGeneratorMineable(IBlockState blockState, int maxPerChunk, int blockcount, int minHeight, int maxHeight) {
		this.blockState = blockState;
		this.maxPerChunk = maxPerChunk;
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
		
		this.generator = new WorldGenMinable(blockState, blockcount);
	}
	
	@Override
	public void generateChunk(Random random, int chunkX, int chunkY, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		//chunk to world coordinates
		int x = chunkX * 16;
		int z = chunkY * 16;
		for (int i = 0; i < this.maxPerChunk; i++) {
			int randPosX = x + random.nextInt(16);
			int randPosY = random.nextInt(this.maxHeight - this.minHeight) + this.minHeight;
			int randPosZ = z + random.nextInt(16);
			generator.generate(world, random, new BlockPos(randPosX, randPosY, randPosZ));
		}
	}
	
}