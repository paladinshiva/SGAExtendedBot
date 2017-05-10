package com.reztek.Badges;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

import com.reztek.SGAExtendedBot;
import com.reztek.Base.Taskable;
import com.reztek.Secret.GlobalDefs;

public class BadgeCacheTask extends Taskable{
	
	private final long CACHE_TIMEOUT = 86400000; // 24 hrs
	
	public BadgeCacheTask(SGAExtendedBot bot) {
		super(bot);
		setTaskName("Badge Cache Cleaner");
		setTaskDelay(20);
	}

	@Override
	public void runTask() {
		cleanCache();
	}
	
	public void cleanCache() {
		System.out.println("Starting Cache Cleaning Check...");
		File cacheDir = new File((GlobalDefs.BOT_DEV ? GlobalDefs.LOCAL_DEV_BADGE_CACHE : GlobalDefs.LOCAL_BADGE_CACHE));
		if (!cacheDir.isDirectory()) {
			System.out.println("GlobalDefs.LOCAL_IMGFOLDER is not a Directory!");
			return;
		}
		Date d = new Date();
		for (File f : cacheDir.listFiles()) {
			try {
				BasicFileAttributes ba = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
				if ((d.getTime() - ba.creationTime().toMillis()) >= CACHE_TIMEOUT) {
					System.out.println(f.getName() + " is out of cache date - deleting");
					f.delete();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Cache Cleaning Complete!");
	}
}
