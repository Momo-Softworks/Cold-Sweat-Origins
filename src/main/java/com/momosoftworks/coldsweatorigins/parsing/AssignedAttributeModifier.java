package com.momosoftworks.coldsweatorigins.parsing;

import com.momosoftworks.coldsweat.data.codec.configuration.BlockTempData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class AssignedAttributeModifier extends AttributeModifier
{
    private final ResourceLocation attribute;

    public AssignedAttributeModifier(ResourceLocation attribute, String name, double value, Operation operation)
    {   super(name, value, operation);
        this.attribute = attribute;
    }

    public ResourceLocation getAttribute()
    {   return attribute;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        AssignedAttributeModifier that = (AssignedAttributeModifier) obj;
        return super.equals(obj)
            && attribute.equals(that.attribute);
    }
}
