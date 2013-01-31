package net.loadingchunks.plugins.Leeroy;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.loadingchunks.plugins.Leeroy.Types.*;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class LeeroyNPCHandler {
	
	public Leeroy plugin;
	
	public LeeroyNPCHandler(Leeroy plugin)
	{
		this.plugin = plugin;
	}
	
	public void spawn(String type, String name, Location l, String msg1, String msg2, String msg3, String msg4, boolean isnew, String world, String id)
	{
		byte[] bytesOfMessage = null;
		try {
			bytesOfMessage = (l + Long.toString(System.currentTimeMillis())).toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		ConfigurationSection override = this.plugin.getConfig().getConfigurationSection("homeworlds." + world);
		
		if(override != null && type.equalsIgnoreCase("butler") && override.contains("butler"))
		{
			if(override.contains("butler.spawn"))
				l = new Location(l.getWorld(), override.getInt("butler.x"), override.getInt("butler.y"), (float)override.getInt("butler.z"), (float)override.getDouble("butler.yaw"), (float)override.getDouble("butler.pitch"));

			if(override.contains("butler.name"))
				name = override.getString("butler.name");
		}
		
		try {

			MessageDigest md = null;

			md = MessageDigest.getInstance("MD5");
			byte[] td = md.digest(bytesOfMessage);
			
			if(id == null)
				id = world + "-" + type + "_" + td.toString();

			this.plugin.log.info("[LEEROY] Spawning " + id);
			if(type.equalsIgnoreCase("basic") || type.isEmpty())
			{
				this.plugin.NPCList.put(id, new BasicNPC(this.plugin, name, l, id, msg1, msg2, msg3, msg4, isnew, world, "basic", "leeroy_npcbasic"));
			} else if (type.equalsIgnoreCase("port"))
			{
				this.plugin.NPCList.put(id, new PortNPC(this.plugin, name, l, id, msg1, msg2, msg3, msg4, isnew, world));
			} else if (type.equalsIgnoreCase("butler"))
			{
				this.plugin.NPCList.put(id, new ButlerNPC(this.plugin, name, l, id, msg1, msg2, msg3, msg4, isnew, world));
			} else if (type.equalsIgnoreCase("shop"))
			{
				this.plugin.NPCList.put(id, new ShopNPC(this.plugin, name, l, id, msg1, msg2, msg3, msg4, isnew, world));
			}

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public void remove(String id)
	{
		((BasicNPC)this.plugin.NPCList.get(id)).npc.removeFromWorld();
		try {
			this.plugin.sql.RemoveNPC(id);
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		this.plugin.NPCList.remove(id);
	}
}
