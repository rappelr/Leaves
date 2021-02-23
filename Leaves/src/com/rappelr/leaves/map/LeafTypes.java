package com.rappelr.leaves.map;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;

import com.rappelr.leaves.Leaves;

import lombok.val;
import net.minecraft.server.v1_16_R3.IBlockData;

public class LeafTypes {
	
	private final HashMap<Material, IBlockData> types;
	
	{
		types = new HashMap<Material, IBlockData>();
	}
	
	public void reload() {
		types.clear();
		
		types.put(Material.OAK_LEAVES, getIBlockData(Material.OAK_LEAVES));
		types.put(Material.BIRCH_LEAVES, getIBlockData(Material.BIRCH_LEAVES));
		types.put(Material.SPRUCE_LEAVES, getIBlockData(Material.SPRUCE_LEAVES));
		types.put(Material.JUNGLE_LEAVES, getIBlockData(Material.JUNGLE_LEAVES));
		types.put(Material.ACACIA_LEAVES, getIBlockData(Material.ACACIA_LEAVES));
		types.put(Material.DARK_OAK_LEAVES, getIBlockData(Material.DARK_OAK_LEAVES));
		
		val section = Leaves.getInstance().getConfiguration().getSource().getConfigurationSection("colors");
		
		if(section != null) {
			for(String key : section.getKeys(false)) {
				Material leaf = Material.valueOf(key);
				Material color = section.isString(key) ? Material.valueOf(section.getString(key)) : null;
				
				if(leaf == null || !types.containsKey(leaf)) {
					Leaves.getInstance().getLogger().warning("Leaf type \"" + key + "\" does not exist");
					continue;
				}
				
				if(color == null) {
					Leaves.getInstance().getLogger().warning("Material color for leaf type " + leaf.name() + " does not exist");
					continue;
				}
				
				types.replace(leaf, getIBlockData(color));
			}
		}
	}
	
	public IBlockData data(Material material) {
		for(Map.Entry<Material, IBlockData> e : types.entrySet())
			if(e.getKey().equals(material))
				return e.getValue();
		return null;
	}
	
	private IBlockData getIBlockData(Material material) {
		return CraftMagicNumbers.getBlock(material).getBlockData();
	}

}
