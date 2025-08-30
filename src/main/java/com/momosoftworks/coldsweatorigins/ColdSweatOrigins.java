package com.momosoftworks.coldsweatorigins;

import com.google.common.collect.Multimap;
import com.momosoftworks.coldsweat.ColdSweat;
import com.momosoftworks.coldsweat.api.event.core.registry.AddRegistriesEvent;
import com.momosoftworks.coldsweat.data.RegistryHolder;
import com.momosoftworks.coldsweat.util.math.RegistryMultiMap;
import com.momosoftworks.coldsweatorigins.parsing.OriginModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ColdSweatOrigins.MOD_ID)
@Mod.EventBusSubscriber
public class ColdSweatOrigins
{
    public static final String MOD_ID = "cold_sweat_origins";
    public static final Logger LOGGER = LogManager.getFormatterLogger("Cold Sweat Origins");

    /**
     * Registry key for Cold Sweat origin modifiers
     */
    public static RegistryHolder<OriginModifier> ORIGIN_SETTING_REGISTRY;
    /**
     * Map of origin settings for each origin
     */
    public static final Multimap<ResourceLocation, OriginModifier> ORIGIN_SETTINGS = new RegistryMultiMap<>();

    public ColdSweatOrigins()
    {
    }

    @SubscribeEvent
    public static void onInit(AddRegistriesEvent event)
    {   ORIGIN_SETTING_REGISTRY = event.createRegistry(new ResourceLocation(ColdSweat.MOD_ID, "origin_modifier"), OriginModifier.CODEC, OriginModifier.class);
    }
}
