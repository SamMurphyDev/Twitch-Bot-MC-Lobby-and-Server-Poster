/*
 * Twitch MCBot - Posts which servers you are in, lobbies and mini-games.
 * Copyright (C) 2015 Sam Murphy
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.bitbucket.master_mas.twitchBotMC.servers;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.bitbucket.master_mas.twitchBotMC.MinecraftChatHandler;
import org.bitbucket.master_mas.twitchBotMC.MinecraftCurrentInfo;
import org.bitbucket.master_mas.twitchBotMC.external.TwitchBotListener;

public abstract class MinecraftServerHandler {

	protected final ConcurrentLinkedQueue<String> messageQueue;
	
	public static boolean listenerAttached = false;
	
	public MinecraftServerHandler() {
		messageQueue = MinecraftChatHandler.getInstance().messageQueue;
	}
	
	public void messageQueue(String location, boolean lobby) {
		TwitchBotListener.connectToRoom(location);
		
		if(!listenerAttached)
			if(lobby)
				messageQueue.add("I've just spawned in the lobby " + location + " on " + MinecraftCurrentInfo.serverHost);
			else
				messageQueue.add("I've justed entered the mini-game " + location + " on " + MinecraftCurrentInfo.serverHost);
	}
	
	public abstract void handle(String message);
}
