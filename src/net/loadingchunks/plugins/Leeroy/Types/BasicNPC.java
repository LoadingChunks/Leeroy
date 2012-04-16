package net.loadingchunks.plugins.Leeroy.Types;

import net.loadingchunks.plugins.Leeroy.Leeroy;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import com.topcat.npclib.NPCManager;
import com.topcat.npclib.entity.HumanNPC;
import com.topcat.npclib.entity.NPC;
import com.topcat.npclib.nms.NpcEntityTargetEvent;
import com.topcat.npclib.nms.NpcEntityTargetEvent.NpcTargetReason;

public class BasicNPC
{
	public NPCManager manager;
	public String name;
	public HumanNPC npc;
	public Leeroy plugin;
	public String message1;
	public String message2;
	public String message3;
	public String message4;
	
	private final String type = "leeroy_npcbasic";
	private final String hrtype = "basic";

	public BasicNPC(Leeroy plugin, String name, Location l, String id, String msg1, String msg2, String msg3, String msg4, boolean isnew, String world)
	{
		this.plugin = plugin;
		this.manager = new NPCManager(plugin);
		this.name = name;
		this.npc = (HumanNPC)manager.spawnHumanNPC(name, l);
		FixedMetadataValue meta = new FixedMetadataValue(this.plugin, this.type);
		FixedMetadataValue hash = new FixedMetadataValue(this.plugin, id);
		this.npc.getBukkitEntity().setMetadata("leeroy_type", meta);
		this.npc.getBukkitEntity().setMetadata("leeroy_id", hash);
		
		if(isnew)
			this.plugin.sql.AddNPC(id, name, this.hrtype, l, world);
		
		this.message1 = msg1;
		this.message2 = msg2;
		this.message3 = msg3;
		this.message4 = msg4;
		
		this.SetBroadcast(this.message4);
	}
	
	public void SetBroadcast(final String msg)
	{
		if(msg == null || msg.isEmpty())
			return;

		final HumanNPC tmp = this.npc;
		this.plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(this.plugin, new Runnable() {
			public void run() {
				for(Entity e : tmp.getBukkitEntity().getNearbyEntities(10,5,10))
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
		}, 20L, 6000L);
	}
	
	public void onTarget(Player player, NpcEntityTargetEvent event)
	{
		if(event.getNpcReason() == NpcTargetReason.NPC_RIGHTCLICKED)
			this.onRightClick(player, event);
		else if(event.getNpcReason() == NpcTargetReason.NPC_BOUNCED)
			this.onBounce(player, event);
	}

	public void onRightClick(Player player, NpcEntityTargetEvent event)
	{
		player.sendMessage("<" + this.name + "> Yes?"); 
	}
	
	public void onBounce(Player player, EntityTargetEvent event)
	{
		player.sendMessage("<" + this.name + "> Hey, careful!");
	}
	
	public void onNear(Player player)
	{
		this.npc.moveTo(this.lookAt(this.npc.getBukkitEntity().getLocation(), player.getLocation()));
		return;
	}
	
	public void onHit(Player assailant, EntityDamageByEntityEvent event)
	{
		this.npc.actAsHurt();
		assailant.sendMessage("<" + this.name + "> Hey, that hurts!");
	}
	
	
	public static boolean IsNearby(Player p, Location l)
	{	
		if( ((l.getX() + 10) > p.getLocation().getX() && (l.getX() - 10) < p.getLocation().getX()) &&
				((l.getY() + 5) > p.getLocation().getY() && (l.getY() - 5) < p.getLocation().getY()) &&
				((l.getZ() + 10) > p.getLocation().getY() && (l.getY() - 10) < p.getLocation().getY()))
		{
			return true;
		} else
			return false;
	}
	
	// Credit to bergerkiller for this: http://forums.bukkit.org/threads/lookat-and-move-functions.26768/
    public static Location lookAt(Location loc, Location lookat) {
        //Clone the loc to prevent applied changes to the input loc
        loc = loc.clone();

        // Values of change in distance (make it relative)
        double dx = lookat.getX() - loc.getX();
        double dy = lookat.getY() - loc.getY();
        double dz = lookat.getZ() - loc.getZ();

        // Set yaw
        if (dx != 0) {
            // Set yaw start value based on dx
            if (dx < 0) {
                loc.setYaw((float) (1.5 * Math.PI));
            } else {
                loc.setYaw((float) (0.5 * Math.PI));
            }
            loc.setYaw((float) loc.getYaw() - (float) Math.atan(dz / dx));
        } else if (dz < 0) {
            loc.setYaw((float) Math.PI);
        }

        // Get the distance from dx/dz
        double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));

        // Set pitch
        loc.setPitch((float) -Math.atan(dy / dxz));

        // Set values, convert to degrees (invert the yaw since Bukkit uses a different yaw dimension format)
        loc.setYaw(-loc.getYaw() * 180f / (float) Math.PI);
        loc.setPitch(loc.getPitch() * 180f / (float) Math.PI);

        return loc;
    }
}
