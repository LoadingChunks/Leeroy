package net.loadingchunks.plugins.Leeroy;

import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.MetadataValue;


import net.loadingchunks.plugins.Leeroy.Types.*;
import net.LoadingChunks.vendor.npclib.NPCEntityTargetEvent;

import java.util.List;

public class LeeroyNPCListener implements Listener
{
	private Leeroy plugin;
	
	public LeeroyNPCListener(Leeroy plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	{
		List<MetadataValue> md = event.getEntity().getMetadata("leeroy_id");
		List<MetadataValue> mdtype = event.getEntity().getMetadata("leeroy_type");

		if(md.size() > 0 && this.plugin.NPCList.containsKey(md.get(0).asString()))
		{
			this.plugin.log.finer("[LEEROY] Let's find that NPC... for damage event");

			if(mdtype.get(0).asString().equalsIgnoreCase("leeroy_npcbasic"))
				((BasicNPC)this.plugin.NPCList.get(md.get(0).asString())).onHit(event.getDamager(), event);
			else if(mdtype.get(0).asString().equalsIgnoreCase("leeroy_npcport"))
				((PortNPC)this.plugin.NPCList.get(md.get(0).asString())).onHit(event.getDamager(), event);
			else if(mdtype.get(0).asString().equalsIgnoreCase("leeroy_npcbutler"))
				((ButlerNPC)this.plugin.NPCList.get(md.get(0).asString())).onHit(event.getDamager(), event);
			else if(mdtype.get(0).asString().equalsIgnoreCase("leeroy_npcshop"))
				((ShopNPC)this.plugin.NPCList.get(md.get(0).asString())).onHit(event.getDamager(), event);			

			event.setDamage(0.0);
		}
	}
	
	@EventHandler
	public void onEntityTarget(NPCEntityTargetEvent event)
	{
		if(event.getEntity() instanceof HumanEntity && event.getTarget() instanceof Player)
		{
			List<MetadataValue> md = event.getEntity().getMetadata("leeroy_id");
			List<MetadataValue> mdtype = event.getEntity().getMetadata("leeroy_type");

			if(md.size() > 0 && this.plugin.NPCList.containsKey(md.get(0).asString()))
			{
				this.plugin.log.finer("[LEEROY] Let's find that NPC... for target event");

				if(mdtype.get(0).asString().equalsIgnoreCase("leeroy_npcbasic"))
					((BasicNPC)this.plugin.NPCList.get(md.get(0).asString())).onTarget((Player)event.getTarget(), event);
				else if(mdtype.get(0).asString().equalsIgnoreCase("leeroy_npcport"))
					((PortNPC)this.plugin.NPCList.get(md.get(0).asString())).onTarget((Player)event.getTarget(), event);
				else if(mdtype.get(0).asString().equalsIgnoreCase("leeroy_npcbutler"))
					((ButlerNPC)this.plugin.NPCList.get(md.get(0).asString())).onTarget((Player)event.getTarget(), event);
				else if(mdtype.get(0).asString().equalsIgnoreCase("leeroy_npcshop"))
					((ShopNPC)this.plugin.NPCList.get(md.get(0).asString())).onTarget((Player)event.getTarget(), event);
			}
		}		
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		Integer r = 5;
		
		if(event.getPlayer().getWorld().getName().startsWith("homeworld_"))
			r = 50;

		List<Entity> entities = event.getPlayer().getNearbyEntities(r, r, r);
		for(Entity e : entities)
		{
			if(e instanceof HumanEntity)
			{
				List<MetadataValue> md = e.getMetadata("leeroy_id");
				List<MetadataValue> mdtype = e.getMetadata("leeroy_type");

				if(md.size() > 0 && this.plugin.NPCList.containsKey(md.get(0).asString()))
				{
					if(mdtype.get(0).asString().equalsIgnoreCase("leeroy_npcbasic"))
						((BasicNPC)this.plugin.NPCList.get(md.get(0).asString())).onNear(event.getPlayer());
					else if(mdtype.get(0).asString().equalsIgnoreCase("leeroy_npcport"))
						((PortNPC)this.plugin.NPCList.get(md.get(0).asString())).onNear(event.getPlayer());
					else if(mdtype.get(0).asString().equalsIgnoreCase("leeroy_npcbutler"))
						((ButlerNPC)this.plugin.NPCList.get(md.get(0).asString())).onNear(event.getPlayer());
					else if(mdtype.get(0).asString().equalsIgnoreCase("leeroy_npcshop"))
						((ShopNPC)this.plugin.NPCList.get(md.get(0).asString())).onNear(event.getPlayer());
				}
			}
		}
	}
}
