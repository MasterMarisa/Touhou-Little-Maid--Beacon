package com.mastermarisa.maidbeacon.logic;

import com.mastermarisa.maidbeacon.config.ModConfig;
import com.mastermarisa.maidbeacon.utils.ModUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
public class MovingBeacon implements INBTSerializable<CompoundTag> {
    public int beaconLevel = -1;
    private Set<String> activatedEffectIDs = new HashSet<>();

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("beaconLevel",this.beaconLevel);
        ModUtils.writeStringSet(tag,"activatedEffectIDs",this.activatedEffectIDs);

        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag) {
        this.beaconLevel = compoundTag.getInt("beaconLevel");
        this.activatedEffectIDs = ModUtils.readStringSet(compoundTag,"activatedEffectIDs");
    }

    public Set<String> getActivatedEffectIDs(){
        if (this.activatedEffectIDs == null){
            this.activatedEffectIDs = new HashSet<>();
        }

        return this.activatedEffectIDs;
    }

    public void setActivatedEffectIDs(Set<String> set){
        this.activatedEffectIDs = set;
    }

    public List<EffectAura> getEffectAuras(){
        List<EffectAura> auras = new ArrayList<>();
        for (String ID : getActivatedEffectIDs()){
            if(ModConfig.getEffectAuraByID(ID) != null){
                auras.add(ModConfig.getEffectAuraByID(ID));
            }
        }

        return auras;
    }

    public void upgradeBeaconLevel(){
        this.beaconLevel++;
    }

    public int getTotalCost(){
        List<EffectAura> auras = getEffectAuras();
        int cost = 0;
        if (auras.isEmpty()) return 0;
        for (EffectAura aura : auras){
            cost += aura.cost;
        }
        return cost;
    }
}
