package net.loadingchunks.plugins.Leeroy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import org.bukkit.World;


public class LeeroyUtils {
	/**
	 * 
	 * @param world
	 * @param plugin
	 * @param destinationName
	 */
	public static void DuplicateWorld(World world, Leeroy plugin, String destinationName) {
		
		if(world == null) {
			plugin.getLogger().severe("World does not exist! Tried to make " + destinationName);
			return;
		}

		world.save();

		File worldFolder = world.getWorldFolder();
		File worldDestinationFolder = new File(plugin.getServer().getWorldContainer().getAbsoluteFile()
				+ File.separator + destinationName);

		try {
			copyFolder(worldFolder, worldDestinationFolder);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 *             Why do I have to comment this?!
	 */
	public static void copyFolder(File src, File dest) throws IOException {

		if (src.isDirectory()) {

			// if directory not exists, create it
			if (!dest.exists()) {
				dest.mkdir();
			}

			// list all the directory contents
			String[] files = src.list();

			for (String file : files) {
				// construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// recursive copy
				copyFolder(srcFile, destFile);
			}

		} else {
			// if file, then copy it
			// Use bytes stream to support all file types
			if (src.getName().equals("session.lock"))
				return;
			if (src.getName().equals("uid.dat"))
				return;

			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();
			out.close();
		}
	}
	
	public static Boolean hasNPC(Leeroy p, String w)
	{
		HashMap<String, Object> tmplist = p.NPCList;
		if(tmplist.containsKey(w + "_butler"))
			return true;
		else
			return false;
	}
}
