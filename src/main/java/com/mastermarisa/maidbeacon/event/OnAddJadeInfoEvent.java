package com.mastermarisa.maidbeacon.event;

import com.github.tartaricacid.touhoulittlemaid.api.event.AddJadeInfoEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mastermarisa.maidbeacon.config.ModConfig;
import com.mastermarisa.maidbeacon.utils.MovingBeaconHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import snownee.jade.api.ITooltip;

public class OnAddJadeInfoEvent {
    public static final boolean JADE_LOADED = ModList.get().isLoaded("jade");

    @SubscribeEvent
    public static void onAddJadeInfoEvent(AddJadeInfoEvent event){
        EntityMaid maid = event.getMaid();
        ITooltip tooltip = event.getTooltip();
        int beaconLevel = MovingBeaconHelper.getBeaconLevel(maid);
        if (beaconLevel >= 0){
            tooltip.add(Component.translatable("tooltip.maidbeacon.jade_maid.beacon_level",beaconLevel).withStyle(ChatFormatting.AQUA));
        }
        if (beaconLevel < ModConfig.BEACON_COST.get().size()){
            ItemStack stack;
            if(beaconLevel == -1){
                stack = new ItemStack(Items.BEACON);
            } else {
                stack = ModConfig.getItemStackByLevel(beaconLevel);
            }
            if (stack == null) return;
            tooltip.add(Component.translatable("tooltip.maidbeacon.jade_maid.itemstack_to_upgrade",Component.literal(String.valueOf(stack.getCount()) + " ").append(stack.getHoverName())).withStyle(ChatFormatting.AQUA));
        }
    }
}
