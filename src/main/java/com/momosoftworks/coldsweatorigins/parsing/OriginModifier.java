package com.momosoftworks.coldsweatorigins.parsing;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public record OriginModifier(ResourceLocation origin, List<MappedAttribute> baseValues, List<AssignedAttributeModifier> modifiers, List<ResourceLocation> immuneTempModifiers)
{
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
            ATTRIBUTE_CODEC.listOf().optionalFieldOf("attributes", new ArrayList<>()).forGetter(OriginModifier::baseValues),
            MODIFIER_CODEC.listOf().optionalFieldOf("modifiers", new ArrayList<>()).forGetter(OriginModifier::modifiers),
            ResourceLocation.CODEC.listOf().optionalFieldOf("immune_temp_modifiers", new ArrayList<>()).forGetter(OriginModifier::immuneTempModifiers)
    ).apply(instance, OriginModifier::new));
}
