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

public class BotMessagePoster implements Runnable {
	
	private Launcher launcher;
	private String channel = null;
	
	public static boolean mute = false;

	public BotMessagePoster(Launcher launcher) {
		this.launcher = launcher;
	}
	
	@Override
	public void run() {
		String line;
		
		while(true) {
			if(channel == null) {
				try {
					for(String channel : launcher.getBot().getConfiguration().getAutoJoinChannels().keySet())
						this.channel = channel;
				} catch (Exception e)  { }
			} else
				if((line = MinecraftChatHandler.getInstance().messageQueue.poll()) != null)
					if(!mute)
						launcher.getBot().sendIRC().message(channel, line);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
