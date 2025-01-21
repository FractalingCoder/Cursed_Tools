package net.fractalcoder.cursedtools.entity.custom;

import com.google.common.base.MoreObjects;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class HomingArrowEntity extends ShulkerBulletEntity {
    public Entity target;

    public HomingArrowEntity(EntityType<ShulkerBulletEntity> entityType, World world) {
        super(entityType, world);
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
    }

    @Override
    public void checkDespawn() {
    }

    @Override
    public void tick() {
        super.tick();

        // Ensure target is valid and alive
        if (this.target != null && this.target.isAlive()) {
            // Get the current position of the arrow and the target
            Vec3d currentPos = this.getPos();
            Vec3d targetPos = this.target.getPos().add(0, this.target.getStandingEyeHeight() * 0.5, 0); // Use the target's eye height for aiming

            // Calculate the direction vector to the target
            Vec3d direction = targetPos.subtract(currentPos).normalize();

            // Adjust the velocity to move the arrow towards the target with some waviness
            double speed = 0.5; // Adjust speed as necessary
            this.setVelocity(direction.multiply(speed)); // Update velocity with the random direction

            // Ensure the arrow does not fall due to gravity
            this.setNoGravity(true);
        }

        // If the target is invalid or dead, remove the arrow
        if (this.target == null || !this.target.isAlive()) {
            ((ServerWorld)this.getWorld()).spawnParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 2, 0.2, 0.2, 0.2, 0.0);
            this.playSound(SoundEvents.ENTITY_SHULKER_BULLET_HIT, 1.0F, 1.0F);
            this.discard();
        }
    }

    private static BlockHitResult RayCastBlocks (Entity entity, World world, int maxDistance) {
        Vec3d startPos = entity.getCameraPosVec(1.0F);
        Vec3d direction = entity.getRotationVec(1.0F).multiply(maxDistance);
        Vec3d endPos = startPos.add(direction);
        RaycastContext raycastContext = new RaycastContext(
                startPos,                            // Start position
                endPos,                              // End position
                RaycastContext.ShapeType.OUTLINE,    // Interaction type (blocks' outline)
                RaycastContext.FluidHandling.ANY, // Only interact with fluid source blocks
                entity                            // Entity to ignore in raycast (the player)
        );
        return world.raycast(raycastContext);
    }


}
