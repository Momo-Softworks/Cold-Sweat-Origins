package com.momosoftworks.coldsweatorigins;

import com.momosoftworks.coldsweat.ColdSweat;
import com.momosoftworks.coldsweatorigins.parsing.OriginModifier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DataPackRegistryEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

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
    public static final ResourceKey<Registry<OriginModifier>> ORIGIN_SETTING_REGISTRY = ResourceKey.createRegistryKey(new ResourceLocation(ColdSweat.MOD_ID, "origin_modifier"));
    /**
     * Map of origin settings for each origin
     */
    public static final Map<ResourceLocation, OriginModifier> ORIGIN_SETTINGS = new HashMap<>();

    public ColdSweatOrigins()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener((DataPackRegistryEvent.NewRegistry event) ->
        {
            event.dataPackRegistry(ORIGIN_SETTING_REGISTRY, OriginModifier.CODEC);
        });

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
