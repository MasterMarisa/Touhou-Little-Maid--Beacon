package com.mastermarisa.maidbeacon.logic;

import com.mastermarisa.maidbeacon.data.EffectAuraData;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class EffectAura {
    public static final int effectCheckInterval = 20;

    public Holder<MobEffect> effect;
    public int effectLevel;
    public int cost;
    public int requiredBeaconLevel;
    public float range;

    public EffectAura(Holder<MobEffect> effect,int effectLevel,int cost,int requiredBeaconLevel,float range){
        this.effect = effect;
        this.effectLevel = effectLevel;
        this.cost = cost;
        this.requiredBeaconLevel = requiredBeaconLevel;
        this.range = range;
    }

    public EffectAura(EffectAuraData data){
        this.effectLevel = data.effectLevel;
        this.cost = data.cost;
        this.requiredBeaconLevel = data.requiredBeaconLevel;
        this.range = data.range;

        MobEffect mobEffect = BuiltInRegistries.MOB_EFFECT.get(ResourceLocation.tryParse(data.registerName));
        if(mobEffect != null){
            this.effect = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(mobEffect);
        }
    }

    public void applyAuraEffect(Entity entity){
        List<Player> players = entity.level().getEntitiesOfClass(Player.class,
                entity.getBoundingBox().inflate(range));

        for(Player player : players){
            player.addEffect(new MobEffectInstance(
                    effect,
                    effectCheckInterval + 10,
                    effectLevel,
                    false,
                    true
            ));
        }
    }

    public void applyAuraEffect(List<Player> players){
        for(Player player : players){
            player.addEffect(new MobEffectInstance(
                    effect,
                    effectCheckInterval + 10,
                    effectLevel,
                    false,
                    true
            ));
        }
    }
}
