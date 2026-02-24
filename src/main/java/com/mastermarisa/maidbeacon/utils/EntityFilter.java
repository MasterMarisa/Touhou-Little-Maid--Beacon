package com.mastermarisa.maidbeacon.utils;

import com.mastermarisa.maidbeacon.config.Config;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class EntityFilter {
    private static final List<EntityType<?>> whitelist;
    private static final List<TagKey<EntityType<?>>> tagWhitelist;

    public static boolean filter(EntityType<?> type) {
        return whitelist.contains(type) || tagWhitelist.stream().anyMatch(type::is);
    }

    private static void init() {
        for (String str : Config.WHITELIST()) {
            if (isTagLocation(str)) {
                ResourceLocation location = ResourceLocation.tryParse(str.substring(1));
                if (location == null) continue;
                tagWhitelist.add(TagKey.create(Registries.ENTITY_TYPE, location));
            } else {
                ResourceLocation location = ResourceLocation.tryParse(str);
                if (location == null) continue;
                whitelist.add(BuiltInRegistries.ENTITY_TYPE.get(location));
            }
        }
    }

    private static boolean isTagLocation(String location) {
        return location.startsWith("#");
    }

    static {
        whitelist = new ArrayList<>();
        tagWhitelist = new ArrayList<>();
        init();
    }
}
