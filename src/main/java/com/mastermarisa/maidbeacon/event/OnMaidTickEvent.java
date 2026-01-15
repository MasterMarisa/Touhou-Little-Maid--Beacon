package com.mastermarisa.maidbeacon.event;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTickEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mastermarisa.maidbeacon.logic.EffectAura;
import com.mastermarisa.maidbeacon.logic.MovingBeacon;
import com.mastermarisa.maidbeacon.registry.ModAttachmentTypes;
import com.mastermarisa.maidbeacon.utils.MovingBeaconHelper;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;

public class OnMaidTickEvent {
    @SubscribeEvent
    public static void onMaidTickEvent(MaidTickEvent event){
        EntityMaid maid = event.getMaid();
        Level level = maid.level();
        if(level.isClientSide() || level.getGameTime() % EffectAura.effectCheckInterval != 0) return;
        MovingBeacon movingBeacon = maid.getData(ModAttachmentTypes.MOVING_BEACON);
        MovingBeaconHelper.applyEffectAuras(maid,movingBeacon.getEffectAuras());
    }
}
