package net.loadingchunks.plugins.Leeroy.Types;

import java.util.List;
import java.util.Random;
import java.io.File;

import net.loadingchunks.plugins.Leeroy.Leeroy;
import net.loadingchunks.plugins.Leeroy.LeeroyUtils;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import com.topcat.npclib.entity.HumanNPC;
import com.topcat.npclib.nms.NpcEntityTargetEvent;

public class PortNPC extends BasicNPC
{	
	public String type = "leeroy_npcport";
	public String hrtype = "port";
	public long bcastInitial = 20L;
	public long bcastBetween = 500L;

	public PortNPC(Leeroy plugin, String name, Location l, String id, String msg1, String msg2, String msg3, String msg4, boolean isnew, String world)
	{
		super(plugin, name, l, id, msg1, msg2, msg3, msg4, isnew, world, "port", "leeroy_npcport");
	}

	@Override
	public void SetBroadcast(final String msg)
	{
		if(msg == null || msg.isEmpty())
			return;

		final HumanNPC tmp = this.npc;
		this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable() {
			public void run() {
				List<Entity> entities = tmp.getBukkitEntity().getNearbyEntities(10,5,10);
				for(Entity e : entities)
				{
					if(e instanceof Player)
					{
						String fmsg;
						Player p = (Player)e;
						fmsg = msg.replaceAll("<player>", p.getDisplayName());
						fmsg = fmsg.replaceAll("<npc>", npc.getName());
						p.sendMessage(fmsg);
					}
				}
			}
		}, this.bcastInitial, this.bcastBetween);
	}

	// Player bounces (doesn't seem to work)
	@Override
	public void onBounce(Player player, EntityTargetEvent event)
	{
		return;
	}

	// Player near
	@Override
	public void onNear(Player player)
	{
		this.npc.lookAtPoint(player.getEyeLocation());
		return;
	}

	// Player right clicks
	public void onRightClick(Player player, NpcEntityTargetEvent event)
	{
		final Player p = player;
		Location l = null;
		Boolean making = false;

		if(!this.plugin.mvcore.getMVWorldManager().loadWorld("homeworld_" + player.getName()))
		{
			File worldDestinationFolder = new File(plugin.getServer().getWorldContainer().getAbsoluteFile() + File.separator + "homeworld_" + player.getName());

			if(!worldDestinationFolder.exists())
			{
				player.sendMessage("<" + this.name + "> We're just building your homeworld, right click me again to go there!");
				l = this.makeWorld(player);
				making = true;
			} else {
				this.plugin.getLogger().warning("CAUGHT UN-ADDED HOMEWORLD FOLDER: " + worldDestinationFolder.getAbsolutePath());
				for(String command : (List<String>)this.plugin.getConfig().getStringList("events.onBuild"))
				{
					this.plugin.getServer().dispatchCommand((CommandSender) (this.plugin.getServer().getConsoleSender()), command.replace("{player}", p.getName()));	
				}			}
		}

		if(l == null && making)
		{
			return;
		}

		player.sendMessage("<" + this.name + "> You'll be transported to your homeworld in 5 seconds");
		player.sendMessage("<" + this.name + "> Thank you for using the Chunky Transport System!");
		
		for(String command : this.plugin.getConfig().getStringList("events.onEnter"))
		{
			this.plugin.getServer().dispatchCommand((CommandSender) (this.plugin.getServer().getConsoleSender()), command.replace("{player}", player.getName()));	
		}

		this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
			public void run() {
				if(p == null || !p.isOnline())
					return;

				if(plugin.mvcore.getMVWorldManager().isMVWorld("homeworld_" + p.getName()) && plugin.mvcore.getMVWorldManager().loadWorld("homeworld_" + p.getName()))
				{
					ConfigurationSection override = plugin.getConfig().getConfigurationSection("homeworlds.homeworld_" + p.getName());
					
					if(override != null && override.contains("spawn"))
					{
						p.teleport(new Location(plugin.mvcore.getMVWorldManager().getMVWorld("homeworld_" + p.getName()).getCBWorld(), override.getInt("spawn.x"), override.getInt("spawn.y"), override.getInt("spawn.z"), (float)override.getDouble("spawn.yaw"), (float)override.getDouble("spawn.pitch")));
					} else
						p.teleport(plugin.mvcore.getMVWorldManager().getMVWorld("homeworld_" + p.getName()).getSpawnLocation());
				}
				else
					plugin.mvcore.getLogger().warning("[LEEROY] Something is odd! " + p.getName() + "'s homeworld isn't loading!");
			}
		},100L);
	}

	// When a player attacks
	@Override
	public void onPlayer(Player assailant, EntityDamageByEntityEvent event)
	{
		return;
	}

	// When a monster attacks
	@Override
	public void onMonster(Monster monster, EntityDamageByEntityEvent event)
	{
		this.broadcast("We're under attack!");
		this.npc.lookAtPoint(monster.getLocation());
		this.npc.animateArmSwing();
		
		if(this.IsNearby(monster.getLocation(), event.getDamager().getLocation(), 2, 2))
			monster.damage(5);
	}

	public Location makeWorld(Player p)
	{
		
		if(this.plugin.getServer().getWorld("homeworld_" + p.getName()) == null && !this.plugin.mvcore.getMVWorldManager().loadWorld("homeworld_" + p.getName()))
		{
			this.plugin.log.info("[LEEROY] Player " + p.getName() + " has no home world, let's make them one!");
			this.plugin.log.info("[LEEROY] Creating world file from template...");
			
			Random rand = new Random();
			
			this.plugin.log.info("[LEEROY] Picking from " + this.plugin.getConfig().getStringList("general.templates").size() + " templates.");
			
			int randomnum = rand.nextInt(this.plugin.getConfig().getStringList("general.templates").size());

			String randworld = this.plugin.getConfig().getStringList("general.templates").get(randomnum);
			
			LeeroyUtils.DuplicateWorld(this.plugin.getServer().getWorld(randworld), this.plugin, "homeworld_" + p.getName());
			
			this.plugin.log.info("[LEEROY] Adding to WM... (Using Template #" + randomnum + ", aka " + randworld + ")");
			
			for(String command : (List<String>)this.plugin.getConfig().getStringList("events.onBuild"))
			{
				this.plugin.getServer().dispatchCommand((CommandSender) (this.plugin.getServer().getConsoleSender()), command.replace("{player}", p.getName()));	
			}
			
			ConfigurationSection override = this.plugin.getConfig().getConfigurationSection("overrides." + randworld);
			
			if(override != null && override.contains("spawn"))
			{
				this.plugin.getLogger().info("[LEEROY] Adding overrides...");
				this.plugin.getConfig().createSection("homeworlds.homeworld_" + p.getName(), override.getValues(true));
				this.plugin.saveConfig();
			}

			if(!this.plugin.getMVCore().getMVWorldManager().loadWorld("homeworld_" + p.getName()))
			{
				p.sendMessage("<" + this.name + "> Something went wrong! Please alert an admin and provide Error Code: 404");
				return null;
			}
		}

		return null;
	}
}
