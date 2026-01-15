package com.mastermarisa.maidbeacon.data;

import com.mastermarisa.maidbeacon.logic.EffectAura;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public class EffectAuraData {
    public static final List<EffectAuraData> defaultData;

    static {
        defaultData = new ArrayList<>();
        defaultData.add(new EffectAuraData("minecraft:speed",0,1,0,20));
        defaultData.add(new EffectAuraData("minecraft:haste",0,1,0,20));
        defaultData.add(new EffectAuraData("minecraft:resistance",0,1,1,20));
        defaultData.add(new EffectAuraData("minecraft:jump_boost",0,1,1,20));
        defaultData.add(new EffectAuraData("minecraft:strength",0,1,2,20));
        defaultData.add(new EffectAuraData("minecraft:regeneration",0,2,3,20));
    }

    public String registerName;
    public int effectLevel;
    public int cost;
    public int requiredBeaconLevel;
    public float range;

    public EffectAuraData(String registerName,int effectLevel,int cost,int requiredBeaconLevel,float range){
        this.registerName = registerName;
        this.effectLevel = effectLevel;
        this.cost = cost;
        this.requiredBeaconLevel = requiredBeaconLevel;
        this.range = range;
    }

    public static final Codec<EffectAuraData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("registerName").forGetter(s -> s.registerName),
                    Codec.INT.fieldOf("effectLevel").forGetter(s -> s.effectLevel),
                    Codec.INT.fieldOf("cost").forGetter(s -> s.cost),
                    Codec.INT.fieldOf("requiredBeaconLevel").forGetter(s -> s.requiredBeaconLevel),
                    Codec.FLOAT.fieldOf("range").forGetter(s -> s.range)
            ).apply(instance,EffectAuraData::new));
}
