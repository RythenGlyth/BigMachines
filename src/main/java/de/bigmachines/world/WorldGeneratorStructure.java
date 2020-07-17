package de.bigmachines.world;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import de.bigmachines.Reference;
import de.bigmachines.config.WorldGenerationConfig;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class WorldGeneratorStructure extends WorldGeneratorBase {
	
	public final WorldGeneratorStructure.Generator generator;
	public final Integer chance;
	
	public WorldGeneratorStructure(WorldGeneratorStructure.Generator generator, List<Integer> blacklistedDimensions, Integer chance) {
		super(blacklistedDimensions);
		this.generator = generator;
		this.chance = Math.max(1, chance);
	}
	
	@Override
	public void generateChunk(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if(!isDimensionBlacklisted(world.provider.getDimension())) {
			int x = (chunkX * 16) + random.nextInt(15);
			int z = (chunkZ * 16) + random.nextInt(15);
			BlockPos pos = ModWorldGenerator.getGroundFromAbove(world, x, z);
			if(!(world.getBlockState(pos).getBlock() instanceof BlockLiquid) && random.nextInt(chance) == 0) {
				generator.generate(world, random, pos);
			}
		}
	}

	public static class WorldGeneratorStructureDeserializer implements JsonDeserializer<WorldGeneratorStructure> {

		@Override
		public WorldGeneratorStructure deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonObj = json.getAsJsonObject();
			
			List<Integer> blacklistedDimensions;
			if(jsonObj.has("blacklisted-dimensions") && jsonObj.get("blacklisted-dimensions").isJsonArray()) {
				blacklistedDimensions = WorldGenerationConfig.gson.fromJson(jsonObj.get("blacklisted-dimensions").getAsJsonArray(), new TypeToken<List<Integer>>(){}.getType());
			} else {
				blacklistedDimensions = new ArrayList<>();
			}
			
			return new WorldGeneratorStructure(
					new WorldGeneratorStructure.Generator(new ResourceLocation(jsonObj.has("structure-id") ? jsonObj.get("structure-id").getAsString() : "")),
					blacklistedDimensions,
					jsonObj.has("chance") ? jsonObj.get("chance").getAsInt() : 10000
			);
		}
		
	}
	
	public static class Generator extends WorldGenerator {
		
		public ResourceLocation structureLocation;
		public PlacementSettings placementSettings;
		
		public Generator(ResourceLocation structureName) {
			this(structureName,
					new PlacementSettings().setChunk(null).setIgnoreEntities(false).setIgnoreStructureBlock(false)
			);
		}
		
		public Generator(ResourceLocation structureName, PlacementSettings placementSettings) {
			this.structureLocation = structureName;
			this.placementSettings = placementSettings;
		}
		
		@Override
		public boolean generate(World worldIn, Random rand, BlockPos pos) {
			MinecraftServer mcServer = worldIn.getMinecraftServer();
			WorldServer worldServer = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
			TemplateManager templateManager = worldServer.getStructureTemplateManager();
			Template template = templateManager.get(mcServer, structureLocation);
			
			if(template != null) {
				IBlockState state = worldIn.getBlockState(pos);
				worldIn.notifyBlockUpdate(pos, state, state, 3);
				template.addBlocksToWorldChunk(worldIn, pos, placementSettings.setRotation(Rotation.values()[rand.nextInt(Rotation.values().length)]).setMirror(Mirror.values()[rand.nextInt(Mirror.values().length)]));
			} else {
				System.out.println(structureLocation + " was not found"); 
			}
			return true;
		}
		
	}

}