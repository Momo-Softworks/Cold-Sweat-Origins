package com.momosoftworks.coldsweatorigins.event.common;

import com.momosoftworks.coldsweat.api.util.Temperature;
import com.momosoftworks.coldsweat.common.event.capability.EntityTempManager;
import com.momosoftworks.coldsweatorigins.ColdSweatOrigins;
import com.momosoftworks.coldsweatorigins.parsing.MappedAttribute;
import com.momosoftworks.coldsweatorigins.parsing.AssignedAttributeModifier;
import com.momosoftworks.coldsweatorigins.parsing.OriginModifier;
import io.github.edwinmindcraft.origins.api.OriginsAPI;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

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
                player.getCapability(OriginsAPI.ORIGIN_CONTAINER).ifPresent(origins ->
                {
                    origins.getOrigins().forEach((layer, origin) ->
                    {
                        OriginModifier.ORIGIN_SETTINGS.computeIfPresent(origin.location(), (org, settings) ->
                        {
                            ColdSweatOrigins.LOGGER.info("Applying origin settings for %s to %s", origin.location(), player.getName().getString());
                            if (!player.getPersistentData().getString("Origin").equals(origin.location().toString()))
                            {
                                for (Temperature.Trait trait : EntityTempManager.VALID_ATTRIBUTE_TYPES)
                                {   AttributeInstance attribute = EntityTempManager.getAttribute(trait, player);
                                    temp.clearPersistentAttribute(attribute.getAttribute());
                                    attribute.removeModifiers();
                                    attribute.setBaseValue(attribute.getAttribute().getDefaultValue());
                                }
                            }
                            else return settings;

                            for (MappedAttribute settingAttribute : settings.baseValues())
                            {
                                AttributeInstance attribute = player.getAttribute(settingAttribute.attribute());
                                if (attribute != null)
                                {   attribute.setBaseValue(settingAttribute.value());
                                    temp.markPersistentAttribute(settingAttribute.attribute());
                                }
                            }
                            for (AssignedAttributeModifier modifier : settings.modifiers())
                            {
                                AttributeInstance attribute = player.getAttribute(ForgeRegistries.ATTRIBUTES.getValue(modifier.getAttribute()));
                                if (attribute != null)
                                {   attribute.addPermanentModifier(modifier);
                                }
                                temp.markPersistentAttribute(attribute.getAttribute());
                            }

                            player.getPersistentData().putString("Origin", origin.location().toString());

                            return settings;
                        });
                    });
                });
            });
        }
    }
}
