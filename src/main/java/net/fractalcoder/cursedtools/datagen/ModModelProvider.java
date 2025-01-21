package net.fractalcoder.cursedtools.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fractalcoder.cursedtools.item.ModItems;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.GREEDY_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.HAUNTED_HOE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.VEINCALLER, Models.HANDHELD);
    }
}
