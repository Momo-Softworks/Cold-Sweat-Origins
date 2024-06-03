package com.momosoftworks.coldsweatorigins.condition_config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.momosoftworks.coldsweat.api.util.Temperature;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.Comparison;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;

public record CompareTemperatureConfiguration(Temperature.Trait trait, Comparison comparison, float compareTo) implements IDynamicFeatureConfiguration
{
    public static final Codec<CompareTemperatureConfiguration> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Temperature.Trait.CODEC.fieldOf("temperature_type").forGetter(CompareTemperatureConfiguration::trait),
                              ApoliDataTypes.COMPARISON.fieldOf("comparison").forGetter(CompareTemperatureConfiguration::comparison),
                              Codec.FLOAT.fieldOf("compare_to").forGetter(CompareTemperatureConfiguration::compareTo)).apply(instance, CompareTemperatureConfiguration::new);
    });

    public CompareTemperatureConfiguration(Temperature.Trait trait, Comparison comparison, float compareTo)
    {   this.trait = trait;
        this.comparison = comparison;
        this.compareTo = compareTo;
    }

    public boolean check(float value)
    {   return this.comparison().compare(value, this.compareTo());
    }

    public Comparison comparison()
    {   return this.comparison;
    }

    public float compareTo()
    {   return this.compareTo;
    }
}
