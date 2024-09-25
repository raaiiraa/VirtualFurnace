package com.gmail.lynx7478.vFurnace;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VFurnaceSmeltEvent extends Event
{
    private static final HandlerList HANDLERS = new HandlerList();

    private VFurnace furnace;

    public VFurnaceSmeltEvent(VFurnace furnace)
    {
        this.furnace = furnace;
    }

    public VFurnace getFurnace()
    {
        return furnace;
    }

    public static HandlerList getHandlerList()
    {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers()
    {
        return HANDLERS;
    }


}
