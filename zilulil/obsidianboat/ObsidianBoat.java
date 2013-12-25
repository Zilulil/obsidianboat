package zilulil.obsidianboat;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import zilulil.obsidianboat.entity.EntityObsidianBoat;
import zilulil.obsidianboat.items.ItemObsidianBoat;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid=OBInfo.ID,name=OBInfo.NAME,version=OBInfo.VERS)
@NetworkMod(clientSideRequired=true,serverSideRequired=false)
public class ObsidianBoat {
	
	
	// The instance of your mod that Forge uses.
	@Instance(value = OBInfo.ID)
	public static ObsidianBoat instance;
		
	@SidedProxy(clientSide=OBInfo.CLIENT_PROXY + "ClientProxy", serverSide=OBInfo.COMMON_PROXY + "CommonProxy")
	public static CommonProxy proxy;
	
	//Telling forge that we are creating these
	//items
	public static Item obsidianBoat;
	public static int obsidianBoatID;
	public static boolean useDiamond;
	//blocks

	//tools

	// Says where the client and server 'proxy' code is loaded.
	

	@EventHandler // used in 1.6.2
	//@PreInit    // used in 1.5.2
	public void preInit(FMLPreInitializationEvent event) {
		// Stub Method
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		obsidianBoatID = config.get(Configuration.CATEGORY_ITEM, "obsidianBoat", 2100).getInt();
		useDiamond = config.get(Configuration.CATEGORY_GENERAL, "useDiamondInBoat", false, "Enable more expensive crafting recipe that requires a diamond in the bottom middle slot").getBoolean(false);
		config.save();
	}


	@EventHandler
	public void load(FMLInitializationEvent event){
		// define items
		obsidianBoat = new ItemObsidianBoat(obsidianBoatID).setUnlocalizedName("obsidianBoat").setTextureName("obsidianboat:obsidianBoat");
		// define blocks
		
		//adding names
		LanguageRegistry.addName(obsidianBoat, "Obsidian Boat");
		
		//items

		//blocks
		//crafting
		if(!useDiamond){
			GameRegistry.addRecipe(new ItemStack(obsidianBoat, 1), new Object[]{
				"   ","T T","TTT",'T',Block.obsidian,
			});
		}
		else{
			GameRegistry.addRecipe(new ItemStack(obsidianBoat, 1), new Object[]{
				"   ","T T","TDT", 'T',Block.obsidian, 'D',Item.diamond,
			});
		}
		
		proxy.registerRenderers();
		
		int id = 0;
		EntityRegistry.registerModEntity(EntityObsidianBoat.class, "ObsidianBoat", id++, this, 80, 1, true);
		LanguageRegistry.instance().addStringLocalization("entity.EntityObsidianBoat.name", "Obsidian Boat");
		
	}

	@EventHandler // used in 1.6.2
	//@PostInit   // used in 1.5.2
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
