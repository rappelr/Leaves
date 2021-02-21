package com.rappelr.leaves.map;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.rappelr.leaves.Leaves;
import com.rappelr.leaves.chunk.ChunkOrder;
import com.rappelr.leaves.chunk.LeafChunk;

import lombok.Getter;

public class LeafMap {
	
	private final List<LeafChunk> map;
	
	private long redistributeSpeed = 50l, reprocessSpeed = 50l, replantSpeed = 10l;
	private int redistributeID = -1, reprocessID = -1, replantID = -1;
	
	@Getter
	private final LeafTypes types;
	
	{
		types = new LeafTypes();
		map = new ArrayList<LeafChunk>();
		
		reload();
	}

	@SuppressWarnings("serial")
	public void redistribute() {
		final HashMap<Chunk, List<Player>> chunkPile = new HashMap<Chunk, List<Player>>();
		
		for(Player p : Leaves.getInstance().getServer().getOnlinePlayers()) {
			final Chunk[] surr = getSurroundings(p.getLocation());
			
			for(Chunk c : surr) {
				if(chunkPile.containsKey(c))
					chunkPile.get(c).add(p);
				else
					chunkPile.put(c, new ArrayList<Player>() {{add(p);}});
			}
		}
		
		Bukkit.getScheduler().runTaskAsynchronously(Leaves.getInstance(), 
				() -> aRedistribute(chunkPile));
	}
	
	public Chunk[] getSurroundings(Location l) {
		final Chunk[] result = new Chunk[9];
		final Chunk active = l.getChunk();
		result[0] = active;
		result[1] = l.getWorld().getChunkAt(active.getX() -1, active.getZ() -1);
		result[2] = l.getWorld().getChunkAt(active.getX()   , active.getZ() -1);
		result[3] = l.getWorld().getChunkAt(active.getX() +1, active.getZ() -1);
		result[4] = l.getWorld().getChunkAt(active.getX() -1, active.getZ()   );
		result[5] = l.getWorld().getChunkAt(active.getX() +1, active.getZ()   );
		result[6] = l.getWorld().getChunkAt(active.getX() -1, active.getZ() +1);
		result[7] = l.getWorld().getChunkAt(active.getX()   , active.getZ() +1);
		result[8] = l.getWorld().getChunkAt(active.getX() +1, active.getZ() +1);
		return result;
	}
	
	private synchronized void aRedistribute(final HashMap<Chunk, List<Player>> chunkPile) {
		final List<ChunkOrder> processed = new ArrayList<ChunkOrder>();
		final List<LeafChunk> toRemove = new ArrayList<LeafChunk>();
		
		toRemove.addAll(map);
		chunkPile.entrySet().forEach(e -> processed.add(new ChunkOrder(e.getKey().getWorld(), e.getKey().getX(), e.getKey().getZ(), e.getValue())));
		
		for(ChunkOrder o : processed) {
			LeafChunk c = null;
			
			for(LeafChunk i : map)
				if(i.is(o))
					c = i;
			
			if(c == null)
				map.add(new LeafChunk(o));
			else {
				c.setPlayers(o.getPlayers());
				toRemove.remove(c);
			}
		}
		
		map.removeAll(toRemove);
	}
	
	public synchronized void reprocess() {
		try {
			map.forEach(c -> c.reprocess());
		} catch (ConcurrentModificationException e) {
			//Bukkit.getLogger().info("CME in reprocess, this should't happen please contact Rappelr");
		}
	}
	
	public void plant() {
		Bukkit.getScheduler().runTaskAsynchronously(Leaves.getInstance(), 
				() -> aPlant());
	}
	
	public synchronized void aPlant() {
		try {
			map.forEach(c -> c.plant());
		} catch (ConcurrentModificationException e) {
			//Bukkit.getLogger().info("CME in aPlant, this should't happen please contact Rappelr");
		}
	}
	
	public void removeProcesses() {
		if(redistributeID != -1)
			Bukkit.getScheduler().cancelTask(redistributeID);
		if(reprocessID != -1)
			Bukkit.getScheduler().cancelTask(reprocessID);
		if(replantID != -1)
			Bukkit.getScheduler().cancelTask(replantID);
	}

	public void reload() {
		
		removeProcesses();
		
		redistributeSpeed = Leaves.getInstance().getConfiguration().getSource().getLong("redistribute", redistributeSpeed);
		reprocessSpeed = Leaves.getInstance().getConfiguration().getSource().getLong("reprocess", reprocessSpeed);
		replantSpeed = Leaves.getInstance().getConfiguration().getSource().getLong("replant", replantSpeed);
		
		LeafChunk.reload();
		
		redistributeID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Leaves.getInstance(),
				() -> redistribute(), 21l, redistributeSpeed);
		reprocessID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Leaves.getInstance(),
				() -> reprocess(), 22l, reprocessSpeed);
		replantID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Leaves.getInstance(),
				() -> plant(), 23l, replantSpeed);
	}

}
