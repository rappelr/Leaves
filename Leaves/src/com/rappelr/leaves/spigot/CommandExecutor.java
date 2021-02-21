package com.rappelr.leaves.spigot;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

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
		
		sender.sendMessage(ChatColor.RED + tag + " Reloading...");
		Leaves.getInstance().reload();
		sender.sendMessage(ChatColor.GREEN + tag + " Reload complete!");
		return true;
	}
	
}