package com.mastermarisa.maidbeacon.data;

import com.mastermarisa.maidbeacon.config.Config;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BeaconData implements INBTSerializable<CompoundTag> {
    public int level = 0;
    public IntSet activated = new IntArraySet();

    public int getUsedCost() {
        int total = 0;
        for (Integer i : activated) {
            if (i >= Config.EFFECT_AURAS().size()) continue;
            total += Config.EFFECT_AURAS().get(i).cost;
        }
        return total;
    }

    public ConcurrentHashMap<Float, List<EffectAura>> batched() {
        ConcurrentHashMap<Float, List<EffectAura>> map = new ConcurrentHashMap<>();
        for (int i : activated) {
            if (i >= Config.EFFECT_AURAS().size()) continue;
            EffectAura aura = Config.EFFECT_AURAS().get(i);
            map.computeIfAbsent(aura.range, (f) -> new ArrayList<>(2));
            map.get(aura.range).add(aura);
        }
        return map;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("level", level);
        tag.putIntArray("activated", activated.toIntArray());
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        level = tag.getInt("level");
        activated = new IntArraySet(tag.getIntArray("activated"));
    }

    public static class Syncer implements AttachmentSyncHandler<BeaconData> {
        @Override
        public void write(RegistryFriendlyByteBuf registryFriendlyByteBuf, BeaconData beaconData, boolean b) {
            registryFriendlyByteBuf.writeInt(beaconData.level);
            registryFriendlyByteBuf.writeCollection(beaconData.activated, FriendlyByteBuf::writeInt);
        }

        @Override
        public @Nullable BeaconData read(IAttachmentHolder iAttachmentHolder, RegistryFriendlyByteBuf registryFriendlyByteBuf, @Nullable BeaconData beaconData) {
            if (beaconData == null) beaconData = new BeaconData();
            beaconData.level = registryFriendlyByteBuf.readInt();
            beaconData.activated = registryFriendlyByteBuf.readCollection(IntArraySet::new, FriendlyByteBuf::readInt);
            return beaconData;
        }
    }

    public static final AttachmentType<BeaconData> TYPE = AttachmentType.serializable(BeaconData::new).sync(new Syncer()).build();
}
