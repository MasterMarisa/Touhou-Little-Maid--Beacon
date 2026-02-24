package com.mastermarisa.maidbeacon.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mastermarisa.maidbeacon.MaidBeacon;
import com.mastermarisa.maidbeacon.config.Config;
import com.mastermarisa.maidbeacon.data.BeaconData;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.items.wrapper.EntityArmorInvWrapper;

@EventBusSubscriber(modid = MaidBeacon.MOD_ID)
public class OnInteractMaid {
    @SubscribeEvent
    public static void onPlayerInteractMaidEvent(PlayerInteractEvent.EntityInteract event){
        if (event.getLevel().isClientSide()) return;

        if (event.getTarget() instanceof EntityMaid maid){
            EntityArmorInvWrapper wrapper = maid.getArmorInvWrapper();
            ItemStack itemStack = event.getItemStack();
            if (wrapper.getStackInSlot(3).is(Items.BEACON)) {
                BeaconData beaconData = maid.getData(BeaconData.TYPE);
                if (beaconData.level >= Config.MAX_LEVEL()) return;
                ItemStack required = Config.ITEMSTACK_TO_UPGRADE_BEACON_LEVEL().get(beaconData.level);
                if (ItemStack.isSameItem(required, itemStack) && itemStack.getCount() >= required.getCount()) {
                    beaconData.level++;
                    itemStack.split(required.getCount());
                    maid.setData(BeaconData.TYPE, beaconData);
                    event.setCanceled(true);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                }
            } else if (wrapper.getStackInSlot(3).isEmpty() && itemStack.is(Items.BEACON)) {
                wrapper.setStackInSlot(3, itemStack.split(1));
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }
}
