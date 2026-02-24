package com.mastermarisa.maidbeacon.network;

import com.mastermarisa.maidbeacon.MaidBeacon;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record ChangeAuraStatePayload(int actionCode, UUID uuid, int index) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ChangeAuraStatePayload> TYPE =
            new CustomPacketPayload.Type<>(MaidBeacon.resourceLocation("change_aura_state"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, ChangeAuraStatePayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT,
                    ChangeAuraStatePayload::actionCode,
                    NetworkHandler.UUID_STREAM_CODEC,
                    ChangeAuraStatePayload::uuid,
                    ByteBufCodecs.INT,
                    ChangeAuraStatePayload::index,
                    ChangeAuraStatePayload::new
            );
}
