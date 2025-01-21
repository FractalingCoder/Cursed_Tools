package net.fractalcoder.cursedtools.util;

import net.fractalcoder.cursedtools.CursedTools;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.List;

public record ModToolMaterial(TagKey<Block> incorrectBlocksForDrops, int durability, float speed, float attackDamageBonus, int enchantmentValue, TagKey<Item> repairItems) {
    public static final ToolMaterial GREEDY;
    public static final Identifier BASE_MINING_SPEED_ID = Identifier.of("base_mining_speed_id", CursedTools.MOD_ID);

    public ModToolMaterial(TagKey<Block> incorrectBlocksForDrops, int durability, float speed, float attackDamageBonus, int enchantmentValue, TagKey<Item> repairItems) {
            this.incorrectBlocksForDrops = incorrectBlocksForDrops;
            this.durability = durability;
            this.speed = speed;
            this.attackDamageBonus = attackDamageBonus;
            this.enchantmentValue = enchantmentValue;
            this.repairItems = repairItems;
    }

    private Item.Settings applyBaseSettings(Item.Settings settings) {
        return settings.maxDamage(this.durability).repairable(this.repairItems).enchantable(this.enchantmentValue);
    }

    public Item.Settings applyToolSettings(Item.Settings settings, TagKey<Block> effectiveBlocks, float attackDamage, float attackSpeed, float miningSpeed) {
        RegistryEntryLookup<Block> registryEntryLookup = Registries.createEntryLookup(Registries.BLOCK);
        return this.applyBaseSettings(settings).component(DataComponentTypes.TOOL, new ToolComponent(List.of(ToolComponent.Rule.ofNeverDropping(registryEntryLookup.getOrThrow(this.incorrectBlocksForDrops)), ToolComponent.Rule.ofAlwaysDropping(registryEntryLookup.getOrThrow(effectiveBlocks), this.speed)), 1.0F, 1)).attributeModifiers(this.createToolAttributeModifiers(attackDamage, attackSpeed, miningSpeed));
    }

    private AttributeModifiersComponent createToolAttributeModifiers(float attackDamage, float attackSpeed, float miningSpeed) {
        return AttributeModifiersComponent.builder().add(EntityAttributes.ATTACK_DAMAGE, new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, (double)(attackDamage + this.attackDamageBonus), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).add(EntityAttributes.ATTACK_SPEED, new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, (double)attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).add(EntityAttributes.ATTACK_DAMAGE, new EntityAttributeModifier(ModToolMaterial.BASE_MINING_SPEED_ID, (double)(miningSpeed + this.attackDamageBonus), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).build();
    }

    public Item.Settings applySwordSettings(Item.Settings settings, float attackDamage, float attackSpeed) {
        RegistryEntryLookup<Block> registryEntryLookup = Registries.createEntryLookup(Registries.BLOCK);
        return this.applyBaseSettings(settings).component(DataComponentTypes.TOOL, new ToolComponent(List.of(ToolComponent.Rule.ofAlwaysDropping(RegistryEntryList.of(new RegistryEntry[]{Blocks.COBWEB.getRegistryEntry()}), 15.0F), ToolComponent.Rule.of(registryEntryLookup.getOrThrow(BlockTags.SWORD_EFFICIENT), 1.5F)), 1.0F, 2)).attributeModifiers(this.createSwordAttributeModifiers(attackDamage, attackSpeed));
    }

    private AttributeModifiersComponent createSwordAttributeModifiers(float attackDamage, float attackSpeed) {
        return AttributeModifiersComponent.builder().add(EntityAttributes.ATTACK_DAMAGE, new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, (double)(attackDamage + this.attackDamageBonus), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).add(EntityAttributes.ATTACK_SPEED, new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, (double)attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).build();
    }

    public TagKey<Block> incorrectBlocksForDrops() {
            return this.incorrectBlocksForDrops;
        }

    public int durability() {
            return this.durability;
        }

    public float speed() {
            return this.speed;
        }

    public float attackDamageBonus() {
            return this.attackDamageBonus;
        }

    public int enchantmentValue() {
            return this.enchantmentValue;
        }

    public TagKey<Item> repairItems() {
            return this.repairItems;
        }

    static {
        GREEDY = new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 750, 12.0F, 1.0F, 22, ItemTags.DIAMOND_TOOL_MATERIALS);
    }

    public static void registerToolMaterials() {
        CursedTools.LOGGER.info("Registering Mod Materials for " + CursedTools.MOD_ID);
    }
}

