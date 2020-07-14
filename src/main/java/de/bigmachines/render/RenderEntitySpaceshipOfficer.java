package de.bigmachines.render;

import de.bigmachines.entities.EntitySpaceshipOfficer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEntitySpaceshipOfficer extends RenderLiving<EntitySpaceshipOfficer> {

    private static final ResourceLocation VILLAGER_TEXTURES = new ResourceLocation("textures/entity/villager/villager.png");
	
	public RenderEntitySpaceshipOfficer(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelVillager(0.0F), 0.5F);
        this.addLayer(new LayerCustomHead(this.getMainModel().villagerHead));
    }

    public ModelVillager getMainModel()
    {
        return (ModelVillager)super.getMainModel();
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntitySpaceshipOfficer entity) {
        return new ResourceLocation("textures/entity/zombie/zombie.png");
    }

    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    protected void preRenderCallback(EntityVillager entitylivingbaseIn, float partialTickTime) {
        float f = 0.9375F;

        if (entitylivingbaseIn.getGrowingAge() < 0)
        {
            f = (float)((double)f * 0.5D);
            this.shadowSize = 0.25F;
        }
        else
        {
            this.shadowSize = 0.5F;
        }

        GlStateManager.scale(f, f, f);
    }
	
}
