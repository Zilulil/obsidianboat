package zilulil.obsidianboat.client;

import zilulil.obsidianboat.CommonProxy;
import zilulil.obsidianboat.client.renderer.entity.RenderObsidianBoat;
import zilulil.obsidianboat.entity.EntityObsidianBoat;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
        
        @Override
        public void registerRenderers() {
                // This is for rendering entities and so forth later on
        	RenderingRegistry.registerEntityRenderingHandler(EntityObsidianBoat.class, new RenderObsidianBoat());
        }
        
}
