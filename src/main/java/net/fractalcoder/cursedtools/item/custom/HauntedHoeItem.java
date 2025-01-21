package net.fractalcoder.cursedtools.item.custom;

import com.mojang.datafixers.util.Pair;
import net.fractalcoder.cursedtools.util.ModComponents;
import net.minecraft.block.Block;
import net.minecraft.client.sound.Sound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class HauntedHoeItem extends HoeItem {
    public HauntedHoeItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>> pair = (Pair)TILLING_ACTIONS.get(world.getBlockState(blockPos).getBlock());
        if (pair == null) {
            return ActionResult.SUCCESS;
        } else {
            Predicate<ItemUsageContext> predicate = (Predicate)pair.getFirst();
            Consumer<ItemUsageContext> consumer = (Consumer)pair.getSecond();
            if (predicate.test(context)) {
                PlayerEntity playerEntity = context.getPlayer();
                world.playSound(playerEntity, blockPos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!world.isClient) {
                    consumer.accept(context);
                    if (playerEntity != null) {
                        if (world.getTimeOfDay() <= 23000 && world.getTimeOfDay() >= 12542) {
                            Random random = new Random();
                            int chance = random.nextInt(40);
                            if (chance == 25) {
                                BlockPos SkelePos = new BlockPos(context.getBlockPos().getX(), context.getBlockPos().getY() + 1, blockPos.getZ());
                                EntityType.SKELETON.spawn(Objects.requireNonNull(world.getServer()).getOverworld(), SkelePos, SpawnReason.DIMENSION_TRAVEL);
                                world.addBlockBreakParticles(blockPos, world.getBlockState(blockPos));
                                world.playSound(context.getPlayer(), SkelePos, SoundEvents.BLOCK_CREAKING_HEART_BREAK, SoundCategory.HOSTILE, 2, 0.1F);
                            }
                        }
                        context.getStack().damage(1, playerEntity, LivingEntity.getSlotForHand(context.getHand()));
                    }
                }

                return ActionResult.SUCCESS;
            } else {
                return ActionResult.PASS;
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(1, Text.literal("§6§oForged from the cursed remains of the long-dead,").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xAA5500))));
        tooltip.add(2, Text.literal("§6§othis hoe seeks to keep its fields forever planted").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xAA5500))));
        tooltip.add(3, Text.literal("§6§o— though not all that grows is good.").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xAA5500))));
        super.appendTooltip(stack, context, tooltip, type);
    }
}
