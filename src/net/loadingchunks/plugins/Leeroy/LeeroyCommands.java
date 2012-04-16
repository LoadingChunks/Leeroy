package net.loadingchunks.plugins.Leeroy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeeroyCommands implements CommandExecutor
{
	public Leeroy plugin;

	public LeeroyCommands(Leeroy plugin)
	{
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{	
		if(cmd.getName().equalsIgnoreCase("leeroy"))
		{
			this.plugin.log.info("[LEEROY] Got Leeroy Command Hook!");

			if(args[0].equalsIgnoreCase("spawn"))
			{
				String name;
				
				if(args.length == 3)
					name = args[2];
				else
					name = "Notch";

				this.plugin.log.info("[LEEROY] Got Spawn!");

				if(!(sender instanceof Player))
				{
					sender.sendMessage("[LEEROY] This command can only be used in-game.");
					return false;
				}

				if(!LeeroyPermissions.canSpawn((Player)sender))
				{
					sender.sendMessage("[LEEROY] This command is op-only!");
					return false;
				}

				this.plugin.log.info("[LEEROY] Init Handler.");
				LeeroyNPCHandler npc = new LeeroyNPCHandler(this.plugin);
				
				this.plugin.log.info("[LEEROY] Spawning...");
				npc.spawn(args[1], name, ((Player)sender).getLocation(), "", "", "", "", true, ((Player)sender).getWorld().getName());

				return true;
			}
		}
		
		return false;
	}
	
}
