package net.fractalcoder.cursedtools;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.PlayerPickItemEvents;
import net.fractalcoder.cursedtools.HauntedHoeEvents.HauntedHoeEvents;
import net.fractalcoder.cursedtools.entity.ModEntities;
import net.fractalcoder.cursedtools.item.ModItems;
import net.fractalcoder.cursedtools.util.ModComponents;
import net.fractalcoder.cursedtools.util.ModToolMaterial;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ItemEvent;
import java.util.Random;

public class CursedTools implements ModInitializer {
	public static final String MOD_ID = "cursed-tools";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModToolMaterial.registerToolMaterials();
		ModComponents.registerModComponents();

		Text Ghost_Scythe_Name = Text.literal("Ghost Scythe").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xa1aebf)));


		ModItems.registerModItems();
		ModEntities.registerModEntities();

		PlayerBlockBreakEvents.AFTER.register(this::onBlockBreak);


		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
			if (!world.isClient && player.getMainHandStack().getItem() == ModItems.GREEDY_PICKAXE) {
				int greed = player.getMainHandStack().getOrDefault(ModComponents.GREED, 0);
				if (player instanceof ServerPlayerEntity serverPlayer) {
					if (!isOre(state.getBlock())) {
						Random random = new Random();
						int randomNum = random.nextInt(50);
						if (randomNum == 4) {player.getMainHandStack().set(ModComponents.GREED, ++greed);}
						if (greed >= 5) {
							if (player.getHungerManager().getFoodLevel() > 0) {
								player.getHungerManager().setFoodLevel(player.getHungerManager().getFoodLevel() - 2);
							}else if (player.getHungerManager().getFoodLevel() == 0) {
								player.setHealth(player.getHealth() - 2);
								player.playSoundToPlayer(SoundEvents.ENTITY_PLAYER_HURT, SoundCategory.PLAYERS, 1, 1);
								player.tiltScreen(20, 20);
							}
						}
					}else {
						if (greed >= 0) {player.getMainHandStack().set(ModComponents.GREED, greed-1);}
					}
				}
			}
		});
	}

	private void onBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState blockState, BlockEntity blockEntity){
		HauntedHoeEvents.onBlockBreak(world, player, pos, blockState, blockEntity);
	}

	private boolean isOre(Block block) {
		return block == Blocks.IRON_ORE || block == Blocks.GOLD_ORE || block == Blocks.DEEPSLATE_GOLD_ORE || block == Blocks.DEEPSLATE_IRON_ORE || block == Blocks.DIAMOND_ORE || block == Blocks.DEEPSLATE_DIAMOND_ORE || block == Blocks.EMERALD_ORE || block == Blocks.DEEPSLATE_EMERALD_ORE|| block == Blocks.GOLD_BLOCK|| block == Blocks.IRON_BLOCK|| block == Blocks.EMERALD_BLOCK|| block == Blocks.DIAMOND_BLOCK|| block == Blocks.NETHERITE_BLOCK|| block == Blocks.ANCIENT_DEBRIS;
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}