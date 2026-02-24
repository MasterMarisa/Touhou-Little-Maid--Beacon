package com.mastermarisa.maidbeacon.network;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mastermarisa.maidbeacon.MaidBeacon;
import com.mastermarisa.maidbeacon.config.Config;
import com.mastermarisa.maidbeacon.data.BeaconData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.UUID;

@EventBusSubscriber(modid = MaidBeacon.MOD_ID)
public class NetworkHandler {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0");

        registrar.playToServer(
                ChangeAuraStatePayload.TYPE,
                ChangeAuraStatePayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        handleChangeAuraStateOnServer(payload, context);
                    });
                }
        );
    }

    private static void handleChangeAuraStateOnServer(ChangeAuraStatePayload payload, IPayloadContext context) {
        ServerLevel level = (ServerLevel) context.player().level();
        if (level.getEntity(payload.uuid()) instanceof EntityMaid maid) {
            BeaconData beaconData = maid.getData(BeaconData.TYPE);
            switch (payload.actionCode()) {
                case 0 -> beaconData.activated.remove(payload.index());
                case 1 -> {
                    if (beaconData.getUsedCost() + Config.EFFECT_AURAS().get(payload.index()).cost <= Config.getCost(beaconData.level))
                        beaconData.activated.add(payload.index());
                }
            }
            maid.setData(BeaconData.TYPE, beaconData);
        }
    }

    public static final StreamCodec<FriendlyByteBuf, UUID> UUID_STREAM_CODEC = StreamCodec.of(
            (buf, uuid) -> buf.writeUUID(uuid),
            buf -> buf.readUUID()
    );
}
