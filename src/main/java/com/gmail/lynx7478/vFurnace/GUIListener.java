package com.gmail.lynx7478.vFurnace;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class GUIListener implements Listener
{

    private GUIManager manager;

    public GUIListener(GUIManager manager)
    {
        this.manager = manager;
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e)
    {
        manager.handleOpen(e);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e)
    {
        Bukkit.broadcastMessage("click");
        manager.handleClick(e);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e)
    {
        manager.handleClose(e);
    }
}
