package net.loadingchunks.plugins.Leeroy;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.loadingchunks.plugins.Leeroy.Types.BasicNPC;

import org.bukkit.Location;

public class LeeroyNPCHandler {
	
	public Leeroy plugin;
	
	public LeeroyNPCHandler(Leeroy plugin)
	{
		this.plugin = plugin;
	}
	
	public void spawn(String type, String name, Location l, String msg1, String msg2, String msg3, String msg4, boolean isnew, String world)
	{
		byte[] bytesOfMessage = null;
		try {
			bytesOfMessage = l.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {

			MessageDigest md = null;

			md = MessageDigest.getInstance("MD5");
			byte[] td = md.digest(bytesOfMessage);

			if(type.equalsIgnoreCase("basic") || type.isEmpty())
			{
				this.plugin.NPCList.put(type + "_" + td.toString(), new BasicNPC(this.plugin, name, l, type + "_" + td.toString(), msg1, msg2, msg3, msg4, isnew, world));
			}

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
