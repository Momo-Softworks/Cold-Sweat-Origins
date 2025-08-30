package com.momosoftworks.coldsweatorigins.parsing;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.momosoftworks.coldsweat.data.codec.impl.ConfigData;
import com.momosoftworks.coldsweat.data.codec.util.NegatableList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.*;

public class OriginModifier extends ConfigData
{
    private final ResourceLocation origin;
    private final List<MappedAttribute> baseValues;
    private final List<AssignedAttributeModifier> modifiers;
    private final List<ResourceLocation> immuneTempModifiers;

    public OriginModifier(ResourceLocation origin, List<MappedAttribute> baseValues, List<AssignedAttributeModifier> modifiers, List<ResourceLocation> immuneTempModifiers, NegatableList<String> requiredMods)
    {
        super(requiredMods);
        this.origin = origin;
        this.baseValues = baseValues;
        this.modifiers = modifiers;
        this.immuneTempModifiers = immuneTempModifiers;
    }

    public OriginModifier(ResourceLocation origin, List<MappedAttribute> baseValues, List<AssignedAttributeModifier> modifiers, List<ResourceLocation> immuneTempModifiers)
    {   this(origin, baseValues, modifiers, immuneTempModifiers, new NegatableList<>());
    }

    public static final Codec<AssignedAttributeModifier> MODIFIER_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(AssignedAttributeModifier::getAttribute),
            Codec.STRING.fieldOf("operation").forGetter(mod -> mod.getOperation().name()),
            Codec.DOUBLE.fieldOf("value").forGetter(AttributeModifier::getAmount)
    ).apply(instance, (id, operation, value) -> new AssignedAttributeModifier(id, "Origins Attribute", value, AttributeModifier.Operation.valueOf(operation.toUpperCase(Locale.ROOT)))));

    public static final Codec<OriginModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("origin").forGetter(OriginModifier::origin),
            MappedAttribute.CODEC.listOf().optionalFieldOf("attributes", List.of()).forGetter(OriginModifier::baseValues),
            MODIFIER_CODEC.listOf().optionalFieldOf("modifiers", List.of()).forGetter(OriginModifier::modifiers),
            ResourceLocation.CODEC.listOf().optionalFieldOf("immune_temp_modifiers", List.of()).forGetter(OriginModifier::immuneTempModifiers),
            NegatableList.listCodec(Codec.STRING).optionalFieldOf("required_mods", new NegatableList<>()).forGetter(OriginModifier::requiredMods)
    ).apply(instance, OriginModifier::new));

    public ResourceLocation origin()
    {   return origin;
    }
    public List<MappedAttribute> baseValues()
    {   return baseValues;
    }
    public List<AssignedAttributeModifier> modifiers()
    {   return modifiers;
    }
    public List<ResourceLocation> immuneTempModifiers()
    {   return immuneTempModifiers;
    }

    @Override
    public Codec<? extends ConfigData> getCodec()
    {   return CODEC;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        OriginModifier that = (OriginModifier) obj;
        return Objects.equals(origin, that.origin)
            && Objects.equals(baseValues, that.baseValues)
            && Objects.equals(modifiers, that.modifiers)
            && Objects.equals(immuneTempModifiers, that.immuneTempModifiers);
    }
}
