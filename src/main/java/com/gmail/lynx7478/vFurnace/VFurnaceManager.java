package com.gmail.lynx7478.vFurnace;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class VFurnaceManager implements Listener
{
    private HashMap<UUID, VFurnace> furnaces;

    // Unneeded, furnace inventory already limits the items placeable in fuel slot.
    // private HashMap<Material, Integer> fuels;

    private HashMap<Material, Material> results;

    public VFurnaceManager()
    {
        this.furnaces =  new HashMap<UUID, VFurnace>();

        // fuels = new HashMap<Material, Integer>();

        results = new HashMap<Material, Material>();

        results.put(Material.IRON_ORE, Material.IRON_INGOT);

        // Add fuels.
        // fuels.put(Material.COAL, 8*20);
        // fuels.put(Material.CHARCOAL, 8*20);

        for(Material m : Material.values())
        {
            String mName = m.toString();
            // Wood and logs.
            //if (mName.contains("_WOOD")|| mName.contains("_LOG")) {
            //    fuels.put(m, 15 * 20);
            // }

            /* if(mName.contains("_ORE"))
            {
                String mNameEdit = mName.replace("_ORE","_INGOT");
                Material mEdit = Material.getMaterial(mNameEdit);
                results.put(m, mEdit);
                //TODO Debug
                System.out.println(mName);
                System.out.println(mNameEdit);
            } */

        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if(e.getClickedBlock().getType() == Material.BLAST_FURNACE)
            {
                if(!furnaces.containsKey(p.getUniqueId()))
                {
                    furnaces.put(e.getPlayer().getUniqueId(), new VFurnace(e.getPlayer()));
                }
                VFurnace inv = furnaces.get(p.getUniqueId());
                Main.getInstance().getGuiManager().registerInventory(inv.getFurnace().getTopInventory(), inv);
                inv.openInventory();

                e.setCancelled(true);
            }
        }
    }

    private long firstSmelt;
    private long lastSmelt;
    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent e)
    {
        if(firstSmelt == 0)
        {
            firstSmelt = System.currentTimeMillis();
        }else
        {
            lastSmelt = System.currentTimeMillis();

            long smeltTime = lastSmelt-firstSmelt;

            long smeltTicks = Math.floorDiv(smeltTime, 50);
            Bukkit.broadcastMessage("Ticks to smelt: "+smeltTicks);
        }

    }

    @EventHandler
    public void onVFurnaceSmelt(VFurnaceSmeltEvent e)
    {
        VFurnace furnace = e.getFurnace();
        Bukkit.broadcastMessage("Canceling smelt");
        Main.getInstance().getServer().getScheduler().cancelTask(furnace.getTaskSmelt());
    }

    public HashMap<UUID, VFurnace> getFurnaces()
    {
        return furnaces;
    }

    public HashMap<Material, Material> getResults()
    {
        return results;
    }
}
