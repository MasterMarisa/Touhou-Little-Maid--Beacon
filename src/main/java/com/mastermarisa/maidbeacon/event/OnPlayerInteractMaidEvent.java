package com.mastermarisa.maidbeacon.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mastermarisa.maidbeacon.config.ModConfig;
import com.mastermarisa.maidbeacon.utils.MovingBeaconHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class OnPlayerInteractMaidEvent {
    @SubscribeEvent
    public static void onPlayerInteractMaidEvent(PlayerInteractEvent.EntityInteract event){
        Player player = event.getEntity();
        Entity entity = event.getTarget();
        if(entity instanceof EntityMaid maid){
            int beaconLevel = MovingBeaconHelper.getBeaconLevel(maid);
            if (beaconLevel == -1){
                if (player.isSecondaryUseActive() && player.getMainHandItem().is(Items.BEACON)){
                    MovingBeaconHelper.upgradeBeaconLevel(maid);
                    player.getMainHandItem().split(1);
                }
            } else {
                ItemStack stack = ModConfig.getItemStackByLevel(beaconLevel);
                if (stack == null) return;
                if (player.isSecondaryUseActive() && player.getMainHandItem().is(stack.getItem()) && player.getMainHandItem().getCount() >= stack.getCount()){
                    MovingBeaconHelper.upgradeBeaconLevel(maid);
                    player.getMainHandItem().split(stack.getCount());
                }
            }
        }
    }
}
