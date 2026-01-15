package com.mastermarisa.maidbeacon.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

public class OnMaidJoinWorldEvent {
    @SubscribeEvent
    public static void onMaidJoinWorld(EntityJoinLevelEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof EntityMaid maid){

        }
    }
}
