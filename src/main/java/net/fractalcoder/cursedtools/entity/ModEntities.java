package net.fractalcoder.cursedtools.entity;

import net.fractalcoder.cursedtools.CursedTools;
import net.fractalcoder.cursedtools.entity.custom.HomingArrowEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<ShulkerBulletEntity> HOMING_ARROW = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(CursedTools.MOD_ID, "homing_arrow"),
            EntityType.Builder.create(HomingArrowEntity::new, SpawnGroup.CREATURE)
                    .dimensions(1f, 2.5f).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of("homing_arrow", CursedTools.MOD_ID))));

    public static void registerModEntities() {
        CursedTools.LOGGER.info("Registering Mod Entities for " + CursedTools.MOD_ID);
    }
}
