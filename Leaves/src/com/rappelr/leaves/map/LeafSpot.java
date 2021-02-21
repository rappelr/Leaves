package com.rappelr.leaves.map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.v1_16_R3.IBlockData;

@AllArgsConstructor
public class LeafSpot {
	
	@Getter
	private final int x, y, z;
	
	@Getter
	private IBlockData data;
	
}
