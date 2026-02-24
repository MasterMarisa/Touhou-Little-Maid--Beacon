package com.mastermarisa.maidbeacon;

import com.mastermarisa.maidbeacon.config.Config;
import com.mastermarisa.maidbeacon.event.OnRenderAuraOutline;
import com.mastermarisa.maidbeacon.init.ModCompats;
import com.mastermarisa.maidbeacon.init.ModEntities;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;

@Mod(MaidBeacon.MOD_ID)
public class MaidBeacon {
    public static final String MOD_ID = "maidbeacon";

    public static ResourceLocation resourceLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public MaidBeacon(IEventBus modEventBus, ModContainer modContainer) {
        ModEntities.register(modEventBus);
        Config.register(modContainer);
        ModCompats.register();
    }
}
