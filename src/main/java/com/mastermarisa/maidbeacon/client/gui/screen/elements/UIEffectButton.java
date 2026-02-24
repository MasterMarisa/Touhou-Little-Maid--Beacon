package com.mastermarisa.maidbeacon.client.gui.screen.elements;

import com.mastermarisa.maidbeacon.client.gui.base.*;
import com.mastermarisa.maidbeacon.client.gui.screen.BeaconScreen;
import com.mastermarisa.maidbeacon.config.Config;
import com.mastermarisa.maidbeacon.data.BeaconData;
import com.mastermarisa.maidbeacon.data.EffectAura;
import com.mastermarisa.maidbeacon.network.ChangeAuraStatePayload;
import com.mastermarisa.maidbeacon.utils.RomanConverter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.List;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class UIEffectButton extends UIButton {
    private static final ImageData buttonImg = new ImageData(
            ResourceLocation.parse("minecraft:textures/gui/sprites/container/beacon/button.png"),
            new Rectangle(0,0,22,22),
            22,22,
            22,22
    );

    private static final ImageData disabledImg = new ImageData(
            ResourceLocation.parse("minecraft:textures/gui/sprites/container/beacon/button_disabled.png"),
            new Rectangle(0,0,22,22),
            22,22,
            22,22
    );

    private final UIImage icon;
    private final UIImage bg;
    private final UIImage disabledBg;
    private final UILabel cost;
    private final int index;
    private final EffectAura aura;
    private final BeaconData beaconData;
    private final BeaconScreen screen;
    private boolean toggled;

    public UIEffectButton(EffectAura aura, UUID uuid, BeaconData beaconData, BeaconScreen screen) {
        super(new Rectangle(22,22), (btn) -> ((UIEffectButton) btn).trigger(uuid), 0);
        this.aura = aura;
        this.beaconData = beaconData;
        this.index = Config.EFFECT_AURAS().indexOf(aura);
        this.toggled = beaconData.activated.contains(index);
        this.screen = screen;
        this.cost = new UILabel("Cost:" + aura.cost, UILabel.TextAlignment.CENTER, Color.WHITE, true);
        icon = new UIImage(new ImageData(
                aura.getIcon(),
                new Rectangle(0,0,18,18),
                18,18,
                18,18
        ));
        bg = new UIImage(buttonImg);
        disabledBg = new UIImage(disabledImg);

        children = List.of(icon, cost);
        tooltip.add(aura.getHolder().value().getDisplayName().copy().append(" " + RomanConverter.toRoman(aura.effectLevel + 1)).withStyle(ChatFormatting.WHITE));
        tooltip.add(Component.translatable("gui.maidbeacon.tooltip.range", aura.range).withStyle(ChatFormatting.WHITE));
    }

    @Override
    protected void render(GuiGraphics graphics, int mouseX, int mouseY) {
        if (toggled) {
            bg.setCenter(getCenterX(), getCenterY());
            UIElement.render(graphics, bg, mouseX, mouseY);
        } else {
            disabledBg.setCenter(getCenterX(), getCenterY());
            UIElement.render(graphics, disabledBg, mouseX, mouseY);
        }
        icon.setCenter(getCenterX(), getCenterY());
        cost.setCenterX(getCenterX());
        cost.setMinY(icon.getMaxY() + 5);
        super.render(graphics, mouseX, mouseY);
    }

    public void trigger(UUID uuid) {
        if (toggled) {
            ChangeAuraStatePayload payload = new ChangeAuraStatePayload(0, uuid, index);
            PacketDistributor.sendToServer(payload);
            toggled = false;
            beaconData.activated.remove(index);
            screen.resetCostLabel(beaconData.getUsedCost());
        } else {
            int cost = beaconData.getUsedCost() + aura.cost;
            if (cost <= Config.getCost(beaconData.level)) {
                ChangeAuraStatePayload payload = new ChangeAuraStatePayload(1, uuid, index);
                PacketDistributor.sendToServer(payload);
                toggled = true;
                beaconData.activated.add(index);
                screen.resetCostLabel(beaconData.getUsedCost());
            }
        }
    }
}
