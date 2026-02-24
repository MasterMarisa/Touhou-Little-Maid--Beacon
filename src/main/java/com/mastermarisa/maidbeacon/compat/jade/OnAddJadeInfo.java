package com.mastermarisa.maidbeacon.compat.jade;

import com.github.tartaricacid.touhoulittlemaid.api.event.AddJadeInfoEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mastermarisa.maidbeacon.config.Config;
import com.mastermarisa.maidbeacon.data.BeaconData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import snownee.jade.api.ITooltip;

public class OnAddJadeInfo {
    @SubscribeEvent
    public static void onAddJadeInfoEvent(AddJadeInfoEvent event){
        EntityMaid maid = event.getMaid();
        ITooltip tooltip = event.getTooltip();
        if (!maid.getArmorInvWrapper().getStackInSlot(3).is(Items.BEACON)) return;
        BeaconData beaconData = maid.getData(BeaconData.TYPE);
        tooltip.add(Component.translatable("jade.maidbeacon.tooltip.beacon_level", beaconData.level).withStyle(ChatFormatting.AQUA));
        if (beaconData.level >= Config.MAX_LEVEL()) return;
        ItemStack required = Config.ITEMSTACK_TO_UPGRADE_BEACON_LEVEL().get(beaconData.level);
        tooltip.add(Component.translatable(
                "jade.maidbeacon.tooltip.itemstack_to_upgrade",
                required.getHoverName().getString() + " x" + required.getCount()
        ).withStyle(ChatFormatting.AQUA));
    }
}
