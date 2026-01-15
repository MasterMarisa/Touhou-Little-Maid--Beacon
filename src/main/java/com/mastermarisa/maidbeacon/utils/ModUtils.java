package com.mastermarisa.maidbeacon.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

import java.util.HashSet;
import java.util.Set;

public class ModUtils {
    private static final Gson GSON;

    static {
        GSON = new GsonBuilder().setPrettyPrinting().create();
    }

    public static <T> String toJson(T obj){
        return GSON.toJson(obj);
    }

    public static <T> T toObject(String json, Class<? extends T> type){
        return GSON.fromJson(json, type);
    }

    public static void writeStringSet(CompoundTag tag, String key, Set<String> stringSet) {
        ListTag listTag = new ListTag();
        for (String str : stringSet) {
            listTag.add(StringTag.valueOf(str));
        }
        tag.put(key, listTag);
    }

    public static Set<String> readStringSet(CompoundTag tag, String key) {
        Set<String> stringSet = new HashSet<>();
        if (tag.contains(key, Tag.TAG_LIST)) {
            ListTag listTag = tag.getList(key, Tag.TAG_STRING);
            for (int i = 0; i < listTag.size(); i++) {
                stringSet.add(listTag.getString(i));
            }
        }
        return stringSet;
    }

    public static ResourceLocation getPath(Holder<MobEffect> effect){
        ResourceLocation effectId = ResourceLocation.parse(effect.getRegisteredName());
        return ResourceLocation.fromNamespaceAndPath(effectId.getNamespace(), "textures/mob_effect/" + effectId.getPath() + ".png");
    }
}
