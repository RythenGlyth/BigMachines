package de.bigmachines.config.worldGeneration;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class WorldGeneratorMineable extends WorldGenerator {
	
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
	
	public void generate(Random random, int x, int y, World world) {
		for (int i = 0; i < this.maxPerChunk; i++) {
			int randPosX = x + random.nextInt(16);
			int randPosY = random.nextInt(this.maxHeight - this.minHeight) + this.minHeight;
			int randPosZ = y + random.nextInt(16);
			generator.generate(world, random, new BlockPos(randPosX, randPosY, randPosZ));
		}
	}
	
}
