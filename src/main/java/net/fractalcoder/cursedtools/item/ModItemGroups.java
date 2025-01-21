package net.fractalcoder.cursedtools.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fractalcoder.cursedtools.CursedTools;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup VANILLACONSTRUCTS_ITEM_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(CursedTools.MOD_ID, "vanilla_constructs"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.GREEDY_PICKAXE))
                    .displayName(Text.translatable("itemgroup.vanillaconstructs"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.GREEDY_PICKAXE);
                        entries.add(ModItems.GHOST_SCYTHE);
                        entries.add(ModItems.HAUNTED_HOE);
                        entries.add(ModItems.VEINCALLER);


                    }).build());

    public static void registerItemGroups() {
        CursedTools.LOGGER.info("Registering Item Groups for " + CursedTools.MOD_ID);
    }
}
