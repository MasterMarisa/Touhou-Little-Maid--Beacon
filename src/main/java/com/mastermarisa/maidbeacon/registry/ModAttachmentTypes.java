package com.mastermarisa.maidbeacon.registry;

import com.mastermarisa.maidbeacon.MaidBeacon;
import com.mastermarisa.maidbeacon.logic.MovingBeacon;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MaidBeacon.MOD_ID);

    public static final Supplier<AttachmentType<MovingBeacon>> MOVING_BEACON = ATTACHMENT_TYPES.register("movingbeacon",()-> AttachmentType.serializable(MovingBeacon::new).sync(new MovingBeaconSyncHandler()).build());
}
