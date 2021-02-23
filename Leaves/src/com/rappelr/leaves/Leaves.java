package com.rappelr.leaves;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.rappelr.leaves.command.CommandExecutor;
import com.rappelr.leaves.config.Configuration;
import com.rappelr.leaves.map.LeafMap;

import lombok.Getter;

public class Leaves extends JavaPlugin {
	
	@Getter
	private static Leaves instance;
	
	@Getter
	private Configuration configuration;
	
	@Getter
	private LeafMap map;
	
	{
		instance = this;
	}
	
	@Override
    public void onEnable() {
		configuration = new Configuration("config.yml", this, true);
		configuration.load();
		
		map = new LeafMap();
		
		getCommand("leavesreload").setExecutor(new CommandExecutor());
	}

	@Override
    public void onDisable() {
		map.removeProcesses();
		Bukkit.getScheduler().cancelTasks(this);
	}

	public void reload() {
		configuration.load();
		map.reload();
	}
}
