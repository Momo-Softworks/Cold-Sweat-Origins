package com.momosoftworks.coldsweatorigins.parsing;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.ForgeRegistries;

public record MappedAttribute(Attribute attribute, double value)
{
    public static final Codec<MappedAttribute> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(attr -> ForgeRegistries.ATTRIBUTES.getKey(attr.attribute())),
            Codec.DOUBLE.fieldOf("value").forGetter(MappedAttribute::value)
    ).apply(instance, (id, value) -> new MappedAttribute(ForgeRegistries.ATTRIBUTES.getValue(id), value)));
}
