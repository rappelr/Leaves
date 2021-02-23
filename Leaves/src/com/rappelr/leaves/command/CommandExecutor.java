package com.rappelr.leaves.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.rappelr.leaves.Leaves;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {
	
	private final String tag = "[" + Leaves.getInstance().getDescription().getName() + 
			"/" + Leaves.getInstance().getDescription().getVersion() + "]";

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(!sender.isOp()) {
			sender.sendMessage(ChatColor.RED + "That command is only available for operators");
			return true;
		}
		
		if(sender instanceof Player)
			sender.sendMessage(ChatColor.RED + tag + " Reloading...");
		Leaves.getInstance().getLogger().info("reloading...");
		
		Leaves.getInstance().reload();
		
		Leaves.getInstance().getLogger().info("reload complete!");
		if(sender instanceof Player)
			sender.sendMessage(ChatColor.GREEN + tag + " Reload complete!");
		
		return true;
	}
	
}