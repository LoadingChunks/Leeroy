package net.loadingchunks.plugins.Leeroy;

import org.bukkit.entity.Player;

public class LeeroyPermissions {

	public static boolean canSpawn(Player p)
	{
		return p.isOp();
	}

}
