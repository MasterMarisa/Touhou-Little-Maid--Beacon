package com.mastermarisa.maidbeacon.network;


import com.mastermarisa.maidbeacon.MaidBeacon;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record EffectButtonClickPayload(int actionCode, int entityId, String effectID) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<EffectButtonClickPayload> TYPE =
            new CustomPacketPayload.Type<>(MaidBeacon.resourceLocation("effect_button_clicked"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, EffectButtonClickPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT,
                    EffectButtonClickPayload::actionCode,
                    ByteBufCodecs.INT,
                    EffectButtonClickPayload::entityId,
                    ByteBufCodecs.STRING_UTF8,
                    EffectButtonClickPayload::effectID,
                    EffectButtonClickPayload::new
            );
}
