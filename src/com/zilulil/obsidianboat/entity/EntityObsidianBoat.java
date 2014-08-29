package com.zilulil.obsidianboat.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.zilulil.obsidianboat.ObsidianBoat;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityObsidianBoat extends EntityBoat
{
    private boolean isBoatEmpty;
    private double speedMultiplier;
    private int boatPosRotationIncrements;
    private double boatX;
    private double boatY;
    private double boatZ;
    private double boatYaw;
    private double boatPitch;
    @SideOnly(Side.CLIENT)
    private double velocityX;
    @SideOnly(Side.CLIENT)
    private double velocityY;
    @SideOnly(Side.CLIENT)
    private double velocityZ;
    //replacement for ID maybe?
    private static final String __OBFID = "CL_15000002";

    public EntityObsidianBoat(World par1World)
    {
        super(par1World);
        this.isImmuneToFire = true;
        
    }

    public EntityObsidianBoat(World par1World, double par2, double par4, double par6)
    {
        this(par1World);
        this.setPosition(par2, par4 + (double)this.yOffset, par6);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = par2;
        this.prevPosY = par4;
        this.prevPosZ = par6;
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset()
    {
    	//looks weird but required to keep player out of lava
        return (double)this.height * 0.7D - 0.04050001192092896D;
    }
    

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else if (DamageSource.lava == par1DamageSource)
        {
        	return false;
        }
        else if ("fireball".equals(par1DamageSource.getDamageType())){
        	return false;
        }
        else if(par1DamageSource.isExplosion() && par2 <= 34){
        	return false;
        }
        else if (!this.worldObj.isRemote && !this.isDead)
        {
            this.setForwardDirection(-this.getForwardDirection());
            this.setTimeSinceHit(10);
            this.setDamageTaken(this.getDamageTaken() + par2 * 10.0F);
            this.setBeenAttacked();
            boolean flag = par1DamageSource.getEntity() instanceof EntityPlayer && ((EntityPlayer)par1DamageSource.getEntity()).capabilities.isCreativeMode;

            if (flag || this.getDamageTaken() > 40.0F)
            {
                if (this.riddenByEntity != null)
                {
                    this.riddenByEntity.mountEntity(this);
                }

                if (!flag)
                {
                	if (par1DamageSource.getEntity() instanceof EntityPlayer){
                		boolean added = ((EntityPlayer) par1DamageSource.getEntity()).inventory.addItemStackToInventory(new ItemStack(ObsidianBoat.obsidianBoat, 1));
                		if (!added)
                			this.func_145778_a(ObsidianBoat.obsidianBoat, 1, 0.0F);
                	}
                	else{
                		this.func_145778_a(ObsidianBoat.obsidianBoat, 1, 0.0F);
                	}
                }
                this.setDead();
            }

            return true;
        }
        else
        {
            return true;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate()
    {
        //super.onUpdate();
    	this.onEntityUpdate();

        if (this.getTimeSinceHit() > 0)
        {
            this.setTimeSinceHit(this.getTimeSinceHit() - 1);
        }

        if (this.getDamageTaken() > 0.0F)
        {
            this.setDamageTaken(this.getDamageTaken() - 1.0F);
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        byte b0 = 5;
        double d0 = 0.0D;
        boolean inLavaFlag = false;
        
        for (int i = 0; i < b0; ++i)
        {
            double d1 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(i + 0) / (double)b0 - 0.125D;
            double d3 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(i + 1) / (double)b0 - 0.125D;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(this.boundingBox.minX, d1, this.boundingBox.minZ, this.boundingBox.maxX, d3, this.boundingBox.maxZ);

            if (this.worldObj.isAABBInMaterial(axisalignedbb, Material.water))
            {
                d0 += 1.0D / (double)b0;
            }
            else if (this.worldObj.isAABBInMaterial(axisalignedbb, Material.lava)){
                d0 += 1.0D / (double)b0;
                inLavaFlag = true;
            }
        }

        double d10 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        double d2;
        double d4;
        int j;

        if (d10 > 0.26249999999999996D)
        {
            d2 = Math.cos((double)this.rotationYaw * Math.PI / 180.0D);
            d4 = Math.sin((double)this.rotationYaw * Math.PI / 180.0D);

            for (j = 0; (double)j < 1.0D + d10 * 60.0D; ++j)
            {
                double d5 = (double)(this.rand.nextFloat() * 2.0F - 1.0F);
                double d6 = (double)(this.rand.nextInt(2) * 2 - 1) * 0.7D;
                double d8;
                double d9;
                if (!inLavaFlag){
	                if (this.rand.nextBoolean())
	                {
	                    d8 = this.posX - d2 * d5 * 0.8D + d4 * d6;
	                    d9 = this.posZ - d4 * d5 * 0.8D - d2 * d6;
	                    this.worldObj.spawnParticle("splash", d8, this.posY - 0.125D, d9, this.motionX, this.motionY, this.motionZ);
	                }
	                else
	                {
	                    d8 = this.posX + d2 + d4 * d5 * 0.7D;
	                    d9 = this.posZ + d4 - d2 * d5 * 0.7D;
	                    this.worldObj.spawnParticle("splash", d8, this.posY - 0.125D, d9, this.motionX, this.motionY, this.motionZ);
	                }
                }
                else{
	                if (this.rand.nextBoolean())
	                {
	                	d8 = this.posX - d2 * d5 * 0.8D + d4 * d6;
	                    d9 = this.posZ - d4 * d5 * 0.8D - d2 * d6;
	                    this.worldObj.spawnParticle("flame", d8, this.posY - 0.125D, d9, this.motionX, this.motionY, this.motionZ);
	                }
	                else
	                {
	                	d8 = this.posX + d2 + d4 * d5 * 0.7D;
	                    d9 = this.posZ + d4 - d2 * d5 * 0.7D;
	                    this.worldObj.spawnParticle("flame", d8, this.posY - 0.125D, d9, this.motionX, this.motionY, this.motionZ);
	                }
                }
            }
        }

        double d11;
        double d12;

        if (this.worldObj.isRemote && this.isBoatEmpty)
        {
            if (this.boatPosRotationIncrements > 0)
            {
                d2 = this.posX + (this.boatX - this.posX) / (double)this.boatPosRotationIncrements;
                d4 = this.posY + (this.boatY - this.posY) / (double)this.boatPosRotationIncrements;
                d11 = this.posZ + (this.boatZ - this.posZ) / (double)this.boatPosRotationIncrements;
                d12 = MathHelper.wrapAngleTo180_double(this.boatYaw - (double)this.rotationYaw);
                this.rotationYaw = (float)((double)this.rotationYaw + d12 / (double)this.boatPosRotationIncrements);
                this.rotationPitch = (float)((double)this.rotationPitch + (this.boatPitch - (double)this.rotationPitch) / (double)this.boatPosRotationIncrements);
                --this.boatPosRotationIncrements;
                this.setPosition(d2, d4, d11);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
            else
            {
                d2 = this.posX + this.motionX;
                d4 = this.posY + this.motionY;
                d11 = this.posZ + this.motionZ;
                this.setPosition(d2, d4, d11);

                if (this.onGround)
                {
                    this.motionX *= 0.5D;
                    this.motionY *= 0.5D;
                    this.motionZ *= 0.5D;
                }

                this.motionX *= 0.9900000095367432D;
                this.motionY *= 0.949999988079071D;
                this.motionZ *= 0.9900000095367432D;
            }
        }
        else
        {
            if (d0 < 1.0D)
            {
                d2 = d0 * 2.0D - 1.0D;
                this.motionY += 0.03999999910593033D * d2;
            }
            else
            {
                if (this.motionY < 0.0D)
                {
                    this.motionY /= 2.0D;
                }

                this.motionY += 0.007000000216066837D;
            }

            if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityLivingBase)
            {
                EntityLivingBase entitylivingbase = (EntityLivingBase)this.riddenByEntity;
                entitylivingbase.extinguish();
                entitylivingbase.addPotionEffect(new PotionEffect(12, 360));
         
                float f = this.riddenByEntity.rotationYaw + -entitylivingbase.moveStrafing * 90.0F;
                this.motionX += -Math.sin((double)(f * (float)Math.PI / 180.0F)) * this.speedMultiplier * (double)entitylivingbase.moveForward * 0.05000000074505806D;
                this.motionZ += Math.cos((double)(f * (float)Math.PI / 180.0F)) * this.speedMultiplier * (double)entitylivingbase.moveForward * 0.05000000074505806D;
                
            }

            d2 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

            if (d2 > 0.35D)
            {
                d4 = 0.35D / d2;
                this.motionX *= d4;
                this.motionZ *= d4;
                d2 = 0.35D;
            }

            if (d2 > d10 && this.speedMultiplier < 0.35D)
            {
                this.speedMultiplier += (0.35D - this.speedMultiplier) / 35.0D;

                if (this.speedMultiplier > 0.35D)
                {
                    this.speedMultiplier = 0.35D;
                }
            }
            else
            {
                this.speedMultiplier -= (this.speedMultiplier - 0.07D) / 35.0D;

                if (this.speedMultiplier < 0.07D)
                {
                    this.speedMultiplier = 0.07D;
                }
            }

            int l;

            for (l = 0; l < 4; ++l)
            {
                int i1 = MathHelper.floor_double(this.posX + ((double)(l % 2) - 0.5D) * 0.8D);
                j = MathHelper.floor_double(this.posZ + ((double)(l / 2) - 0.5D) * 0.8D);

                for (int j1 = 0; j1 < 2; ++j1)
                {
                    int k = MathHelper.floor_double(this.posY) + j1;
                    Block block = this.worldObj.getBlock(i1, k, j);

                    if (block == Blocks.snow_layer)
                    {
                        this.worldObj.setBlockToAir(i1, k, j);
                        this.isCollidedHorizontally = false;
                    }
                    else if (block == Blocks.waterlily)
                    {
                        this.worldObj.func_147480_a(i1, k, j, true);
                        this.isCollidedHorizontally = false;
                    }
                }
            }
            
            if (this.onGround)
            {
                this.motionX *= 0.5D;
                this.motionY *= 0.5D;
                this.motionZ *= 0.5D;
            }

            this.moveEntity(this.motionX, this.motionY, this.motionZ);

            if (this.isCollidedHorizontally && d10 > 0.2D)
            {
            	/*
                if (!this.worldObj.isRemote && !this.isDead)
                {
                    this.setDead();

                    this.dropItemWithOffset(Block.obsidian.blockID, 5, 0.0F);

                }
                */
            }
            else
            {
                this.motionX *= 0.9900000095367432D;
                this.motionY *= 0.949999988079071D;
                this.motionZ *= 0.9900000095367432D;
            }

            this.rotationPitch = 0.0F;
            d4 = (double)this.rotationYaw;
            d11 = this.prevPosX - this.posX;
            d12 = this.prevPosZ - this.posZ;

            if (d11 * d11 + d12 * d12 > 0.001D)
            {
                d4 = (double)((float)(Math.atan2(d12, d11) * 180.0D / Math.PI));
            }

            double d7 = MathHelper.wrapAngleTo180_double(d4 - (double)this.rotationYaw);

            if (d7 > 20.0D)
            {
                d7 = 20.0D;
            }

            if (d7 < -20.0D)
            {
                d7 = -20.0D;
            }

            this.rotationYaw = (float)((double)this.rotationYaw + d7);
            this.setRotation(this.rotationYaw, this.rotationPitch);

            if (!this.worldObj.isRemote)
            {
                List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));

                if (list != null && !list.isEmpty())
                {
                    for (int k1 = 0; k1 < list.size(); ++k1)
                    {
                        Entity entity = (Entity)list.get(k1);

                        if (entity != this.riddenByEntity && entity.canBePushed() && entity instanceof EntityBoat)
                        {
                            entity.applyEntityCollision(this);
                        }
                    }
                }

                if (this.riddenByEntity != null && this.riddenByEntity.isDead)
                {
                    this.riddenByEntity = null;
                }
            }
        }
    }

    /**
     * Takes in the distance the entity has fallen this tick and whether its on the ground to update the fall distance
     * and deal fall damage if landing on the ground.  Args: distanceFallenThisTick, onGround
     */
    protected void updateFallState(double par1, boolean par3)
    {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.posY);
        int k = MathHelper.floor_double(this.posZ);

        if (par3)
        {
            if (this.fallDistance > 3.0F)
            {
                this.fall(this.fallDistance);
                this.fallDistance = 0.0F;
            }
        }
        else if ((this.worldObj.getBlock(i, j - 1, k).getMaterial() != Material.water  && this.worldObj.getBlock(i,  j - 1,  k).getMaterial() != Material.lava) && par1 < 0.0D)
        {
            this.fallDistance = (float)((double)this.fallDistance - par1);
        }
    }
}
