package com.zilulil.obsidianboat.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.zilulil.obsidianboat.client.model.ModelObsidianBoat;
import com.zilulil.obsidianboat.entity.EntityObsidianBoat;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderObsidianBoat extends Render
{
    private static final ResourceLocation boatTextures = new ResourceLocation("obsidianboat", "textures/entity/obsidianBoat.png");
    /** instance of ModelBoat for rendering */
    protected ModelBase modelObsidianBoat;

    public RenderObsidianBoat()
    {
        this.shadowSize = 0.5F;
        this.modelObsidianBoat = new ModelObsidianBoat();
    }

    /**
     * The render method used in RenderBoat that renders the boat model.
     */
    public void renderObsidianBoat(EntityObsidianBoat par1EntityObsidianBoat, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
        float f2 = (float)par1EntityObsidianBoat.getTimeSinceHit() - par9;
        float f3 = par1EntityObsidianBoat.getDamageTaken() - par9;

        if (f3 < 0.0F)
        {
            f3 = 0.0F;
        }

        if (f2 > 0.0F)
        {
            GL11.glRotatef(MathHelper.sin(f2) * f2 * f3 / 10.0F * (float)par1EntityObsidianBoat.getForwardDirection(), 1.0F, 0.0F, 0.0F);
        }

        float f4 = 0.75F;
        GL11.glScalef(f4, f4, f4);
        GL11.glScalef(1.0F / f4, 1.0F / f4, 1.0F / f4);
        this.bindEntityTexture(par1EntityObsidianBoat);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        this.modelObsidianBoat.render(par1EntityObsidianBoat, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    protected ResourceLocation getBoatTextures(EntityObsidianBoat par1EntityBoat)
    {
        return boatTextures;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.getBoatTextures((EntityObsidianBoat)par1Entity);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.renderObsidianBoat((EntityObsidianBoat)par1Entity, par2, par4, par6, par8, par9);
    }
}
