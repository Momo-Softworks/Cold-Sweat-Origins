package com.momosoftworks.coldsweatorigins.parsing;

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
}
