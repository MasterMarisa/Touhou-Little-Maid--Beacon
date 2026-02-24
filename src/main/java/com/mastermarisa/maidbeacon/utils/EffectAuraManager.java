package com.mastermarisa.maidbeacon.utils;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTickEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mastermarisa.maidbeacon.MaidBeacon;
import com.mastermarisa.maidbeacon.config.Config;
import com.mastermarisa.maidbeacon.data.BeaconData;
import com.mastermarisa.maidbeacon.data.EffectAura;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@EventBusSubscriber(modid = MaidBeacon.MOD_ID)
public class EffectAuraManager {
    public static final List<String> defaultData;

    static {
        defaultData = Stream.of(
                new EffectAura("minecraft:speed", 0, 0, 1, 20),
                new EffectAura("minecraft:haste", 0, 0, 1, 20),
                new EffectAura("minecraft:resistance", 0, 1, 1, 20),
                new EffectAura("minecraft:jump_boost",0,1,1,20),
                new EffectAura("minecraft:strength",0,2,1,20),
                new EffectAura("minecraft:regeneration",0,3,2,20)
        ).map(EncodeUtils::toJson).toList();
    }

    @SubscribeEvent
    public static void onMaidTickEvent(MaidTickEvent event) {
        EntityMaid maid = event.getMaid();
        Level level = maid.level();
        if (level.isClientSide() || level.getGameTime() % Config.CHECK_INTERVAL() != 0) return;
        if (!maid.getArmorInvWrapper().getStackInSlot(3).is(Items.BEACON)) return;
        BeaconData beaconData = maid.getData(BeaconData.TYPE);
        ConcurrentHashMap<Float, List<EffectAura>> batched = beaconData.batched();
        for (float range : batched.keySet()) {
            List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, maid.getBoundingBox().inflate(range));
            targets.stream().filter(e -> EntityFilter.filter(e.getType())).forEach(e -> {
                for (EffectAura aura : batched.get(range)) {
                    e.addEffect(new MobEffectInstance(
                            aura.getHolder(),
                            Config.CHECK_INTERVAL() + 10,
                            aura.effectLevel,
                            false,
                            true
                    ));
                }
            });
        }
    }

    /**
     * 以Y轴为中心轴渲染旋转的粒子光环
     *
     * @param level 粒子渲染的level
     * @param aabb 使用的AABB
     * @param angle 旋转角[0,90]
     * @param step 粒子间隔(密度)
     */
    public static void drawAABBHaloYHorizontal(ServerLevel level, AABB aabb, double centerY, double angle, double step) {
        double minX = aabb.minX, minY = aabb.minY, minZ = aabb.minZ;
        double maxX = aabb.maxX, maxY = aabb.maxY, maxZ = aabb.maxZ;
        double centerX = (minX + maxX) / 2D, centerZ = (minZ + maxZ) / 2D;
        double lengthX = maxX - minX, lengthZ = maxZ - minZ;

        if (0 <= angle && angle <= 90) {
            double tan = Math.tan(Math.toRadians(angle - 45));
            int[][] edges = {{0,1}, {1,2}, {2,3}, {3,0}};
            double[][] vertices = {
                    {minX, centerY, centerZ + tan * lengthZ / 2}, {centerX + tan * lengthX / 2, centerY, maxZ},
                    {maxX, centerY, centerZ - tan * lengthZ / 2}, {centerX - tan * lengthX / 2, centerY, minZ}
            };
            drawParticles(level, edges, vertices, step);
        }
    }

    public static void drawAABBHaloYVertical(ServerLevel level, AABB aabb, double angle, double step) {
        double minX = aabb.minX, minY = aabb.minY, minZ = aabb.minZ;
        double maxX = aabb.maxX, maxY = aabb.maxY, maxZ = aabb.maxZ;
        double centerX = (minX + maxX) / 2D, centerZ = (minZ + maxZ) / 2D;
        double lengthX = maxX - minX, lengthZ = maxZ - minZ;

        if (0 <= angle && angle <= 90) {
            double tan = Math.tan(Math.toRadians(angle - 45));
            int[][] edges = {{0,4}, {1,5}, {2,6}, {3,7}, {0,2}, {1,3}, {4,6}, {5,7}};
            double[][] vertices = {
                    {minX, minY, centerZ + tan * lengthZ / 2}, {centerX + tan * lengthX / 2, minY, maxZ},
                    {maxX, minY, centerZ - tan * lengthZ / 2}, {centerX - tan * lengthX / 2, minY, minZ},
                    {minX, maxY, centerZ + tan * lengthZ / 2}, {centerX + tan * lengthX / 2, maxY, maxZ},
                    {maxX, maxY, centerZ - tan * lengthZ / 2}, {centerX - tan * lengthX / 2, maxY, minZ}
            };
            drawParticles(level, edges, vertices, step);
        }
    }

    private static void drawParticles(ServerLevel level, int[][] edges, double[][] vertices, double step) {
        for (int[] edge : edges) {
            double[] p1 = vertices[edge[0]];
            double[] p2 = vertices[edge[1]];
            double length = Math.sqrt(
                    Math.pow(p2[0]-p1[0], 2) +
                            Math.pow(p2[1]-p1[1], 2) +
                            Math.pow(p2[2]-p1[2], 2)
            );
            int count = (int) Math.ceil(length / step);

            for (int i = 0; i <= count; i++) {
                double t = (double) i / count;
                double x = p1[0] + (p2[0] - p1[0]) * t;
                double y = p1[1] + (p2[1] - p1[1]) * t;
                double z = p1[2] + (p2[2] - p1[2]) * t;

                level.sendParticles(
                        ParticleTypes.END_ROD,
                        x, y, z,
                        1,
                        0, 0, 0,
                        0
                );
            }
        }
    }
}
