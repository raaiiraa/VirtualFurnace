package com.gmail.lynx7478.vFurnace;

import org.bukkit.Bukkit;
import org.bukkit.Material;

public enum Result
{

    IRON(Material.IRON_ORE, Material.IRON_INGOT);

    public static Result getByMaterial(String m)
    {
        for(Result r : Result.values())
        {
            Bukkit.broadcastMessage(r.getMaterial().toString());
            if(r.getMaterial().toString() == m)
            {
                Bukkit.broadcastMessage("Found result!");
                return r;
            }
        }

        return null;
    }

    private Material mat;
    private Material result;

    Result(Material mat, Material result)
    {
        this.mat = mat;
        this.result = result;
    }

    public Material getMaterial()
    {
        return mat;
    }

    public Material getResult()
    {
        return result;
    }

}
