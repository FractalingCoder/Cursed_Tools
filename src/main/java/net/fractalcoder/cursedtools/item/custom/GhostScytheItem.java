package net.fractalcoder.cursedtools.item.custom;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.nimbusds.openid.connect.sdk.federation.entities.EntityType;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityType;
import net.fractalcoder.cursedtools.util.ModComponents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class GhostScytheItem extends HoeItem {
    protected static final Map<Block, Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>>> TILLING_ACTIONS;

    public GhostScytheItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Random random = new Random();
        int randomNum = random.nextInt(50);
        //Small Life Steal
        if (randomNum == 15 || randomNum == 35) {
            if (attacker.getHealth() < attacker.getMaxHealth()) {
                attacker.setHealth(attacker.getHealth() + 2);
            }
        } //Large Life Steal
        else if (randomNum == 25) {
            if (attacker.getHealth() < attacker.getMaxHealth()) {
                attacker.setHealth(attacker.getHealth() + 4);
            }
        }
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        Text coloredText = Text.literal("Forged from the remnants of restless spirits,").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xa1aebf)));
        Text coloredText2 = Text.literal("this scythe reaps more than just crops.").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xa1aebf)));

        tooltip.add(1, Text.of(coloredText));
        tooltip.add(2, Text.of(coloredText2));
        tooltip.add(Text.of(" "));

        super.appendTooltip(stack, context, tooltip, type);
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        BlockPos blockPos = context.getBlockPos();
        Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>> pair = (Pair)TILLING_ACTIONS.get(world.getBlockState(blockPos).getBlock());
        if (pair == null || !Objects.requireNonNull(player).isSneaking()) {
            assert player != null;
            Vec3d lookVec = player.getRotationVector();  // This returns the direction player is facing

            // Negate the vector to go in the opposite direction
            Vec3d launchVec = new Vec3d(-lookVec.x, -lookVec.y, -lookVec.z).normalize();

            spawnParticleCircle(world, blockPos, context.getSide());
            world.playSound(player, blockPos, context.getWorld().getBlockState(blockPos).getSoundGroup().getBreakSound(), SoundCategory.BLOCKS);
            world.playSound(player, blockPos, SoundEvents.ITEM_MACE_SMASH_GROUND_HEAVY, SoundCategory.PLAYERS, 5.0F, 0.0F);

            player.addVelocity(launchVec.x, launchVec.y * 1.5, launchVec.z); // Adjust values as needed

            player.currentExplosionImpactPos = this.getCurrentExplosionImpactPos(player);
            player.setIgnoreFallDamageFromCurrentExplosion(true);

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
                        context.getStack().damage(1, playerEntity, LivingEntity.getSlotForHand(context.getHand()));
                    }
                }
                return ActionResult.SUCCESS;
            } else {
                return ActionResult.PASS;
            }
        }
    }

    private Vec3d getCurrentExplosionImpactPos(PlayerEntity player) {
        return player.shouldIgnoreFallDamageFromCurrentExplosion() && player.currentExplosionImpactPos != null && player.currentExplosionImpactPos.y <= player.getPos().y ? player.currentExplosionImpactPos : player.getPos();
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (selected) {
            Random random = new Random();
            int randomNum = random.nextInt(500);
            if (randomNum == 250) {
                if (entity instanceof PlayerEntity) {
                    if (!((PlayerEntity) entity).isInCreativeMode()) {
                        entity.serverDamage(world.getDamageSources().magic(), 1.0F);
                    }
                }
            }
        }
    }

    public static Consumer<ItemUsageContext> createTillAction(BlockState result) {
        return (context) -> {
            context.getWorld().setBlockState(context.getBlockPos(), result, 11);
            context.getWorld().emitGameEvent(GameEvent.BLOCK_CHANGE, context.getBlockPos(), GameEvent.Emitter.of(context.getPlayer(), result));
        };
    }

    public static Consumer<ItemUsageContext> createTillAndDropAction(BlockState result, ItemConvertible droppedItem) {
        return (context) -> {
            context.getWorld().setBlockState(context.getBlockPos(), result, 11);
            context.getWorld().emitGameEvent(GameEvent.BLOCK_CHANGE, context.getBlockPos(), GameEvent.Emitter.of(context.getPlayer(), result));
            Block.dropStack(context.getWorld(), context.getBlockPos(), context.getSide(), new ItemStack(droppedItem));
        };
    }

    public static boolean canTillFarmland(ItemUsageContext context) {
        return context.getSide() != Direction.DOWN && context.getWorld().getBlockState(context.getBlockPos().up()).isAir();
    }

    private void spawnParticleCircle(World world, BlockPos blockPos, Direction side) {
        // Circle properties
        int radius = 3;
        int numParticles = 5;
        double angleIncrement = Math.PI * 2 / numParticles;

        // Adjust rotation based on the surface clicked (wall, ceiling, or floor)
        double rotationOffsetX = 0, rotationOffsetZ = 0;
        switch (side) {
            case UP:
            case DOWN:
                // Rotate horizontally (for floor or ceiling)
                rotationOffsetX = 0;
                rotationOffsetZ = 1;
                break;
            case NORTH:
            case SOUTH:
                // Rotate along the X axis (for walls)
                rotationOffsetX = 1;
                rotationOffsetZ = 0;
                break;
            case EAST:
            case WEST:
                // Rotate along the Z axis (for walls)
                rotationOffsetX = 0;
                rotationOffsetZ = 1;
                break;
        }

        // Spawn particles in a circle around the block position
        for (int i = 0; i < numParticles; i++) {
            double angle = angleIncrement * i;
            double xOffset = radius * Math.cos(angle) * rotationOffsetX;
            double zOffset = radius * Math.sin(angle) * rotationOffsetZ;
            world.addBlockBreakParticles(blockPos, world.getBlockState(blockPos));
        }
    }

    static {
        TILLING_ACTIONS = Maps.newHashMap(ImmutableMap.of(Blocks.GRASS_BLOCK, Pair.of(HoeItem::canTillFarmland, createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.DIRT_PATH, Pair.of(HoeItem::canTillFarmland, createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.DIRT, Pair.of(HoeItem::canTillFarmland, createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.COARSE_DIRT, Pair.of(HoeItem::canTillFarmland, createTillAction(Blocks.DIRT.getDefaultState())), Blocks.ROOTED_DIRT, Pair.of((itemUsageContext) -> {
            return true;
        }, createTillAndDropAction(Blocks.DIRT.getDefaultState(), Items.HANGING_ROOTS))));
    }
}
