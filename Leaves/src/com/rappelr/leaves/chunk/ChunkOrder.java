package com.rappelr.leaves.chunk;

import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChunkOrder {
	
	@Getter
	private final World world;
	
	@Getter
	private final int x, z;
	
	@Getter
	private final List<Player> players;

}
