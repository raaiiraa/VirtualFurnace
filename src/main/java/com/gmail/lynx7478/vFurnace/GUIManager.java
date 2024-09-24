package com.gmail.lynx7478.vFurnace;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.FurnaceView;

import java.util.HashMap;

public class GUIManager
{

    private final HashMap<Inventory, InventoryHandler> inventories = new HashMap<Inventory, InventoryHandler>();

    public GUIManager()
    {

    }

    public void registerInventory(Inventory inventory, InventoryHandler handler)
    {
        this.inventories.put(inventory, handler);
    }

    public void unregisterInventory(Inventory inventory)
    {
        if(this.inventories.containsKey(inventory))
        {
            this.inventories.remove(inventory);
        }
    }

    public void handleOpen(InventoryOpenEvent e)
    {
        if(this.inventories.containsKey(e.getInventory()))
        {
            this.inventories.get(e.getInventory()).onOpen(e);
        }
    }

    public void handleClick(InventoryClickEvent e)
    {
        if(this.inventories.containsKey(e.getInventory()))
        {
            Bukkit.broadcastMessage("Contains inv");
            this.inventories.get(e.getInventory()).onClick(e);
        }
    }

    public void handleClose(InventoryCloseEvent e)
    {
        if(this.inventories.containsKey(e.getInventory()))
        {
            this.inventories.get(e.getInventory()).onClose(e);
            this.unregisterInventory(e.getInventory());
        }
    }

    public HashMap<Inventory, InventoryHandler> getInventories()
    {
        return inventories;
    }

}
