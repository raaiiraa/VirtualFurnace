package com.gmail.lynx7478.vFurnace;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;

    private GUIManager guiManager;
    private VFurnaceManager vFurnaceManager;

    @Override
    public void onEnable()
    {
        vFurnaceManager = new VFurnaceManager();
        this.getServer().getPluginManager().registerEvents(vFurnaceManager, this);

        guiManager = new GUIManager();
        this.getServer().getPluginManager().registerEvents(new GUIListener(guiManager), this);

        this.instance = this;
    }

    @Override
    public void onDisable()
    {

    }

    public static Main getInstance()
    {
        return instance;
    }

    public GUIManager getGuiManager()
    {
        return guiManager;
    }

    public VFurnaceManager getvFurnaceManager()
    {
        return vFurnaceManager;
    }
}
