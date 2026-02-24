package com.mastermarisa.maidbeacon.event;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTickEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mastermarisa.maidbeacon.MaidBeacon;
import com.mastermarisa.maidbeacon.config.Config;
import com.mastermarisa.maidbeacon.data.BeaconData;
import com.mastermarisa.maidbeacon.utils.EffectAuraManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = MaidBeacon.MOD_ID)
public class OnRenderAuraOutline {
    @SubscribeEvent
    public static void onMaidTickEvent(MaidTickEvent event) {
        if (!Config.ENABLE_BEACON_RANGE_RENDERING()) return;
        EntityMaid maid = event.getMaid();
        Level level = maid.level();
        if (level.isClientSide()) return;
        if (!maid.getArmorInvWrapper().getStackInSlot(3).is(Items.BEACON)) return;
        if (level.getGameTime() % 5 != 0) return;
        BeaconData beaconData = maid.getData(BeaconData.TYPE);
        double t = Math.toIntExact(level.getGameTime()) % 90D;
        for (float range : beaconData.batched().keySet()) {
            AABB aabb = maid.getBoundingBox().inflate(range);
            EffectAuraManager.drawAABBHaloYHorizontal((ServerLevel) level, aabb, maid.getY() + maid.getBbHeight(), t, 0.5F);
        }
    }
}
