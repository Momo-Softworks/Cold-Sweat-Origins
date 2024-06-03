package com.momosoftworks.coldsweatorigins.parsing;

import com.mojang.serialization.JsonOps;
import com.momosoftworks.coldsweat.ColdSweat;
import com.momosoftworks.coldsweatorigins.ColdSweatOrigins;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.FileReader;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class OriginAttributesParser
{
    public static final ResourceKey<Registry<OriginModifier>> ORIGIN_SETTING_REGISTRY = ResourceKey.createRegistryKey(new ResourceLocation(ColdSweat.MOD_ID, "origin_modifier"));

    /**
     * Iterates through the files located in Cold Sweat's data/origin_settings directory and parses them into OriginSettings objects.
     */
    public static void parseAllSettings(MinecraftServer server)
    {
        RegistryAccess registries = server.registryAccess();

        final Map<ResourceLocation, OriginModifier> originSettings = registries.registryOrThrow(ORIGIN_SETTING_REGISTRY)
                .holders()
                .map(holder ->
                {
                    OriginModifier settings = holder.get();
                    ResourceLocation location = holder.get().origin();
                    return Map.entry(location, settings);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Path configPath = FMLPaths.CONFIGDIR.get();
        Path originSettingsPath = configPath.resolve("coldsweat/data/origin_settings");
        try
        {
            for (String file : originSettingsPath.toFile().list())
            {
                if (file.endsWith(".json"))
                {
                    try (FileReader reader = new FileReader(originSettingsPath.resolve(file).toFile()))
                    {
                        OriginModifier.CODEC.parse(JsonOps.INSTANCE, GsonHelper.parse(reader))
                                .resultOrPartial(ColdSweatOrigins.LOGGER::error)
                                .ifPresent(modifier -> originSettings.put(modifier.origin(), modifier));
                    }
                    catch (Exception e)
                    {   ColdSweatOrigins.LOGGER.error("Failed to parse origin settings: " + e);
                    }
                }
            }
        }
        catch (Exception e)
        {
            ColdSweatOrigins.LOGGER.error("Failed to parse origin settings: " + e.getMessage());
        }

        ColdSweatOrigins.LOGGER.info("Parsed origin settings: " + originSettings);

        OriginModifier.ORIGIN_SETTINGS.putAll(originSettings);
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event)
    {   parseAllSettings(event.getServer());
    }
}
