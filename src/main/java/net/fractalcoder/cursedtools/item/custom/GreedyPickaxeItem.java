package net.fractalcoder.cursedtools.item.custom;

import net.fractalcoder.cursedtools.util.ModComponents;
import net.fractalcoder.cursedtools.util.ModToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

public class GreedyPickaxeItem extends PickaxeItem {
    public GreedyPickaxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(1, Text.of("§2§oThis pickaxe craves the treasures of the earth."));
        tooltip.add(2, Text.of("§2§oUse it wisely, or it will take what it is owed with your life and your items."));
        tooltip.add(3, Text.of(" "));
        tooltip.add(4, Text.literal("§aGreediness: " + stack.getOrDefault(ModComponents.GREED, 0)));

        super.appendTooltip(stack, context, tooltip, type);
    }
}
