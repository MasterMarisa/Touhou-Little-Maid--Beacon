package com.mastermarisa.maidbeacon.client.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mastermarisa.maidbeacon.MaidBeacon;
import com.mastermarisa.maidbeacon.config.Config;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@EventBusSubscriber(modid = MaidBeacon.MOD_ID, value = Dist.CLIENT)
public class OnRenderLiving {
    private static final ItemStack beaconStack = new ItemStack(Items.BEACON);
    private static final ResourceLocation BEAM_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft","textures/entity/beacon_beam.png");

    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Post<EntityMaid, EntityModel<EntityMaid>> event) {
        EntityMaid maid = (EntityMaid) event.getEntity();
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (!maid.getArmorInvWrapper().getStackInSlot(3).is(Items.BEACON)) return;
        if (Config.ENABLE_BEACON_RENDERING())
            renderBeaconModel(event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), maid, event.getPartialTick());
        if (Config.ENABLE_BEACON_BEAM_RENDERING())
            renderBeaconBeam(
                    event.getPoseStack(),
                    event.getMultiBufferSource(),
                    event.getPartialTick(),
                    1F,
                    mc.player.level().getGameTime(),
                    2, 128,
                    Color.WHITE.getRGB(),
                    0.06F, 0.06F,
                    maid
            );
    }

    private static void renderBeaconModel(PoseStack poseStack, MultiBufferSource bufferSource,
                                   int packedLight, EntityMaid entity, float partialTick) {
        poseStack.pushPose();

        poseStack.translate(0, entity.getBbHeight() + (entity.isMaidInSittingPose() ? Config.Y_OFFSET_SITTING() : Config.Y_OFFSET()), 0);

        long gameTime = entity.level().getGameTime();
        float rotation = (gameTime + partialTick) * 3;
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

        poseStack.scale(1F, 1F, 1F);

        Minecraft.getInstance().getItemRenderer().renderStatic(
                beaconStack,
                ItemDisplayContext.GROUND,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,
                entity.level(),
                entity.getId()
        );

        poseStack.popPose();
    }

    private static void renderBeaconBeam(PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, float textureScale,
                                         long gameTime, int yOffset, int height, int color, float beamRadius, float glowRadius, EntityMaid maid) {
        renderBeaconBeam(
                poseStack,
                bufferSource,
                BEAM_TEXTURE,
                partialTick,
                textureScale,
                gameTime,
                yOffset,
                height,
                color,
                beamRadius,
                glowRadius,
                maid
        );
    }

    private static void renderBeaconBeam(PoseStack poseStack, MultiBufferSource bufferSource, ResourceLocation beamLocation, float partialTick, float textureScale, long gameTime, int yOffset, int height, int color, float beamRadius, float glowRadius, EntityMaid maid) {
        int i = yOffset + height;
        poseStack.pushPose();
        double offset = 0.28D;
        if (maid.isMaidInSittingPose()) offset += Config.Y_OFFSET_SITTING() - Config.Y_OFFSET();
        poseStack.translate(0, offset, 0);
        float f = (float)Math.floorMod(gameTime, 40) + partialTick;
        float f1 = height < 0 ? f : -f;
        float f2 = Mth.frac(f1 * 0.2F - (float)Mth.floor(f1 * 0.1F));
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(f * 2.25F - 45.0F));
        float f3;
        float f5;
        float f6 = -beamRadius;
        float f9 = -beamRadius;
        float f12 = -1.0F + f2;
        float f13 = (float)height * textureScale * (0.5F / beamRadius) + f12;
        BeaconRenderer.renderPart(poseStack, bufferSource.getBuffer(RenderType.beaconBeam(beamLocation, false)), color, yOffset, i, 0.0F, beamRadius, beamRadius, 0.0F, f6, 0.0F, 0.0F, f9, 0.0F, 1.0F, f13, f12);
        poseStack.popPose();
        f3 = -glowRadius;
        float f4 = -glowRadius;
        f5 = -glowRadius;
        f6 = -glowRadius;
        f12 = -1.0F + f2;
        f13 = (float)height * textureScale + f12;
        BeaconRenderer.renderPart(poseStack, bufferSource.getBuffer(RenderType.beaconBeam(beamLocation, true)), FastColor.ARGB32.color(32, color), yOffset, i, f3, f4, glowRadius, f5, f6, glowRadius, glowRadius, glowRadius, 0.0F, 1.0F, f13, f12);
        poseStack.popPose();
    }
}
