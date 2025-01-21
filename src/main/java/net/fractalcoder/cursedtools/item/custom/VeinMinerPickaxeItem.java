package net.fractalcoder.cursedtools.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class VeinMinerPickaxeItem extends PickaxeItem {
    public VeinMinerPickaxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        ToolComponent toolComponent = (ToolComponent)stack.get(DataComponentTypes.TOOL);
        if (toolComponent == null) {
            return false;
        } else {
            if (!world.isClient && state.getHardness(world, pos) != 0.0F && toolComponent.damagePerBlock() > 0) {
                stack.damage(toolComponent.damagePerBlock(), miner, EquipmentSlot.MAINHAND);
                if (state.isIn(BlockTags.DIAMOND_ORES)
                        || state.isIn(BlockTags.IRON_ORES)
                        || state.isIn(BlockTags.GOLD_ORES)
                        || state.isIn(BlockTags.REDSTONE_ORES)
                        || state.isIn(BlockTags.COPPER_ORES)
                        || state.isIn(BlockTags.COAL_ORES)
                        || state.isIn(BlockTags.STONE_ORE_REPLACEABLES)) {
                    ScanBlocks(world, state, pos, miner);
                }
            }

            return true;
        }
    }

    public static int getLevel(ItemStack stack, RegistryKey<Enchantment> enchantment){
        for (RegistryEntry<Enchantment> enchantments : stack.getEnchantments().getEnchantments()){
            if (enchantments.toString().contains(enchantment.getValue().toString())){
                return stack.getEnchantments().getLevel(enchantments);
            }
        }
        return 0;
    }

    public static boolean hasEnchantment(ItemStack stack, RegistryKey<Enchantment> enchantment) {
        return stack.getEnchantments().getEnchantments().toString().
                contains(enchantment.getValue().toString());
    }

    public static int calculateFortune(ItemStack stack, LivingEntity miner) {
        return getLevel(stack, Enchantments.FORTUNE);
    }

    public static void breakBlocksWithFortune(LivingEntity miner, World world, BlockPos pos, BlockState state) {
        int lvl = calculateFortune(miner.getMainHandStack(), miner);
        ItemStack itemStack = null;
        if (state.isIn(BlockTags.COAL_ORES)) {
            itemStack = Items.COAL_ORE.getDefaultStack();
        }else if (state.isIn(BlockTags.COPPER_ORES)) {
            itemStack = Items.RAW_COPPER.getDefaultStack();
        }else if (state.isIn(BlockTags.IRON_ORES)) {
            itemStack = Items.RAW_IRON.getDefaultStack();
        }else if (state.isIn(BlockTags.GOLD_ORES)) {
            itemStack = Items.RAW_GOLD.getDefaultStack();
        }else if (state.isIn(BlockTags.LAPIS_ORES)) {
            itemStack = Items.LAPIS_LAZULI.getDefaultStack();
        }else if (state.isIn(BlockTags.REDSTONE_ORES)) {
            itemStack = Items.REDSTONE.getDefaultStack();
        }else if (state.isIn(BlockTags.DIAMOND_ORES)) {
            itemStack = Items.DIAMOND.getDefaultStack();
        }
        Random random = new Random();
        int lvlNum = random.nextInt(lvl);
        world.breakBlock(pos, false, miner);
        ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
        for (int i = 1; i < lvlNum; i++) {
            world.spawnEntity(itemEntity);
        }
    }

    public static void ScanBlocks(World world, BlockState state, BlockPos pos, LivingEntity miner) {
        breakBlocksWithFortune(miner, world, pos, state);
        BlockPos newPos = null;
        newPos = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }
        newPos = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }
        newPos = new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ());
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }
        newPos = new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ());
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }
        newPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1);
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }
        newPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1);
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }
        newPos = new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ() + 1);
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }
        newPos = new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ() + 1);
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }
        newPos = new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ() - 1);
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }
        newPos = new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ() - 1);
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }



        newPos = new BlockPos(pos.getX() - 1, pos.getY() + 1, pos.getZ());
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }
        newPos = new BlockPos(pos.getX() + 1, pos.getY() + 1, pos.getZ());
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }
        newPos = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ() - 1);
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }
        newPos = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ() + 1);
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }
        newPos = new BlockPos(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }
        newPos = new BlockPos(pos.getX() - 1, pos.getY() + 1, pos.getZ() + 1);
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }
        newPos = new BlockPos(pos.getX() + 1, pos.getY() + 1, pos.getZ() - 1);
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }
        newPos = new BlockPos(pos.getX() - 1, pos.getY() + 1, pos.getZ() - 1);
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }

        newPos = new BlockPos(pos.getX() - 1, pos.getY() - 1, pos.getZ());
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }
        newPos = new BlockPos(pos.getX() + 1, pos.getY() - 1, pos.getZ());
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }
        newPos = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ() - 1);
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }
        newPos = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ() + 1);
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }
        newPos = new BlockPos(pos.getX() + 1, pos.getY() - 1, pos.getZ() + 1);
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }
        newPos = new BlockPos(pos.getX() - 1, pos.getY() - 1, pos.getZ() + 1);
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }
        newPos = new BlockPos(pos.getX() + 1, pos.getY() - 1, pos.getZ() - 1);
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }
        newPos = new BlockPos(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1);
        if (world.getBlockState(newPos).equals(state)) {
            breakBlocksWithFortune(miner, world, newPos, state);
            ScanBlocks(world, state, newPos, miner);
        }
    }
}
