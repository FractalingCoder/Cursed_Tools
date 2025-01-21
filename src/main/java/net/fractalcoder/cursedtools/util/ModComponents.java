package net.fractalcoder.cursedtools.util;

import com.mojang.serialization.Codec;
import net.fractalcoder.cursedtools.CursedTools;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModComponents {
    public static final ComponentType<Integer> GREED = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(CursedTools.MOD_ID, "greed"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public static void registerModComponents() {
        CursedTools.LOGGER.info("Registering Components for " + CursedTools.MOD_ID);
    }
}
