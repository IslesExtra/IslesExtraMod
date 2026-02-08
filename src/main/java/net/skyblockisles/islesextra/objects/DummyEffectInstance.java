package net.skyblockisles.islesextra.objects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class DummyEffectInstance extends StatusEffectInstance {

    private Identifier iconID;
    private String firstValue;
    private String secondValue;

    public DummyEffectInstance(Identifier identifier, Object value) {
        super((RegistryEntry<StatusEffect>) null);
        this.iconID = identifier;
        this.firstValue = String.valueOf(value);
        this.secondValue = null;
    }

    public DummyEffectInstance(Identifier identifier, Object first, Object second) {
        super((RegistryEntry<StatusEffect>) null);
        this.iconID = identifier;
        this.firstValue = String.valueOf(first);
        this.secondValue = String.valueOf(second);
    }

    public Identifier getIconID() {
        return iconID;
    }

    public void setIconID(Identifier iconID) {
        this.iconID = iconID;
    }

    public String getValue() {
        return firstValue;
    }

    public void setValue(String value) {
        this.firstValue = value;
    }

    public String getSecondValue() {
        return secondValue;
    }

    public void setSecondValue(String value) {
        this.secondValue = value;
    }
}