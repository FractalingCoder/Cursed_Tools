package net.fractalcoder.cursedtools.item;

import net.fractalcoder.cursedtools.CursedTools;
import net.fractalcoder.cursedtools.item.custom.*;
import net.fractalcoder.cursedtools.util.ModToolMaterial;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.io.DataInput;

public class ModItems {
    public static final Item GREEDY_PICKAXE = registerItem("greedy_pickaxe", new GreedyPickaxeItem(ModToolMaterial.GREEDY, 1, -1.0F, new Item.Settings().maxCount(1).maxDamage(500).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(CursedTools.MOD_ID, "greedy_pickaxe")))));
    public static final Item GHOST_SCYTHE = registerItem("ghost_scythe", new GhostScytheItem(ToolMaterial.IRON, 7, -1.5F, new Item.Settings().maxCount(1).maxDamage(750).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(CursedTools.MOD_ID, "ghost_scythe")))));

    public static final Item HAUNTED_HOE = registerItem("haunted_hoe", new HauntedHoeItem(ToolMaterial.DIAMOND, 1, -1.5F, new Item.Settings().maxDamage(723).maxCount(1).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(CursedTools.MOD_ID, "haunted_hoe")))));
    public static final Item TEST_RAYCAST = registerItem("raycast", new RaycastTestItem(new Item.Settings().maxCount(1).maxDamage(5).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(CursedTools.MOD_ID, "raycast")))));

    public static final Item VEINCALLER = registerItem("veincaller", new VeinMinerPickaxeItem(ToolMaterial.NETHERITE, 1, -1.0F, new Item.Settings().maxCount(1).maxDamage(750).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(CursedTools.MOD_ID, "veincaller")))));


    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, CursedTools.id(name), item);
    }

    public static void registerModItems() {
        CursedTools.LOGGER.info("Registering Mod Items for " + CursedTools.MOD_ID);
    }
}
