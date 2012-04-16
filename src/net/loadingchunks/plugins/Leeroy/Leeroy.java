package net.loadingchunks.plugins.Leeroy;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import com.onarandombox.MultiverseCore.MultiverseCore;
import net.loadingchunks.plugins.Leeroy.Types.*;

public class Leeroy extends JavaPlugin {
	
	public Logger log = Logger.getLogger("Minecraft");
	public MultiverseCore mvcore;
	public HashMap<String, Object> NPCList = new HashMap<String, Object>();
	public LeeroyNPCHandler npcs;
	public LeeroySQL sql;
	private LeeroyCommands cmdExecutor;

	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(new LeeroyPlayerListener(this), this);
		this.getServer().getPluginManager().registerEvents(new LeeroyNPCListener(this), this);

		this.cmdExecutor = new LeeroyCommands(this);
		
		getCommand("leeroy").setExecutor(this.cmdExecutor);

		if(!this.getServer().getPluginManager().isPluginEnabled("Multiverse-Core"))
		{
			log.info("[LEEROY] Multiverse not found, disabling...");
			this.getServer().getPluginManager().disablePlugin(this);
		}		

		this.mvcore = (MultiverseCore) this.getServer().getPluginManager().getPlugin("Multiverse-Core");
		
		log.info("[LEEROY] Loading Config...");
		
		this.getConfig().options().copyDefaults(true);
		this.getConfig();
		this.saveConfig();
		
		log.info("[LEEROY] Alright chumps let's do this.");
		
		log.info("[LEEROY] Time to create the saved NPCs.");

		this.npcs = new LeeroyNPCHandler(this);

		this.sql = new LeeroySQL(this);
		
		if(!this.sql.success)
			this.getServer().getPluginManager().disablePlugin(this);
		
		this.sql.PopNPCs();
	}
	
	public void onDisable() {
		log.info("[LEEROY] JEEEEENKIIIINS");
	}
}