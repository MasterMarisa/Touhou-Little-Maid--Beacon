package com.mastermarisa.maidbeacon.config;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mastermarisa.maidbeacon.data.EffectAura;
import com.mastermarisa.maidbeacon.utils.EffectAuraManager;
import com.mastermarisa.maidbeacon.utils.EncodeUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Config {
    private static List<EffectAura> EFFECT_AURAS;
    private static ConcurrentHashMap<Integer, List<EffectAura>> MAP_BY_LEVEL;
    private static List<ItemStack> ITEMSTACK_TO_UPGRADE_BEACON_LEVEL;

    public static Integer CHECK_INTERVAL() {
        return Server.EFFECT_CHECK_INTERVAL.getAsInt();
    }

    public static List<? extends String> WHITELIST() {
        return Server.TARGET_WHITELIST.get();
    }

    public static List<EffectAura> EFFECT_AURAS() {
        if (EFFECT_AURAS == null) EFFECT_AURAS = Server.EFFECT_AURAS.get().stream().map(s -> EncodeUtils.fromJson(s, EffectAura.class)).toList();
        return EFFECT_AURAS;
    }

    public static List<ItemStack> ITEMSTACK_TO_UPGRADE_BEACON_LEVEL() {
        if (ITEMSTACK_TO_UPGRADE_BEACON_LEVEL == null)
            ITEMSTACK_TO_UPGRADE_BEACON_LEVEL = Server.ITEMSTACK_TO_UPGRADE_BEACON_LEVEL.get().stream()
                    .map(j -> EncodeUtils.fromJson(j, Server.ItemStackData.class))
                    .map(d -> new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse(d.id)), d.count)).toList();

        return ITEMSTACK_TO_UPGRADE_BEACON_LEVEL;
    }

    public static int MAX_LEVEL() {
        return EFFECT_AURAS().stream().map(e -> e.beaconLevel).max(Comparator.comparingInt(Integer::intValue)).orElse(0);
    }

    public static List<EffectAura> BY_LEVEL(int level) {
        if (MAP_BY_LEVEL == null) {
            MAP_BY_LEVEL = new ConcurrentHashMap<>();
            for (EffectAura aura : EFFECT_AURAS()) {
                MAP_BY_LEVEL.computeIfAbsent(aura.beaconLevel, (i) -> new ArrayList<>());
                MAP_BY_LEVEL.get(aura.beaconLevel).add(aura);
            }
        }
        return MAP_BY_LEVEL.getOrDefault(level, null);
    }

    public static Integer getCost(int level) {
        List<? extends Integer> costs = Server.BEACON_COST.get();
        if (level >= costs.size()) return costs.getLast();
        return costs.get(level);
    }

    public static boolean ENABLE_BEACON_RENDERING() {
        return Client.ENABLE_BEACON_RENDERING.getAsBoolean();
    }

    public static boolean ENABLE_BEACON_BEAM_RENDERING() {
        return Client.ENABLE_BEACON_BEAM_RENDERING.getAsBoolean();
    }

    public static double Y_OFFSET() {
        return Client.BEACON_RENDERING_Y_OFFSET.getAsDouble();
    }

    public static double Y_OFFSET_SITTING() {
        return Client.BEACON_RENDERING_Y_OFFSET_SITTING.getAsDouble();
    }

    public static boolean ENABLE_BEACON_RANGE_RENDERING() { return Server.ENABLE_BEACON_RANGE_RENDERING.getAsBoolean(); }

    private static class Client {
        public static final ModConfigSpec.Builder BUILDER;
        public static final ModConfigSpec SPEC;
        public static final ModConfigSpec.BooleanValue ENABLE_BEACON_RENDERING;
        public static final ModConfigSpec.BooleanValue ENABLE_BEACON_BEAM_RENDERING;
        public static final ModConfigSpec.DoubleValue BEACON_RENDERING_Y_OFFSET;
        public static final ModConfigSpec.DoubleValue BEACON_RENDERING_Y_OFFSET_SITTING;

        public static void register(ModContainer modContainer) {
            modContainer.registerConfig(ModConfig.Type.CLIENT, SPEC);
        }

        static {
            BUILDER = new ModConfigSpec.Builder();

            ENABLE_BEACON_RENDERING = BUILDER
                    .translation("config.maidbeacon.client.enable_beacon_rendering")
                    .define("enable_beacon_rendering", true);

            ENABLE_BEACON_BEAM_RENDERING = BUILDER
                    .translation("config.maidbeacon.client.enable_beacon_beam_rendering")
                    .define("enable_beacon_beam_rendering", true);

            BEACON_RENDERING_Y_OFFSET = BUILDER
                    .translation("config.maidbeacon.client.beacon_rendering_y_offset")
                    .defineInRange("beacon_rendering_y_offset", 0.5F, -1000, Double.MAX_VALUE);

            BEACON_RENDERING_Y_OFFSET_SITTING = BUILDER
                    .translation("config.maidbeacon.beacon_rendering_y_offset_sitting")
                    .defineInRange("beacon_rendering_y_offset_sitting", -0.1F, -1000, Double.MAX_VALUE);

            SPEC = BUILDER.build();
        }
    }

    private static class Server {
        public static final ModConfigSpec.Builder BUILDER;
        public static final ModConfigSpec SPEC;
        public static final ModConfigSpec.ConfigValue<List<? extends String>> EFFECT_AURAS;
        public static final ModConfigSpec.ConfigValue<List<? extends Integer>> BEACON_COST;
        public static final ModConfigSpec.ConfigValue<List<? extends String>> TARGET_WHITELIST;
        public static final ModConfigSpec.ConfigValue<List<? extends String>> ITEMSTACK_TO_UPGRADE_BEACON_LEVEL;
        public static final ModConfigSpec.IntValue EFFECT_CHECK_INTERVAL;
        public static final ModConfigSpec.BooleanValue ENABLE_BEACON_RANGE_RENDERING;

        public static void register(ModContainer modContainer) {
            modContainer.registerConfig(ModConfig.Type.SERVER, SPEC);
        }

        static {
            BUILDER = new ModConfigSpec.Builder();

            EFFECT_AURAS = BUILDER
                    .translation("config.maidbeacon.server.effect_auras")
                    .defineList("effect_auras", EffectAuraManager.defaultData, ()-> "", (e)-> e instanceof String);

            BEACON_COST = BUILDER
                    .translation("config.maidbeacon.server.beacon_cost")
                    .defineList("beacon_cost", List.of(1,2,3,4), ()-> 0, (e)-> e instanceof Integer);

            TARGET_WHITELIST = BUILDER
                    .translation("config.maidbeacon.server.whitelist")
                    .defineList("target_whitelist", List.of(
                            BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.PLAYER).toString(),
                            "touhou_little_maid:maid"
                    ), () -> "", (s) -> s instanceof String);

            ITEMSTACK_TO_UPGRADE_BEACON_LEVEL = BUILDER
                    .translation("config.maidbeacon.server.itemstack")
                    .defineList("itemstack_to_upgrade_beacon_level", List.of(
                            EncodeUtils.toJson(new ItemStackData("minecraft:iron_block", 9)),
                            EncodeUtils.toJson(new ItemStackData("minecraft:gold_block", 9)),
                            EncodeUtils.toJson(new ItemStackData("minecraft:diamond_block", 9))
                    ), ()-> "", (e)-> e instanceof String);

            EFFECT_CHECK_INTERVAL = BUILDER
                    .translation("config.maidbeacon.server.effect_check_interval")
                    .defineInRange("effect_check_interval", 20, 1, Integer.MAX_VALUE);

            ENABLE_BEACON_RANGE_RENDERING = BUILDER
                    .translation("config.maidbeacon.server.enable_beacon_range_rendering")
                    .define("enable_beacon_range_rendering", false);

            SPEC = BUILDER.build();
        }

        public static class ItemStackData {
            public String id;
            public int count;

            public ItemStackData(String id, int count) {
                this.id = id;
                this.count = count;
            }
        }
    }

    public static void register(ModContainer modContainer) {
        Client.register(modContainer);
        Server.register(modContainer);
    }
}
