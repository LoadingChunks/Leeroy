package net.loadingchunks.plugins.Leeroy;

import java.awt.Color;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LeeroyPlayerListener implements Listener {
	
	Leeroy plugin;
	
	public LeeroyPlayerListener(Leeroy plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		if(this.plugin.getServer().getWorld("homeworld_" + event.getPlayer().getName()) == null && !this.plugin.mvcore.getMVWorldManager().loadWorld("homeworld_" + event.getPlayer().getName()))
		{
			Location loc = this.goToHomeWorld(event.getPlayer());
			if(loc != null)
				event.getPlayer().teleport(loc);
		}
	}
	
	public Location goToHomeWorld(Player p)
	{
		this.plugin.log.info("[LEEROY] Got home teleport, going to the user's home at homeworld_" + p.getName());

		this.plugin.getServer().getWorld("homeworld_" + p.getName()).loadChunk(this.plugin.getServer().getWorld("homeworld_" + p.getName()).getChunkAt(-23, 118));
		Location loc = new Location(this.plugin.getServer().getWorld("homeworld_" + p.getName()), 23, 70, 118);
		return loc;
	}
	
	public Location makeWorld(Player p)
	{
		Runtime run = Runtime.getRuntime();
		Process pr;
		if(this.plugin.getServer().getWorld("homeworld_" + p.getName()) == null && !this.plugin.mvcore.getMVWorldManager().loadWorld("homeworld_" + p.getName()))
		{
			this.plugin.log.info("[LEEROY] Player " + p.getName() + " has no home world, let's make them one!");
			this.plugin.log.info("[LEEROY] Creating world file from template...");
			try {
				pr = run.exec("cp -r homeworld homeworld_" + p.getName());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
			try {
				pr.waitFor();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			this.plugin.log.info("[LEEROY] Adding to WM...");
			if(!this.plugin.mvcore.getMVWorldManager().addWorld("homeworld_" + p.getName(), Environment.NORMAL, "foobar", "CleanroomGenerator:."))
			{
				Location loc = new Location(this.plugin.getServer().getWorlds().get(0), this.plugin.getServer().getWorlds().get(0).getSpawnLocation().getX(),this.plugin.getServer().getWorlds().get(0).getSpawnLocation().getY(),this.plugin.getServer().getWorlds().get(0).getSpawnLocation().getZ());
				p.sendMessage(Color.red + "[ERROR] Something went wrong! Please alert an admin and provide Error Code: 404");
				return loc;
			}
		}
		
		return null;
	}
}
