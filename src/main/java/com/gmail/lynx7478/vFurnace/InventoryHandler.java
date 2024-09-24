package com.gmail.lynx7478.vFurnace;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public interface InventoryHandler
{
    void onOpen(InventoryOpenEvent e);

    void onClick(InventoryClickEvent e);

    void onClose(InventoryCloseEvent e);
}
