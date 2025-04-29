package com.momosoftworks.coldsweatorigins.parsing;

import com.momosoftworks.coldsweat.api.event.core.registry.CreateRegistriesEvent;
import com.momosoftworks.coldsweat.util.math.CSMath;
import com.momosoftworks.coldsweatorigins.ColdSweatOrigins;
import net.minecraft.core.Holder;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class OriginAttributesParser
{
    /**
     * Iterates through the files located in Cold Sweat's data/origin_settings directory and parses them into OriginSettings objects.
     */
    @SubscribeEvent
    public static void parseAllSettings(CreateRegistriesEvent.Post event)
    {
        long start = System.nanoTime();

        ColdSweatOrigins.ORIGIN_SETTINGS.clear();
        for (Holder<OriginModifier> holder : event.getRegistry(ColdSweatOrigins.ORIGIN_SETTING_REGISTRY))
        {
            OriginModifier modifier = holder.value();
            ColdSweatOrigins.ORIGIN_SETTINGS.put(modifier.origin(), modifier);
        }

        ColdSweatOrigins.LOGGER.info("Parsed origin settings in %s ms", CSMath.truncate((System.nanoTime() - start) / 1_000_000d, 2));
    }
}
