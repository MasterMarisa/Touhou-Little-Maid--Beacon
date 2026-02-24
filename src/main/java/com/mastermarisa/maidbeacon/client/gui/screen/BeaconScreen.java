package com.mastermarisa.maidbeacon.client.gui.screen;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mastermarisa.maidbeacon.client.gui.base.*;
import com.mastermarisa.maidbeacon.client.gui.screen.elements.UIEffectButton;
import com.mastermarisa.maidbeacon.config.Config;
import com.mastermarisa.maidbeacon.data.BeaconData;
import com.mastermarisa.maidbeacon.data.EffectAura;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class BeaconScreen extends Screen {
    private static final Minecraft mc;
    private static final Font font;

    public final UUID uuid;
    public final BeaconData beaconData;
    private UIContainerVertical effects;
    private UILabel costLabel;
    private double YOffset;

    private BeaconScreen(EntityMaid maid){
        super(Component.empty());
        this.uuid = maid.getUUID();
        this.beaconData = maid.getData(BeaconData.TYPE);
        initEffects();
        resetCostLabel(beaconData.getUsedCost());
    }

    public static void open(EntityMaid maid) {
        Minecraft.getInstance().setScreen(new BeaconScreen(maid));
    }

    private void initEffects() {
        List<UIContainerHorizontal> lines = new ArrayList<>();
        for (int i = 0;i < Math.min(Config.MAX_LEVEL(), beaconData.level) + 1;i++) {
            List<EffectAura> auras = Config.BY_LEVEL(i);
            if (auras == null) continue;
            lines.add(UIContainerHorizontal.wrap(auras.stream().map(a -> new UIEffectButton(a, uuid, beaconData, this)).toList(), 16, 0, UIContainerHorizontal.ElementAlignment.CENTER));
        }
        effects = UIContainerVertical.wrap(lines, 24, 0, UIContainerVertical.ElementAlignment.UP);
        effects.setCenterX(getScreenCenterX() + 8);
        effects.setMinY((int) (60 + YOffset));
        effects.order();
        lines.forEach(UIContainerHorizontal::order);
    }

    public void resetCostLabel(int usedCost) {
        costLabel = new UILabel("Cost:" + usedCost + "/" + Config.getCost(beaconData.level), UILabel.TextAlignment.LEFT, Color.WHITE, true);
        costLabel.setCenter(getScreenCenterX(), (int) (30 + YOffset));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        UIElement.render(graphics, effects, mouseX, mouseY);
        UIElement.render(graphics, costLabel, mouseX, mouseY);
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height);
        resize();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        UIElement.onMouseClicked(effects, mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY != 0) {
            YOffset += scrollY * 3;
            if (YOffset > 0) YOffset = 0;
            resize();
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void resize() {
        effects.setCenterX(getScreenCenterX() + 8);
        effects.setMinY((int) (60 + YOffset));
        effects.order();
        costLabel.setCenter(getScreenCenterX(), (int) (30 + YOffset));
    }

    public static int getScreenCenterX(){
        return mc.getWindow().getGuiScaledWidth() / 2;
    }

    public static int getScreenCenterY(){
        return mc.getWindow().getGuiScaledHeight() / 2;
    }

    static {
        mc = Minecraft.getInstance();
        font = mc.font;
    }
}
