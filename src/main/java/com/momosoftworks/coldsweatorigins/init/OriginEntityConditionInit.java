package com.momosoftworks.coldsweatorigins.init;

import com.momosoftworks.coldsweat.ColdSweat;
import com.momosoftworks.coldsweatorigins.condition.CompareTemperatureCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import io.github.edwinmindcraft.apoli.common.registry.ApoliRegisters;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

public class OriginEntityConditionInit
{
    public static RegistryObject<EntityCondition<?>> TEMPERATURE_CONDITION;

    public static void register()
    {   TEMPERATURE_CONDITION = ApoliRegisters.ENTITY_CONDITIONS.register(new ResourceLocation(ColdSweat.MOD_ID, "temperature").toString(), CompareTemperatureCondition::new);
    }
}
