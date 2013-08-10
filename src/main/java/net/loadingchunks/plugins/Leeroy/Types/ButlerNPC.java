package net.loadingchunks.plugins.Leeroy.Types;

import java.util.List;

import net.LoadingChunks.vendor.npclib.NPCEntityTargetEvent;
import net.loadingchunks.plugins.Leeroy.Leeroy;
import net.LoadingChunks.vendor.npclib.HumanNPC;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;

/* When adding new NPC:
 *  
 */

public class ButlerNPC extends BasicNPC
{	
	public String type = "leeroy_npcbutler";
	public String hrtype = "butler";
	public long bcastInitial = 20L;
	public long bcastBetween = 500L;

	public ButlerNPC(Leeroy plugin, String name, Location l, String id, String msg1, String msg2, String msg3, String msg4, boolean isnew, String world)
	{
		super(plugin, name, l, id, msg1, msg2, msg3, msg4, isnew, world, "butler", "leeroy_npcbutler");
	}

	@Override
	public void SetBroadcast(final String msg)
	{
		final HumanNPC tmp = this.npc;
		this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable() {
			public void run() {
				List<Entity> entities = tmp.getBukkitEntity().getNearbyEntities(50,50,50);
				for(Entity e : entities)
				{
					if(e instanceof Player)
					{
						Player p = (Player)e;
						
						if(p.getWorld().getName().equalsIgnoreCase("homeworld_" + p.getName()))
						{
							p.sendMessage("<" + npc.getName() + "> Welcome to your homeworld, " + p.getDisplayName() + "!");
							p.sendMessage("<" + npc.getName() + "> Right click me to return to the main land.");
						} else if(p.getWorld().getName().startsWith("homeworld_")){
							p.sendMessage("<" + npc.getName() + "> Welcome to " + p.getWorld().getName().replace("homeworld_", "") + "'s homeworld, " + p.getDisplayName() + "!");
							p.sendMessage("<" + npc.getName() + "> Right click me to return to the main land.");
						} else {
							p.sendMessage("<" + npc.getName() + "> This world is so strange...");
						}
					}
				}
			}
		}, 40L, 40000L);
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
	}

	// Player right clicks
	@Override
	public void onRightClick(final Player player, NPCEntityTargetEvent event)
	{
		player.sendMessage("<" + this.name + "> You'll be transported to the main land in 5 seconds");
		player.sendMessage("<" + this.name + "> Thank you for using the Chunky Transport System!");

		this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
			public void run() {
				if(player == null || !player.isOnline())
					return;
				
				player.teleport(plugin.mvcore.getMVWorldManager().getMVWorld(plugin.getConfig().getString("general.lobby")).getSpawnLocation());
			}
		},100L);
	}

	// When a player attacks
	@Override
	public void onPlayer(Player assailant, EntityDamageByEntityEvent event)
	{
		return;
	}

	// What's that coming over the hill
	// Is it a monster?
	// When a monster attacks
	@Override
	public void onMonster(Monster monster, EntityDamageByEntityEvent event)
	{
		return;
	}

}
