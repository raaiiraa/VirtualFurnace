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
    private final VFurnace instance;

    private final FurnaceView furnace;

    private VFurnaceManager furnaceManager;

    private Player p;

    private ItemStack fuel;
    private ItemStack in;
    private ItemStack out;

    private int burnTime;

    private long timeClose;
    private long timeOpen;

    private int taskSmelt;

    private boolean burning;


    public VFurnace(Player holder)
    {
        this.furnace = MenuType.FURNACE.create(holder, "Virtual Furnace");
        this.p = holder;
        burning = false;

        furnaceManager = Main.getInstance().getvFurnaceManager();

        instance = this;
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
        Material fuelType = furnace.getItem(1).getType();
        Material inType = furnace.getItem(0).getType();
        Material outType = furnace.getItem(2).getType();

        Bukkit.broadcastMessage(e.getSlotType().toString());

        if(e.getSlotType() == InventoryType.SlotType.QUICKBAR ||
        e.getSlotType() == InventoryType.SlotType.CONTAINER)
        {
            if(e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY)
            {
                Material currentItemType = e.getCurrentItem().getType();

                // Use CurrentItem for this
                if(currentItemType != Material.AIR)
                {
                    if(fuelType != Material.AIR || inType != Material.AIR)
                    {
                        return true;
                    }
                }
                return false;
            }

            return false;
        }

        Material cursorType = e.getCursor().getType();

        if(fuelType != Material.AIR)
        {
            if(cursorType != Material.AIR)
            {
                return true;
            }
        }else if(inType!= Material.AIR && cursorType != Material.AIR)
        {
            return true;
        }

        return false;
    }

    private Fuel getFuel()
    {
        return Fuel.valueOf(furnace.getItem(1).getType().toString());
    }

    private void burn(InventoryClickEvent e)
    {
        if(checkSmelt(e))
        {
            int ticks = 0;
            burning = true;
            // Animation
            int taskBurn = Main.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable()
            {
                @Override
                public void run()
                {
                    furnace.setBurnTime((int) Fuel.COAL.getBurnTicks()-ticks, Fuel.COAL.getBurnTicks());
                }
            },0L, 10L);

            // Schedule task for end -> Delay = burnTicks of item
            int taskFuel = Main.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable()
            {
                @Override
                public void run()
                {
                    furnace.getItem(1).setAmount(furnace.getItem(1).getAmount()-1);
                    Main.getInstance().getServer().getScheduler().cancelTask(taskBurn);
                    burn(e);
                }
            }, Fuel.COAL.getBurnTicks());
        }else
        {
            burning = false;
        }
    }

    private void smelt(InventoryClickEvent e)
    {
        if(checkSmelt(e))
        {
            final int[] cook = {0};
            // Smelting animation.
            int taskSmelt = Main.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable()
            {
                @Override
                public void run()
                {
                    if(burning)
                    {
                        int taskSmelt = Main.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                furnace.setCookTime(cook[0], 200);
                                cook[0] = cook[0] + 10;
                            }
                        },0L,10L);
                    }else
                    {
                        furnace.setCookTime(0,0);
                    }
                }
            }, 0L, 10L);

            int taskOut = Main.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable()
            {
                @Override
                public void run()
                {
                    Result r = Result.getByMaterial(in.getType());

                    if(out == null || out.getType() == Material.AIR)
                    {
                        out = new ItemStack(r.getResult());
                    }else
                    {
                        out.setAmount(out.getAmount()+1);
                    }

                    furnace.getItem(0).setAmount(furnace.getItem(0).getAmount()-1);

                    furnace.setItem(2,out);

                    smelt(e);
                }
            },100L);
        }
    }

    //TODO: Rewrite this piece of shit method. Create two methods, burn and smelt or something like that.
    private void smeltTrash(ItemStack nItem)
    {
        if(nItem.getType().isFuel())
        {
            this.fuel = nItem;
            this.in = furnace.getItem(0);
        }else
        {
            this.fuel = furnace.getItem(1);
            this.in = nItem;
        }
        int times[] = {0, Float.floatToIntBits(furnace.getCookTime())};
        final boolean[] has = new boolean[2];
        taskSmelt = Main.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            @Override
            public void run()
            {
                //TODO: Cancel if fuel or input runs out.

                if(furnace.getCookTime() == 1)
                {
                    if(furnace.getItem(2).getType() == Material.AIR)
                    {
                        if(has[0])
                        {
                            furnace.setItem(2, new ItemStack(Material.IRON_INGOT));
                            Main.getInstance().getServer().getPluginManager().callEvent(new VFurnaceSmeltEvent(Main.getInstance().getvFurnaceManager().getFurnaces().get(p.getUniqueId())));
                        }
                    }else
                    {
                        if(has[0])
                        {
                            furnace.getItem(2).setAmount(furnace.getItem(2).getAmount()+1);
                            Main.getInstance().getServer().getPluginManager().callEvent(new VFurnaceSmeltEvent(Main.getInstance().getvFurnaceManager().getFurnaces().get(p.getUniqueId())));
                        }
                    }

                    furnace.getItem(0).setAmount(furnace.getItem(0).getAmount()-1);
                    times[0]=0;

                    if(furnace.getItem(0).getType() != Material.AIR)
                    {
                        has[0] = true;
                    }else
                    {
                        has[0] = false;
                    }
                }

                if(furnace.getBurnTime() == 0)
                {
                    fuel.setAmount(fuel.getAmount()-1);
                    furnace.setItem(1, fuel);
                    times[1]=0;
                    if(furnace.getItem(1).getType() != Material.AIR)
                    {
                        has[1] = true;
                    }else
                    {
                        has[1] = false;
                    }
                }

                furnace.setBurnTime((int) Fuel.COAL.getBurnTicks()-times[1], Fuel.COAL.getBurnTicks());
                furnace.setCookTime(times[0], 200);
                if(has[0])
                {
                    times[0] = times[0] + 10;
                }

                if(has[1])
                {
                    times[1] = times[1] + 10;
                }
            }
        }, 0L, 10L);
        return;
    }

    public int getTaskSmelt()
    {
        return taskSmelt;
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
        if(e.getAction() == InventoryAction.PLACE_ALL
        || e.getAction() == InventoryAction.PLACE_ONE
        || e.getAction() == InventoryAction.PLACE_SOME
        || e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY)
        {
            if(checkSmelt(e))
            {
                burn(e);
                smelt(e);
            }
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