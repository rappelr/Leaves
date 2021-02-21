package com.rappelr.leaves.map;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;

import net.minecraft.server.v1_16_R3.IBlockData;

public class LeafTypes {
	
	private final HashMap<Material, IBlockData> types;
	
	{
		types = new HashMap<Material, IBlockData>();
		put(Material.OAK_LEAVES);
		put(Material.BIRCH_LEAVES);
		put(Material.SPRUCE_LEAVES);
		put(Material.JUNGLE_LEAVES);
		put(Material.ACACIA_LEAVES);
		put(Material.DARK_OAK_LEAVES);
	}
	
	public IBlockData data(Material material) {
		for(Map.Entry<Material, IBlockData> e : types.entrySet())
			if(e.getKey().equals(material))
				return e.getValue();
		return null;
	}
	
	private void put(Material material) {
		types.put(material, CraftMagicNumbers.getBlock(Material.OAK_LEAVES).getBlockData());
	}

}
