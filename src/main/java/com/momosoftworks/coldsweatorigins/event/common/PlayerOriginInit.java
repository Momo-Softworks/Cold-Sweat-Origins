package com.momosoftworks.coldsweatorigins.event.common;

import com.momosoftworks.coldsweat.common.capability.handler.EntityTempManager;
import com.momosoftworks.coldsweat.core.event.TaskScheduler;
import com.momosoftworks.coldsweatorigins.ColdSweatOrigins;
import com.momosoftworks.coldsweatorigins.parsing.AssignedAttributeModifier;
import com.momosoftworks.coldsweatorigins.parsing.MappedAttribute;
import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.api.origin.Origin;
import io.github.edwinmindcraft.origins.api.origin.OriginLayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.BiConsumer;

@Mod.EventBusSubscriber
public class PlayerOriginInit
{
    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event)
    {
        if (event.getEntity() instanceof Player player)
        {
            EntityTempManager.getTemperatureCap(player).ifPresent(temp ->
            {
                forEachOrigin(player, (layer, origin) ->
                {
                    ColdSweatOrigins.ORIGIN_SETTINGS.computeIfPresent(origin.location(), (org, settings) ->
                    {
                        TaskScheduler.scheduleServer(() ->
                        {
                            ColdSweatOrigins.LOGGER.info("Applying origin settings for %s to %s", origin.location(), player.getName().getString());
                            for (MappedAttribute settingAttribute : settings.baseValues())
                            {
                                AttributeInstance attribute = player.getAttribute(settingAttribute.attribute());
                                if (attribute != null)
                                {   attribute.setBaseValue(settingAttribute.value());
                                }
                            }
                            for (AssignedAttributeModifier modifier : settings.modifiers())
                            {
                                AttributeInstance attribute = player.getAttribute(ForgeRegistries.ATTRIBUTES.getValue(modifier.getAttribute()));
                                if (attribute != null)
                                {   attribute.addTransientModifier(modifier);
                                }
                            }
                        }, 5);
                        player.getPersistentData().putString("CurrentOrigin", origin.location().toString());

                        return settings;
                    });
                });
            });
        }
    }

    public static void forEachOrigin(Player player, BiConsumer<ResourceKey<OriginLayer>, ResourceKey<Origin>> consumer)
    {
        player.getCapability(OriginsAPI.ORIGIN_CONTAINER).ifPresent(origins -> origins.getOrigins().forEach(consumer));
    }
}
