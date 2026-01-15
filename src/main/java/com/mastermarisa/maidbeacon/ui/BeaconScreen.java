package com.mastermarisa.maidbeacon.ui;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mastermarisa.maidbeacon.config.ModConfig;
import com.mastermarisa.maidbeacon.logic.EffectAura;
import com.mastermarisa.maidbeacon.logic.MovingBeacon;
import com.mastermarisa.maidbeacon.registry.ModAttachmentTypes;
import com.mastermarisa.maidbeacon.utils.ModUtils;
import com.mastermarisa.maidbeacon.utils.MovingBeaconHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class BeaconScreen extends Screen {
    private static final Minecraft mc;
    private static final Font font;
    private static final TextureManager textureManager;
    private static final ModelManager modelManager;
    private static final int iconSize = 18;
    private static final int startY = 40;
    private static final int startX = 20;
    private static final int levelSeparation = 40;

    static {
        mc = Minecraft.getInstance();
        font = mc.font;
        textureManager = mc.getTextureManager();
        modelManager = mc.getModelManager();
    }

    private Player player;
    private EntityMaid maid;
    private MovingBeacon movingBeacon;
    private boolean init = false;
    private List<EffectButton> buttons;

    public BeaconScreen(Player player,EntityMaid maid){
        super(Component.empty());
        this.player = player;
        this.maid = maid;
        this.movingBeacon = maid.getData(ModAttachmentTypes.MOVING_BEACON);
        this.buttons = new ArrayList<>();
    }

    public static void open(Player player,EntityMaid maid) {
        Minecraft.getInstance().setScreen(new BeaconScreen(player,maid));
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        super.render(graphics, mouseX, mouseY, partialTicks);

        if(!init){
            renderButtons();
            init = true;
        }

        int totalCost = movingBeacon.getTotalCost();
        int maxCost = ModConfig.getCostByLevel(movingBeacon.beaconLevel);
        for(int i = 0;i < this.buttons.size();i++){
            this.buttons.get(i).setCost(totalCost,maxCost);
            this.buttons.get(i).tryRenderToolTip(graphics,mouseX,mouseY,partialTicks);
        }

        String str = "Cost：" + String.valueOf(totalCost) + "/" + String.valueOf(maxCost);
        graphics.drawString(font,str,(mc.getWindow().getGuiScaledWidth() - font.width(str)) / 2,startY / 2,Color.WHITE.getRGB());

        for (int i = 0;i <= MovingBeaconHelper.getBeaconLevel(maid);i++) {
            graphics.drawString(font,"信标等级：" + String.valueOf(i),startX,startY + i * levelSeparation, Color.WHITE.getRGB());
        }
    }

    private void renderButtons(){
        if (!this.buttons.isEmpty()){
            for (EffectButton button : this.buttons){
                this.removeWidget(button);
            }
            this.buttons.clear();
        }
        for (int i = 0;i <= MovingBeaconHelper.getBeaconLevel(maid);i++){
            List<EffectAura> auraList = ModConfig.getEffectAurasByLevel(i);
            if (auraList == null) continue;
            for (int j = 0;j < auraList.size();j++){
                EffectAura aura = auraList.get(j);
                ResourceLocation effectIcon = ModUtils.getPath(aura.effect);
                int separation = iconSize + 10;
                int x = (int)(mc.getWindow().getGuiScaledWidth() / 2d - (auraList.size() / 2d) * separation + j * separation);
                int y = startY + i * levelSeparation - 7;
                EffectButton effectButton = this.addRenderableWidget(new EffectButton(x,y,effectIcon,aura.effect.getRegisteredName(),maid.getId(),MovingBeaconHelper.isEffectActivated(maid,aura.effect.getRegisteredName())));
                this.buttons.add(effectButton);
            }
        }
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height);

        renderButtons();
    }
}
