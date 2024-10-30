package com.momosoftworks.coldsweatorigins.event.common;

import com.momosoftworks.coldsweat.api.event.common.temperautre.TempModifierEvent;
import com.momosoftworks.coldsweat.api.registry.TempModifierRegistry;
import com.momosoftworks.coldsweatorigins.parsing.OriginModifier;
import io.github.edwinmindcraft.origins.api.OriginsAPI;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TempModifierImmunity
{
    @SubscribeEvent
    public static void checkTempModifierImmunity(TempModifierEvent.Add event)
    {
        event.getEntity().getCapability(OriginsAPI.ORIGIN_CONTAINER).ifPresent(origins ->
        {
            origins.getOrigins().forEach((layer, origin) ->
            {
                OriginModifier.ORIGIN_SETTINGS.computeIfPresent(origin.location(), (org, settings) ->
                {
                    if (settings.immuneTempModifiers().contains(TempModifierRegistry.getKey(event.getModifier())))
                    {   event.setCanceled(true);
                    }
                    return settings;
                });
            });
        });
    }
}
