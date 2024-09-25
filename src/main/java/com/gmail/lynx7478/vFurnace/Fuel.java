package com.gmail.lynx7478.vFurnace;

import org.bukkit.Material;

public enum Fuel
{
    //TODO: Finish enum.
    COAL(Material.COAL, 1600);

    private Material type;
    private int burnTicks;

    Fuel(Material type, int burnTicks)
    {
        this.type = type;
        this.burnTicks = burnTicks;
    }

    public Material getType()
    {
        return type;
    }

    public int getBurnTicks()
    {
        return burnTicks;
    }
}
