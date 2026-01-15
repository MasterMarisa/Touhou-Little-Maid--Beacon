package com.mastermarisa.maidbeacon.utils;

import com.mastermarisa.maidbeacon.config.ModConfig;
import com.mastermarisa.maidbeacon.logic.EffectAura;
import com.mastermarisa.maidbeacon.logic.MovingBeacon;
import com.mastermarisa.maidbeacon.registry.ModAttachmentTypes;
import net.minecraft.world.entity.Entity;

import java.util.List;
import java.util.Objects;

public class MovingBeaconHelper {
    public static void applyEffectAuras(Entity entity, List<EffectAura> auras){
        if(auras.isEmpty()) return;
        for(EffectAura aura : auras){
            aura.applyAuraEffect(entity);
        }
    }

    public static int getBeaconLevel(Entity entity){
        MovingBeacon movingBeacon = entity.getData(ModAttachmentTypes.MOVING_BEACON);
        return movingBeacon.beaconLevel;
    }

    public static void upgradeBeaconLevel(Entity entity){
        MovingBeacon movingBeacon = entity.getData(ModAttachmentTypes.MOVING_BEACON);
        movingBeacon.upgradeBeaconLevel();
    }

    public static void enableEffectAura(Entity entity,String effectId){
        MovingBeacon movingBeacon = entity.getData(ModAttachmentTypes.MOVING_BEACON);
        movingBeacon.getActivatedEffectIDs().add(effectId);
        entity.setData(ModAttachmentTypes.MOVING_BEACON,movingBeacon);
    }

    public static void disableEffectAura(Entity entity,String effectId){
        MovingBeacon movingBeacon = entity.getData(ModAttachmentTypes.MOVING_BEACON);
        movingBeacon.getActivatedEffectIDs().remove(effectId);
        entity.setData(ModAttachmentTypes.MOVING_BEACON,movingBeacon);
    }

    public static boolean isEffectActivated(Entity entity,String effectId){
        MovingBeacon movingBeacon = entity.getData(ModAttachmentTypes.MOVING_BEACON);
        return movingBeacon.getActivatedEffectIDs().contains(effectId);
    }
}
