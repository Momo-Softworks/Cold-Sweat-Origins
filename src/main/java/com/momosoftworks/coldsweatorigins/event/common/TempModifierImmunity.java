package com.momosoftworks.coldsweatorigins.event.common;

import com.momosoftworks.coldsweat.api.event.common.temperautre.TempModifierEvent;
import com.momosoftworks.coldsweat.api.registry.TempModifierRegistry;
import com.momosoftworks.coldsweatorigins.ColdSweatOrigins;
import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber
public class TempModifierImmunity
{
    public static final Map<Entity, LazyOptional<IOriginContainer>> ORIGIN_CONTAINERS = new WeakHashMap<>();

    public static LazyOptional<IOriginContainer> getOriginContainer(Entity entity)
    {   return ORIGIN_CONTAINERS.computeIfAbsent(entity, e -> e.getCapability(OriginsAPI.ORIGIN_CONTAINER));
    }

    @SubscribeEvent
    public static void checkBeforeAdd(TempModifierEvent.Add event)
    {
        getOriginContainer(event.getEntity()).ifPresent(origins ->
        {
            origins.getOrigins().forEach((layer, origin) ->
            {
                ColdSweatOrigins.ORIGIN_SETTINGS.computeIfPresent(origin.location(), (org, settings) ->
                {
                    if (settings.immuneTempModifiers().contains(TempModifierRegistry.getKey(event.getModifier())))
                    {   event.setCanceled(true);
                    }
                    return settings;
                });
            });
        });
    }

    @SubscribeEvent
    public static void checkBeforeCalculate(TempModifierEvent.Calculate.Pre event)
    {
        getOriginContainer(event.getEntity()).ifPresent(origins ->
        {
            origins.getOrigins().forEach((layer, origin) ->
            {
                ColdSweatOrigins.ORIGIN_SETTINGS.computeIfPresent(origin.location(), (org, settings) ->
                {
                    if (settings.immuneTempModifiers().contains(TempModifierRegistry.getKey(event.getModifier())))
                    {   event.getModifier().expires(0);
                        event.setCanceled(true);
                    }
                    return settings;
                });
            });
        });
    }
}
