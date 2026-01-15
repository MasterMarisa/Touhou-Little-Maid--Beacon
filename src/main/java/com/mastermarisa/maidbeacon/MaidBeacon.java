package com.mastermarisa.maidbeacon;

import com.mastermarisa.maidbeacon.config.ModConfig;
import com.mastermarisa.maidbeacon.event.*;
import com.mastermarisa.maidbeacon.network.ModNetWorkHandler;
import com.mastermarisa.maidbeacon.registry.ModAttachmentTypes;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(MaidBeacon.MOD_ID)
public class MaidBeacon {
    public static final String MOD_ID = "maidbeacon";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation resourceLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public MaidBeacon(IEventBus modEventBus, ModContainer modContainer, Dist dist) {
        ModAttachmentTypes.ATTACHMENT_TYPES.register(modEventBus);
        modEventBus.register(ModNetWorkHandler.class);

        NeoForge.EVENT_BUS.register(OnMaidJoinWorldEvent.class);
        NeoForge.EVENT_BUS.register(OnMaidTickEvent.class);
        NeoForge.EVENT_BUS.register(OnPlayerInteractMaidEvent.class);
        if (OnAddJadeInfoEvent.JADE_LOADED){
            NeoForge.EVENT_BUS.register(OnAddJadeInfoEvent.class);
        }

        modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.SERVER, ModConfig.SERVER_SPEC);
    }
}
