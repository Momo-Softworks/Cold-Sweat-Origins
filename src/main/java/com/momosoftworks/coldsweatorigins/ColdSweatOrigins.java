package com.momosoftworks.coldsweatorigins;

import com.google.common.collect.Multimap;
import com.momosoftworks.coldsweat.ColdSweat;
import com.momosoftworks.coldsweat.data.ModRegistries;
import com.momosoftworks.coldsweat.util.math.RegistryMultiMap;
import com.momosoftworks.coldsweatorigins.parsing.OriginModifier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ColdSweatOrigins.MOD_ID)
public class ColdSweatOrigins
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "cold_sweat_origins";
    public static final Logger LOGGER = LogManager.getFormatterLogger("Cold Sweat Origins");

    /**
     * Registry key for Cold Sweat origin modifiers
     */
    public static final ResourceKey<Registry<OriginModifier>> ORIGIN_SETTING_REGISTRY = ModRegistries.createRegistry(ResourceKey.createRegistryKey(new ResourceLocation(ColdSweat.MOD_ID, "origin_modifier")), OriginModifier.CODEC, OriginModifier.class);
    /**
     * Map of origin settings for each origin
     */
    public static final Multimap<ResourceLocation, OriginModifier> ORIGIN_SETTINGS = new RegistryMultiMap<>();

    public ColdSweatOrigins()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
