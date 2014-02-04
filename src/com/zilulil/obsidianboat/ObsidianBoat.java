package com.zilulil.obsidianboat;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import com.zilulil.obsidianboat.entity.EntityObsidianBoat;
import com.zilulil.obsidianboat.items.ItemObsidianBoat;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid=OBInfo.ID,name=OBInfo.NAME,version=OBInfo.VERS)
public class ObsidianBoat {
	
	
	// The instance of your mod that Forge uses.
	@Instance(value = OBInfo.ID)
	public static ObsidianBoat instance;
		
	
	
	//Telling forge that we are creating these
	//items
	public static final Item obsidianBoat = new ItemObsidianBoat().setUnlocalizedName("obsidianBoat").setTextureName("obsidianboat:obsidianBoat");
	public static boolean useDiamond;
	//blocks

	//tools

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide=OBInfo.CLIENT_PROXY + "ClientProxy", serverSide=OBInfo.COMMON_PROXY + "CommonProxy")
	public static CommonProxy proxy;

	@EventHandler // used in 1.6.2
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		useDiamond = config.get(Configuration.CATEGORY_GENERAL, "useDiamondInBoat", false, "Enable more expensive crafting recipe that requires a diamond in the bottom middle slot").getBoolean(false);
		config.save();
		
		//Why do I have to do this here instead of in the mcmod.info?
		event.getModMetadata().authorList.add("Zilulil");
	}


	@EventHandler
	public void load(FMLInitializationEvent event){
		// register items
		GameRegistry.registerItem(obsidianBoat, "obsidianBoat");
		
		// define blocks
		
		//adding names
		// LanguageRegistry.addName(obsidianBoat, "Obsidian Boat");
		//items

		//blocks
		//crafting
		if(!useDiamond){
			GameRegistry.addRecipe(new ItemStack(obsidianBoat, 1), new Object[]{
				"   ","T T","TTT",'T',Blocks.obsidian,
			});
		}
		else{
			GameRegistry.addRecipe(new ItemStack(obsidianBoat, 1), new Object[]{
				"   ","T T","TDT", 'T',Blocks.obsidian, 'D',Items.diamond,
			});
		}
		
		proxy.registerRenderers();
		
		//register entities
		int id = 0;
		EntityRegistry.registerModEntity(EntityObsidianBoat.class, "ObsidianBoat", id++, this, 80, 1, true);
		// LanguageRegistry.instance().addStringLocalization("entity.EntityObsidianBoat.name", "Obsidian Boat");
		
	}

	@EventHandler // used in 1.6.2
	public void postInit(FMLPostInitializationEvent event) {
		// Stub Method
	}

	public void registerEntity(Class<? extends Entity> entityClass, String entityName) {
		int id = EntityRegistry.findGlobalUniqueEntityId();
		
		EntityRegistry.registerModEntity(entityClass, entityName, id, this, 80, 1, true);//Have to use this instead of commented out ones to make boat render
		//EntityRegistry.registerGlobalEntityID(entityClass, entityName, id);
		//EntityList.entityEggs.put(Integer.valueOf(id), new EntityEggInfo(id));
	}
}
