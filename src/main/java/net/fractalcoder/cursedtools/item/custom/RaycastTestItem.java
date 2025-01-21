package net.fractalcoder.cursedtools.item.custom;

import com.google.common.base.MoreObjects;
import net.fractalcoder.cursedtools.entity.ModEntities;
import net.fractalcoder.cursedtools.entity.custom.HomingArrowEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class RaycastTestItem extends Item {
    LivingEntity target = null;
    LivingEntity previousTarget = null;

    public RaycastTestItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (selected) {
            target = raycastEntity((PlayerEntity) entity, 25);
            if (target != null) {
                target.setGlowing(true);
                previousTarget = target;
            }else if (previousTarget != null) {
                previousTarget.setGlowing(false);
                previousTarget = null;
            }
        }else if (target != null || previousTarget != null) {
            previousTarget.setGlowing(false);
            target.setGlowing(false);
            target = null;
            previousTarget = null;
        }
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (!(user instanceof PlayerEntity playerEntity)) {
            return ActionResult.SUCCESS;
        } else if (target != null) {
            target.setGlowing(false);
            Vec3d eyePosition = user.getPos().add(0, user.getEyeHeight(user.getPose()), 0);
            Vec3d lookDirection = user.getRotationVector();
            Vec3d spawnPosition = eyePosition.add(lookDirection.multiply(0.5)); // Offset in front of the player

            HomingArrowEntity homingArrow = new HomingArrowEntity(EntityType.SHULKER_BULLET, world);
            homingArrow.setOwner(user);
            homingArrow.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 0.5F, 1.0F);
            homingArrow.setTarget(target);
            homingArrow.setNoGravity(true);
            homingArrow.setPos(spawnPosition.x, spawnPosition.y, spawnPosition.z);
            world.spawnEntity(homingArrow);
            target = null;
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    public static LivingEntity raycastEntity(PlayerEntity player, double maxDistance) {
        Vec3d start = player.getCameraPosVec(1.0F);
        Vec3d direction = player.getRotationVec(1.0F);
        Vec3d end = start.add(direction.multiply(maxDistance));

        Box box = new Box(start, end).expand(1.0); // Expand to capture entities close to the ray
        List<Entity> entities = player.getWorld().getOtherEntities(player, box);

        for (Entity entity : entities) {
            Box entityBox = entity.getBoundingBox().expand(0.5);
            Optional<Vec3d> hitResult = entityBox.raycast(start, end);
            if (hitResult.isPresent()) {
                return (LivingEntity) entity;
            }
        }
        return null;
    }

}
