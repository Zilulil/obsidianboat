package com.zilulil.obsidianboat.client;

import com.zilulil.obsidianboat.CommonProxy;
import com.zilulil.obsidianboat.client.renderer.entity.RenderObsidianBoat;
import com.zilulil.obsidianboat.entity.EntityObsidianBoat;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
        
        @Override
        public void registerRenderers() {
                // This is for rendering entities and so forth later on
        	RenderingRegistry.registerEntityRenderingHandler(EntityObsidianBoat.class, new RenderObsidianBoat());
        }
        
}
