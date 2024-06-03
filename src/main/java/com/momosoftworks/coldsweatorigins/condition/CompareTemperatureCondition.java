package com.momosoftworks.coldsweatorigins.condition;

import com.momosoftworks.coldsweat.api.util.Temperature;
import com.momosoftworks.coldsweatorigins.condition_config.CompareTemperatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class CompareTemperatureCondition extends EntityCondition<CompareTemperatureConfiguration>
{
    public CompareTemperatureCondition()
    {   super(CompareTemperatureConfiguration.CODEC);
    }

    public boolean check(CompareTemperatureConfiguration configuration, Entity entity)
    {   if (!(entity instanceof LivingEntity living))
        {   return false;
        }
        return configuration.check((float) Temperature.get(living, configuration.trait()));
    }
}
