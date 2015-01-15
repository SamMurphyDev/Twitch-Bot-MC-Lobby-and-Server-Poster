/*
 * Twitch MCBot - Posts which servers you are in, lobbies and mini-games.
 * Copyright (C) 2015  Sam Murphy
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.bitbucket.master_mas.twitchBotMC;

import java.io.File;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;

public class MinecraftPoller {

	private File log = new File(System.getenv("APPDATA") + "\\.minecraft\\logs\\latest.log");
	@SuppressWarnings("unused")
	private Launcher launcher;
	
	private MinecraftChatHandler chatHandler;
	
	public MinecraftPoller(Launcher launcher) {
		this.launcher = launcher;
		if(!log.exists()) {
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
