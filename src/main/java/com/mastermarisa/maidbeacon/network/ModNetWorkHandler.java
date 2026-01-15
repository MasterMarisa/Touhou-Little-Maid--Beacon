package com.mastermarisa.maidbeacon.network;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mastermarisa.maidbeacon.utils.MovingBeaconHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;


public class ModNetWorkHandler {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0");

        registrar.playToServer(
                EffectButtonClickPayload.TYPE,
                EffectButtonClickPayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        handleEffectButtonClickOnServer(payload,context);
                    });
                }
        );
    }

    //0:disable  1:enable
    private static void handleEffectButtonClickOnServer(EffectButtonClickPayload payload, IPayloadContext context) {
        var serverPlayer = context.player();
        if (serverPlayer != null) {
            switch (payload.actionCode()) {
                case 0 -> {
                    disableEffectAura(payload,context);
                }
                case 1 -> {
                    enableEffectAura(payload,context);
//                    String msg = "服务端收到了你的点击！参数是: ";
//                    serverPlayer.sendSystemMessage(Component.literal(msg));
                }
                default -> serverPlayer.sendSystemMessage(Component.literal("Error:未知的操作码"));
            }
        }
    }

    private static void enableEffectAura(EffectButtonClickPayload payload, IPayloadContext context){
        Player serverPlayer = context.player();
        Level level = serverPlayer.level();
        Entity entity = level.getEntity(payload.entityId());

        if(entity instanceof EntityMaid maid){
            MovingBeaconHelper.enableEffectAura(maid,payload.effectID());
            //serverPlayer.sendSystemMessage(Component.literal("已启用" + payload.effectID()));
        }
    }

    private static void disableEffectAura(EffectButtonClickPayload payload, IPayloadContext context){
        Player serverPlayer = context.player();
        Level level = serverPlayer.level();
        Entity entity = level.getEntity(payload.entityId());

        if(entity instanceof EntityMaid maid){
            MovingBeaconHelper.disableEffectAura(maid,payload.effectID());
            //serverPlayer.sendSystemMessage(Component.literal("已禁用" + payload.effectID()));
        }
    }
}
