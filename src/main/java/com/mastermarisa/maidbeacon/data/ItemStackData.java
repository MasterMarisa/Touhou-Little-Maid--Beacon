package com.mastermarisa.maidbeacon.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class ItemStackData {
    public static final List<ItemStackData> defaultData;

    static {
        defaultData = new ArrayList<>();
        defaultData.add(new ItemStackData(BuiltInRegistries.ITEM.getKey(Items.IRON_BLOCK).toString(),9));
        defaultData.add(new ItemStackData(BuiltInRegistries.ITEM.getKey(Items.GOLD_BLOCK).toString(),9));
        defaultData.add(new ItemStackData(BuiltInRegistries.ITEM.getKey(Items.DIAMOND_BLOCK).toString(),9));
    }

    public String registerName;
    public int count;

    public ItemStackData(String registerName,int count){
        this.registerName = registerName;
        this.count = count;
    }

    public ItemStack toItemStack(){
        return new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(registerName)),count);
    }
}
