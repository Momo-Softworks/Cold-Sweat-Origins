package com.momosoftworks.coldsweatorigins.parsing;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public record OriginModifier(ResourceLocation origin, List<MappedAttribute> baseValues, List<AssignedAttributeModifier> modifiers)
{
    public static final Map<ResourceLocation, OriginModifier> ORIGIN_SETTINGS = new HashMap<>();

    public static final Codec<MappedAttribute> ATTRIBUTE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(attr -> ForgeRegistries.ATTRIBUTES.getKey(attr.attribute())),
            Codec.DOUBLE.fieldOf("value").forGetter(MappedAttribute::value)
    ).apply(instance, (id, value) -> new MappedAttribute(ForgeRegistries.ATTRIBUTES.getValue(id), value)));

    public static final Codec<AssignedAttributeModifier> MODIFIER_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(AssignedAttributeModifier::getAttribute),
            Codec.STRING.fieldOf("operation").forGetter(mod -> mod.getOperation().name()),
            Codec.DOUBLE.fieldOf("value").forGetter(AttributeModifier::getAmount)
    ).apply(instance, (id, operation, value) -> new AssignedAttributeModifier(id, "Origins Attribute", value, AttributeModifier.Operation.valueOf(operation.toUpperCase(Locale.ROOT)))));

    public static final Codec<OriginModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("origin").forGetter(OriginModifier::origin),
            Codec.list(ATTRIBUTE_CODEC).fieldOf("attributes").forGetter(OriginModifier::baseValues),
            Codec.list(MODIFIER_CODEC).fieldOf("modifiers").forGetter(OriginModifier::modifiers)
    ).apply(instance, OriginModifier::new));
}
