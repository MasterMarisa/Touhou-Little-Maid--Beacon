package com.mastermarisa.maidbeacon.compat.jade;

import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;

public class JadeCompat {
    public static final boolean LOADED = ModList.get().isLoaded("jade");

    public static void register() {
        if (LOADED) {
            NeoForge.EVENT_BUS.register(OnAddJadeInfo.class);
        }
    }
}
