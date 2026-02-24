package com.mastermarisa.maidbeacon.data;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class EffectAura {
    public String effect;
    public int effectLevel;
    public int beaconLevel;
    public int cost;
    public float range;

    public EffectAura(String effect, int effectLevel, int beaconLevel, int cost, float range) {
        this.effect = effect;
        this.effectLevel = effectLevel;
        this.beaconLevel = beaconLevel;
        this.cost = cost;
        this.range = range;
    }

    public Holder<MobEffect> getHolder() {
        MobEffect mobEffect = Objects.requireNonNull(BuiltInRegistries.MOB_EFFECT.get(ResourceLocation.tryParse(effect)));
        return BuiltInRegistries.MOB_EFFECT.wrapAsHolder(mobEffect);
    }

    public ResourceLocation getIcon(){
        ResourceLocation effectId = ResourceLocation.parse(getHolder().getRegisteredName());
        return ResourceLocation.fromNamespaceAndPath(effectId.getNamespace(), "textures/mob_effect/" + effectId.getPath() + ".png");
    }
}
