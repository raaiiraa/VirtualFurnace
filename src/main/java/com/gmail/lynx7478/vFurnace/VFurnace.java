package com.gmail.lynx7478.vFurnace;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
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

    private boolean smelting;


    public VFurnace(Player holder)
    {
        this.furnace = MenuType.FURNACE.create(holder, "Virtual Furnace");
        this.p = holder;
        smelting = false;
    }

    public void openInventory()
    {
        p.openInventory(furnace);
    }

    public FurnaceView getFurnace()
    {
        return furnace;
    }

    // Slot 1: FUEL
    // Slot 0: IN
    // Slot 2: OUT
    private boolean checkSmelt(InventoryClickEvent e)
    {
        Bukkit.broadcastMessage(e.getSlotType().toString());
        if(e.getSlotType() == InventoryType.SlotType.QUICKBAR ||
        e.getSlotType() == InventoryType.SlotType.CONTAINER)
        {
            Bukkit.broadcastMessage("Not furnace");
            return false;
        }
        ItemStack cursor = e.getCursor();

        if(furnace.getItem(1).getType() != Material.AIR)
        {
            Bukkit.broadcastMessage("Slot 1 not AIR");
            if(cursor.getType() != Material.AIR)
            {
                Bukkit.broadcastMessage("And cursor is not air");
                return true;
            }
        }else if(furnace.getItem(0).getType() != Material.AIR && cursor.getType() != Material.AIR)
        {
            return true;
        }

        return false;
    }

    private void smelt()
    {
        return;
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
        Bukkit.broadcastMessage(e.getAction().toString());
        if(e.getAction() == InventoryAction.PLACE_ALL
        || e.getAction() == InventoryAction.PLACE_ONE
        || e.getAction() == InventoryAction.PLACE_SOME)
        {
            Bukkit.broadcastMessage("e");
            Bukkit.broadcastMessage(e.getCursor().getType().toString());
            Bukkit.broadcastMessage(String.valueOf(checkSmelt(e)));
        }

        //TODO: Handle picking up. Essentially cancel the smelting.
        // Might have to also consider swapping of fuels/inputs.
    }

    @Override
    public void onClose(InventoryCloseEvent e)
    {
        Bukkit.getServer().getScheduler().cancelTask(taskSmelt);
        timeClose=System.currentTimeMillis();
    }


}