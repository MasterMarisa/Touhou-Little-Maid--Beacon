package com.mastermarisa.maidbeacon.init;

import com.mastermarisa.maidbeacon.data.BeaconData;
import com.mastermarisa.maidbeacon.MaidBeacon;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModEntities {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MaidBeacon.MOD_ID);

    public static final Supplier<AttachmentType<BeaconData>> BEACON_DATA = ATTACHMENT_TYPES.register("beacon_data", () -> BeaconData.TYPE);

    public static void register(IEventBus mod) {
        ATTACHMENT_TYPES.register(mod);
    }
}
