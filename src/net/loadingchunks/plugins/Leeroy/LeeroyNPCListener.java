package net.loadingchunks.plugins.Leeroy;

import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.MetadataValue;

import com.topcat.npclib.nms.NpcEntityTargetEvent;

import net.loadingchunks.plugins.Leeroy.Types.*;

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
		if(event.getEntity() instanceof HumanEntity && event.getDamager() instanceof Player)
		{
			List<MetadataValue> md = event.getEntity().getMetadata("leeroy_id");
			List<MetadataValue> mdtype = event.getEntity().getMetadata("leeroy_type");

			if(md.size() > 0)
			{
				this.plugin.log.info("[LEEROY] Let's find that NPC... for damage event");

				if(mdtype.get(0).asString().equalsIgnoreCase("leeroy_npcbasic"))
					((BasicNPC)this.plugin.NPCList.get(md.get(0).asString())).onHit((Player)event.getDamager(), event);
			}
		}
		event.setDamage(0);
	}
	
	@EventHandler
	public void onEntityTarget(NpcEntityTargetEvent event)
	{
		if(event.getEntity() instanceof HumanEntity && event.getTarget() instanceof Player)
		{
			List<MetadataValue> md = event.getEntity().getMetadata("leeroy_id");
			List<MetadataValue> mdtype = event.getEntity().getMetadata("leeroy_type");

			if(md.size() > 0)
			{
				this.plugin.log.info("[LEEROY] Let's find that NPC... for target event");

				if(mdtype.get(0).asString().equalsIgnoreCase("leeroy_npcbasic"))
					((BasicNPC)this.plugin.NPCList.get(md.get(0).asString())).onTarget((Player)event.getTarget(), event);
			}
		}		
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		for(Entity e : event.getPlayer().getNearbyEntities(5, 5, 5))
		{
			if(e instanceof HumanEntity)
			{
				List<MetadataValue> md = e.getMetadata("leeroy_id");
				List<MetadataValue> mdtype = e.getMetadata("leeroy_type");

				if(md.size() > 0)
				{
					if(mdtype.get(0).asString().equalsIgnoreCase("leeroy_npcbasic"))
						((BasicNPC)this.plugin.NPCList.get(md.get(0).asString())).onNear(event.getPlayer());
				}
			}
		}
	}
}
