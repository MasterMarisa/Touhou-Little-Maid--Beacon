package com.mastermarisa.maidbeacon.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mastermarisa.maidbeacon.MaidBeacon;
import com.mastermarisa.maidbeacon.client.gui.screen.BeaconScreen;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.items.wrapper.EntityArmorInvWrapper;

@EventBusSubscriber(modid = MaidBeacon.MOD_ID)
public class OnOpenScreen {
    @SubscribeEvent
    public static void onPlayerInteractMaidEvent(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof EntityMaid maid){
            EntityArmorInvWrapper wrapper = maid.getArmorInvWrapper();
            ItemStack itemStack = event.getItemStack();
            if (wrapper.getStackInSlot(3).is(Items.BEACON) && itemStack.is(Items.END_CRYSTAL)) {
                if (event.getLevel().isClientSide()) BeaconScreen.open(maid);
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }
}
