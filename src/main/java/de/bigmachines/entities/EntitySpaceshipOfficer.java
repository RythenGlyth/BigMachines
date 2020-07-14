package de.bigmachines.entities;

import de.bigmachines.BigMachines;
import de.bigmachines.Reference;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntitySpaceshipOfficer extends EntityMob {

	public EntitySpaceshipOfficer(World worldIn) {
		super(worldIn);
	}
	
	public static void initialize(int id) { 
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "spaceship_officer"),
				EntitySpaceshipOfficer.class,
				"bigmachines.spaceship_officer", id, BigMachines.INSTANCE, 64, 1, true);
		
	}
	
	@Override
	protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 2.0D));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(3, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(4, new EntityAIWatchClosest2(this, EntityPlayer.class, 12.0F, 1.0F));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 0.6D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        
        this.targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}
	
	@Override
	protected void applyEntityAttributes() {
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ARMOR);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS);
        this.getAttributeMap().registerAttribute(SWIM_SPEED);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
	}

	/*@Override
	public EntitySpaceshipOfficer createChild(EntityAgeable ageable) {
		EntitySpaceshipOfficer entitySpaceshipOfficer = new EntitySpaceshipOfficer(this.world);
		entitySpaceshipOfficer.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitySpaceshipOfficer)), (IEntityLivingData)null);
        return entitySpaceshipOfficer;
	}*/
	
}
