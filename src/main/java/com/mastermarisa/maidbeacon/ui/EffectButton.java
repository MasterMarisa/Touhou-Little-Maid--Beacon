package com.mastermarisa.maidbeacon.ui;

import com.mastermarisa.maidbeacon.MaidBeacon;
import com.mastermarisa.maidbeacon.config.ModConfig;
import com.mastermarisa.maidbeacon.logic.EffectAura;
import com.mastermarisa.maidbeacon.network.EffectButtonClickPayload;
import com.mastermarisa.maidbeacon.utils.RomanConverter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;


import java.util.List;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class EffectButton extends Button {
    public static final int width = 22;
    public static final int height = 22;
    public static final Minecraft mc = Minecraft.getInstance();
    private static final Font font;
    public static final ResourceLocation buttonImg = ResourceLocation.parse("minecraft:textures/gui/sprites/container/beacon/button.png");
    public static final ResourceLocation disabledButtonImg = ResourceLocation.parse("minecraft:textures/gui/sprites/container/beacon/button_disabled.png");
    public static final ResourceLocation highlightedButtonImg = ResourceLocation.parse("minecraft:textures/gui/sprites/container/beacon/button_highlighted.png");
    private final ResourceLocation effectIcon;
    private final String effectId;
    private boolean toggled;
    private final int entityId;
    private int totalCost;
    private int maxCost;

    static {
        font = mc.font;
    }

    public EffectButton(int x,int y,ResourceLocation effectIcon,String effectId,int entityId,boolean enabled){
        super(x,y,width,height, CommonComponents.EMPTY,(button)-> ((EffectButton)button).onButtonClicked(),DEFAULT_NARRATION);
        this.effectIcon = effectIcon;
        this.effectId = effectId;
        this.entityId = entityId;
        this.toggled = enabled;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            if(toggled){
                graphics.blit(buttonImg, this.getX(), this.getY(), 0, 0, width, height, width, height);
            } else {
                graphics.blit(disabledButtonImg, this.getX(), this.getY(), 0, 0, width, height, width, height);
            }

            graphics.blit(effectIcon, this.getX() + 2, this.getY() + 2, 0, 0, 18, 18, 18, 18);
        }
    }

    public void playDownSound(SoundManager soundManager) {
        soundManager.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
    }

    public void onButtonClicked(){
        if(!toggled && totalCost + Objects.requireNonNull(ModConfig.getEffectAuraByID(effectId)).cost > maxCost) return;
        EffectButtonClickPayload packet = new EffectButtonClickPayload(toggled ? 0 : 1,entityId,effectId);
        PacketDistributor.sendToServer(packet);
        toggled = !toggled;
    }

    public void setCost(int totalCost,int maxCost){
        this.totalCost = totalCost;
        this.maxCost = maxCost;
    }

    public void tryRenderToolTip(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        if(this.getX() < mouseX && mouseX < this.getX() + width && this.getY() < mouseY && mouseY < this.getY() + height){
            EffectAura aura = ModConfig.getEffectAuraByID(effectId);
            Component effectDescription = aura.effect.value().getDisplayName().copy().append(" " + RomanConverter.intToRoman(aura.effectLevel + 1)).withStyle(ChatFormatting.WHITE);
            graphics.renderTooltip(font, effectDescription, mouseX, mouseY);
        }
    }
}
