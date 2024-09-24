package com.gmail.lynx7478.vFurnace;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.view.FurnaceView;

public class VFurnace implements InventoryHandler {

    // Slot 1: FUEL
    // Slot 0: IN
    // Slot 2: OUT
    private final FurnaceView furnace;

    private Player p;

    private ItemStack fuel;
    private ItemStack in;
    private ItemStack out;

    private int burnTime;

    private long timeClose;
    private long timeOpen;

    private int taskSmelt;


    public VFurnace(Player holder)
    {
        this.furnace = MenuType.FURNACE.create(holder, "Virtual Furnace");
        this.p = holder;
    }

    private boolean checkSmelt()
    {

        return true;
    }

    public void openInventory()
    {
        p.openInventory(furnace);
    }

    public FurnaceView getFurnace()
    {
        return furnace;
    }

    @Override
    public void onOpen(InventoryOpenEvent e)
    {
        if(timeClose == 0)
        {
            return;
        }

        timeOpen = System.currentTimeMillis();

        long time = timeOpen-timeClose;
        Bukkit.broadcastMessage("Milis: "+String.valueOf(time));

        long ticks = Math.floorDiv(time, 50);
        Bukkit.broadcastMessage("Ticks: "+String.valueOf(ticks));

    }

    @Override
    public void onClick(InventoryClickEvent e)
    {
        Player p = (Player) e.getWhoClicked();
    }

    @Override
    public void onClose(InventoryCloseEvent e)
    {
        Bukkit.getServer().getScheduler().cancelTask(taskSmelt);
        timeClose=System.currentTimeMillis();
    }
}