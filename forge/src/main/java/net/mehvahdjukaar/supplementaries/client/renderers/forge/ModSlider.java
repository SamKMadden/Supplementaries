package net.mehvahdjukaar.supplementaries.client.renderers.forge;

import einstein.jmc.blocks.CakeOvenBlock;
import einstein.jmc.util.CakeBuilder;
import net.mehvahdjukaar.supplementaries.client.screens.widgets.ISlider;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.widget.ForgeSlider;

public class ModSlider extends ForgeSlider implements ISlider, GuiEventListener {
    public ModSlider(int x, int y, int width, int height, Component prefix, Component suffix, double minValue, double maxValue, double currentValue, double stepSize, int precision, boolean drawString) {
        super(x, y, width, height, prefix, suffix, minValue, maxValue, currentValue, stepSize, precision, drawString);
    }

    public ModSlider(int x, int y, int width, int height, Component prefix, Component suffix, double minValue, double maxValue, double currentValue, boolean drawString) {
        super(x, y, width, height, prefix, suffix, minValue, maxValue, currentValue, drawString);
    }
}
