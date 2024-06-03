package com.momosoftworks.coldsweatorigins;

import com.momosoftworks.coldsweatorigins.parsing.OriginAttributesParser;
import com.momosoftworks.coldsweatorigins.parsing.OriginModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DataPackRegistryEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ColdSweatOrigins.MOD_ID)
public class ColdSweatOrigins {

    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "cold_sweat_origins";
    public static final Logger LOGGER = LogManager.getFormatterLogger("Cold Sweat Origins");

    public ColdSweatOrigins()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener((DataPackRegistryEvent.NewRegistry event) -> {
            event.dataPackRegistry(OriginAttributesParser.ORIGIN_SETTING_REGISTRY, OriginModifier.CODEC);
        });

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
