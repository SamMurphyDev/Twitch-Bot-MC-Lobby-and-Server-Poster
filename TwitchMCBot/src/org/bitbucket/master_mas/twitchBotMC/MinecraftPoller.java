package org.bitbucket.master_mas.twitchBotMC;

import java.io.File;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;

public class MinecraftPoller {

	private File log = new File(System.getenv("APPDATA") + "\\.minecraft\\logs\\latest.log");
	private Launcher launcher;
	
	private MinecraftChatHandler chatHandler;
	
	public MinecraftPoller(Launcher launcher) {
		this.launcher = launcher;
		if(!log.exists()) {
			launcher.stopCheckerThread();
			launcher.changeStatusLabel("No Minecraft Log File Detected", "red");
			return;
		}
		
		chatHandler = new MinecraftChatHandler();
		
		TailerListener listener = new PCTailListener();
		Tailer tailer = new Tailer(log, listener, 500, true);
		
		Thread thread = new Thread(tailer);
		thread.start();
	}

	private class PCTailListener extends TailerListenerAdapter {
		@Override
		public void handle(String line) {
			super.handle(line);
			chatHandler.handleChatMessage(line);
		}
	}
}
