package com.mastermarisa.maidbeacon.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mastermarisa.maidbeacon.MaidBeacon;
import com.mastermarisa.maidbeacon.logic.MovingBeacon;
import com.mastermarisa.maidbeacon.ui.BeaconScreen;
import com.mastermarisa.maidbeacon.utils.MovingBeaconHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = MaidBeacon.MOD_ID,value = Dist.CLIENT)
public class OnPlayerOpenBeaconScreenEvent {
    @SubscribeEvent
    public static void onPlayerOpenBeaconScreenEvent(PlayerInteractEvent.EntityInteract event){
        if(!event.getLevel().isClientSide()){
            return;
        }
        Player player = event.getEntity();
        Entity entity = event.getTarget();
        if(entity instanceof EntityMaid maid){
            if (player.isSecondaryUseActive() && player.getMainHandItem().is(Items.NETHER_STAR) && MovingBeaconHelper.getBeaconLevel(maid) >= 0){
                BeaconScreen.open(player,maid);
            }
        }
    }
}
