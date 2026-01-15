package com.mastermarisa.maidbeacon.config;

import com.mastermarisa.maidbeacon.data.EffectAuraData;
import com.mastermarisa.maidbeacon.data.ItemStackData;
import com.mastermarisa.maidbeacon.logic.EffectAura;
import com.mastermarisa.maidbeacon.logic.MovingBeacon;
import com.mastermarisa.maidbeacon.utils.ModUtils;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ModConfig {
    private static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<List<? extends String>> EFFECT_AURAS = SERVER_BUILDER
            .translation("config.maidbeacon.server.effect_auras")
            .defineList("effectAuras",EffectAuraData.defaultData.stream().map(ModUtils::toJson).toList(),()-> "",(e)-> e instanceof String);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> ITEMSTACK_TO_UPGRADE_BEACON_LEVEL = SERVER_BUILDER
            .translation("config.maidbeacon.server.itemstack")
            .defineList("itemStackToUpgradeBeaconLevel", ItemStackData.defaultData.stream().map(ModUtils::toJson).toList(),()-> "",(e)-> e instanceof String);

    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> BEACON_COST = SERVER_BUILDER
            .translation("config.maidbeacon.server.beacon_cost")
            .defineList("beaconCost",List.of(1,2,3,4),()-> 0,(e)-> e instanceof Integer);

    public static final ModConfigSpec SERVER_SPEC = SERVER_BUILDER.build();

    private static List<? extends Integer> beaconCostList;

    private static Hashtable<Integer,List<EffectAura>> effectAurasDic;

    private static Hashtable<String,EffectAura> effectAurasIDDIc;

    private static Hashtable<Integer,ItemStack> itemStacksDic;

    public static int getCostByLevel(int beaconLevel){
        if(beaconCostList == null){
            beaconCostList = BEACON_COST.get();
        }

        if(beaconLevel >= beaconCostList.size()){
            return beaconCostList.getLast();
        }
        return beaconCostList.get(beaconLevel);
    }

    public static List<EffectAura> getEffectAurasByLevel(int level){
        if(effectAurasDic == null){
            effectAurasDic = new Hashtable<>();
            effectAurasIDDIc = new Hashtable<>();
            List<EffectAura> auras = new ArrayList<>();
            for (String json : EFFECT_AURAS.get()){
                EffectAuraData data = ModUtils.toObject(json,EffectAuraData.class);
                EffectAura effectAura = new EffectAura(data);
                effectAurasIDDIc.put(data.registerName,effectAura);
                auras.add(effectAura);
            }
            for (EffectAura aura : auras){
                if (!effectAurasDic.containsKey(aura.requiredBeaconLevel)){
                    effectAurasDic.put(aura.requiredBeaconLevel,new ArrayList<>());
                }
                effectAurasDic.get(aura.requiredBeaconLevel).add(aura);
            }
        }

        return effectAurasDic.get(level);
    }

    public static EffectAura getEffectAuraByID(String ID){
        if(effectAurasDic == null){
            effectAurasDic = new Hashtable<>();
            effectAurasIDDIc = new Hashtable<>();
            List<EffectAura> auras = new ArrayList<>();
            for (String json : EFFECT_AURAS.get()){
                EffectAuraData data = ModUtils.toObject(json,EffectAuraData.class);
                EffectAura effectAura = new EffectAura(data);
                effectAurasIDDIc.put(data.registerName,effectAura);
                auras.add(effectAura);
            }
            for (EffectAura aura : auras){
                if (!effectAurasDic.containsKey(aura.requiredBeaconLevel)){
                    effectAurasDic.put(aura.requiredBeaconLevel,new ArrayList<>());
                }
                effectAurasDic.get(aura.requiredBeaconLevel).add(aura);
            }
        }

        if(!effectAurasIDDIc.containsKey(ID)){
            return null;
        }
        return effectAurasIDDIc.get(ID);
    }

    public static ItemStack getItemStackByLevel(int level){
        if(itemStacksDic == null){
            itemStacksDic = new Hashtable<>();
            List<? extends String> jsons = ITEMSTACK_TO_UPGRADE_BEACON_LEVEL.get();
            for (int i = 0;i < jsons.size();i++){
                itemStacksDic.put(i,ModUtils.toObject(jsons.get(i),ItemStackData.class).toItemStack());
            }
        }

        return itemStacksDic.get(level);
    }
}
