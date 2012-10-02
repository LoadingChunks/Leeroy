package net.loadingchunks.plugins.Leeroy;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import net.loadingchunks.plugins.Leeroy.Types.*;

public class LeeroyPlayerListener implements Listener {
	
	Leeroy plugin;
	
	public LeeroyPlayerListener(Leeroy plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{			
		if(!(event.getPlayer().getWorld().getName().startsWith("homeworld_")) || event.getPlayer().getGameMode() == GameMode.CREATIVE)
		{
			return;
		}

		Location pl = event.getPlayer().getLocation();

		// High X
		if(pl.getX() > this.plugin.getConfig().getDouble("home.border.x-max"))
		{
			event.getPlayer().teleport(new Location(event.getPlayer().getWorld(),(this.plugin.getConfig().getDouble("home.border.x-max") - 0.5), pl.getY(), pl.getZ(), pl.getYaw(), pl.getPitch()));
		}

		// Low X
		if(pl.getX() < this.plugin.getConfig().getDouble("home.border.x-min"))
		{
			event.getPlayer().teleport(new Location(event.getPlayer().getWorld(),(this.plugin.getConfig().getDouble("home.border.x-min") + 0.5), pl.getY(), pl.getZ(), pl.getYaw(), pl.getPitch()));
		}
		
		// High Z
		if(pl.getZ() > this.plugin.getConfig().getDouble("home.border.z-max"))
		{
			event.getPlayer().teleport(new Location(event.getPlayer().getWorld(),pl.getX(), pl.getY(), (this.plugin.getConfig().getDouble("home.border.z-max") - 0.5), pl.getYaw(), pl.getPitch()));
		}
		
		// Low Z
		if(pl.getZ() < this.plugin.getConfig().getDouble("home.border.z-min"))
		{
			event.getPlayer().teleport(new Location(event.getPlayer().getWorld(),pl.getX(), pl.getY(), (this.plugin.getConfig().getDouble("home.border.z-min") + 0.5), pl.getYaw(), pl.getPitch()));
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		if(event.getPlayer().getWorld().getName().startsWith("homeworld_") && !event.getPlayer().getWorld().getName().equalsIgnoreCase("homeworld_" + event.getPlayer().getName()))
		{
			Player p = event.getPlayer();
			if(plugin.mvcore.getMVWorldManager().isMVWorld("homeworld_" + p.getName()) && plugin.mvcore.getMVWorldManager().loadWorld("homeworld_" + p.getName()))
				p.teleport(this.plugin.mvcore.getMVWorldManager().getMVWorld("homeworld_" + p.getName()).getSpawnLocation());
			else
			{
				plugin.mvcore.getLogger().warning("[LEEROY] Something is odd! " + p.getName() + "'s homeworld isn't loading!");
				p.teleport(plugin.mvcore.getMVWorldManager().getMVWorld("mainworld").getSpawnLocation());
			}
		}

		if(event.getPlayer().getWorld().getName().startsWith("homeworld_") && !(LeeroyUtils.hasNPC(this.plugin, event.getPlayer().getWorld().getName())))
		{
			Location nl = new Location(event.getPlayer().getWorld(), this.plugin.getConfig().getDouble("home.butler.x"), this.plugin.getConfig().getDouble("home.butler.y"), this.plugin.getConfig().getDouble("home.butler.z"));
			this.plugin.npcs.spawn("butler",this.plugin.getConfig().getString("home.butler.name"), nl, "", "", "", "", false, event.getPlayer().getWorld().getName(), event.getPlayer().getWorld().getName() + "_butler");
		}	
	}
	
	@EventHandler
	public void onPlayerChangeWorld(PlayerChangedWorldEvent event)
	{
		if(event.getFrom().getName().startsWith("homeworld_"))
		{
			if(LeeroyUtils.hasNPC(this.plugin, event.getFrom().getName()) && this.plugin.NPCList.containsKey(event.getPlayer().getWorld().getName() + "_butler") && event.getFrom().getPlayers().isEmpty())
			{
				((ButlerNPC)this.plugin.NPCList.get(event.getFrom().getName() + "_butler")).npc.removeFromWorld();
				this.plugin.NPCList.remove(event.getFrom().getName() + "_butler");
			}
		}
		
		if(event.getPlayer().getWorld().getName().startsWith("homeworld_"))
		{
			if(!LeeroyUtils.hasNPC(this.plugin, event.getPlayer().getWorld().getName()))
			{
				Location nl = new Location(event.getPlayer().getWorld(), this.plugin.getConfig().getDouble("home.butler.x"), this.plugin.getConfig().getDouble("home.butler.y"), this.plugin.getConfig().getDouble("home.butler.z"));
				this.plugin.npcs.spawn("butler",this.plugin.getConfig().getString("home.butler.name"), nl, "", "", "", "", false, event.getPlayer().getWorld().getName(), event.getPlayer().getWorld().getName() + "_butler");
			}
		}
			
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event)
	{
		if(event.getTo().getWorld().getName().startsWith("homeworld_"))
		{
			if(!LeeroyUtils.hasNPC(this.plugin, event.getTo().getWorld().getName()))
			{
				Location nl = new Location(event.getTo().getWorld(), this.plugin.getConfig().getDouble("home.butler.x"), this.plugin.getConfig().getDouble("home.butler.y"), this.plugin.getConfig().getDouble("home.butler.z"));
				this.plugin.npcs.spawn("butler",this.plugin.getConfig().getString("home.butler.name"), nl, "", "", "", "", false, event.getTo().getWorld().getName(), event.getTo().getWorld().getName() + "_butler");
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		if(event.getPlayer().getWorld() != null && this.plugin.NPCList.containsKey(event.getPlayer().getWorld().getName() + "_butler") && event.getPlayer().getWorld().getPlayers().isEmpty())
		{
			((ButlerNPC)this.plugin.NPCList.get(event.getPlayer().getWorld().getName() + "_butler")).npc.removeFromWorld();
			this.plugin.NPCList.remove(event.getPlayer().getWorld().getName() + "_butler");
		}
	}
}
