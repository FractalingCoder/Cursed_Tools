package net.fractalcoder.cursedtools.HauntedHoeEvents;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class HauntedHoeEvents {

    public static void onBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState blockState, BlockEntity blockEntity) {
        if (!world.isClient()) {
            Random random = new Random();
                if (blockState.getBlock().equals(Blocks.BEETROOTS)) {
                    if (player.getInventory().contains(Items.BEETROOT_SEEDS.getDefaultStack())) {
                        world.setBlockState(pos, Blocks.BEETROOTS.getDefaultState());
                        removeItem(player, Items.BEETROOT_SEEDS, 1);
                    }
                } else if (blockState.getBlock().equals(Blocks.CARROTS)) {
                    if (player.getInventory().contains(new ItemStack(Items.CARROT))) {
                        world.setBlockState(pos, Blocks.CARROTS.getDefaultState());
                        removeItem(player, Items.CARROT, 1);
                    }
                } else if (blockState.getBlock().equals(Blocks.POTATOES)) {
                    if (player.getInventory().contains(new ItemStack(Items.POTATO))) {
                        world.setBlockState(pos, Blocks.POTATOES.getDefaultState());
                        removeItem(player, Items.POTATO, 1);
                    }
                } else if (blockState.getBlock().equals(Blocks.NETHER_WART)) {
                    if (player.getInventory().contains(new ItemStack(Items.NETHER_WART))) {
                        world.setBlockState(pos, Blocks.NETHER_WART.getDefaultState());
                        removeItem(player, Items.NETHER_WART, 1);
                    }
                }
        }
    }

    public static void removeItem(PlayerEntity player, Item item, Integer amount){
        if (player.getInventory().contains(item.getDefaultStack())) {
            // Iterate through the player's inventory
            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack stack = player.getInventory().getStack(i);

                // Check if the stack is the item you want to remove
                if (stack.getItem().equals(item)) {
                    // Remove one item from this stack
                    stack.decrement(amount); // This decrements the stack size by 1
                }
            }
        }
    }

    public static void initialize() {
    }
}
