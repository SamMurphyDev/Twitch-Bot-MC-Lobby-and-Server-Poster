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

import java.util.concurrent.ConcurrentLinkedQueue;

import org.bitbucket.master_mas.twitchBotMC.external.TwitchBotListener;
import org.bitbucket.master_mas.twitchBotMC.servers.HiveMC;
import org.bitbucket.master_mas.twitchBotMC.servers.Hypixel;
import org.bitbucket.master_mas.twitchBotMC.servers.McLegends;
import org.bitbucket.master_mas.twitchBotMC.servers.Mindcrack;
import org.bitbucket.master_mas.twitchBotMC.servers.MinecraftServerHandler;
import org.bitbucket.master_mas.twitchBotMC.servers.Mineplex;

public class MinecraftChatHandler {

	private static MinecraftChatHandler instance;
	
	public ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<String>();
	
	public MinecraftChatHandler() {
		MinecraftChatHandler.instance = this;
		messageQueue.add("!mcbotavailable");
	}

	public static MinecraftChatHandler getInstance() {
		return instance;
	}
	
	public void handleChatMessage(String message) {
		if(message.contains("[Client thread/INFO]: Connecting to")) {
			
			String lastBit = message.substring(46);
			String bits[] = lastBit.split(",");
			
			String ip = bits[0];
			ip.trim();
			MinecraftCurrentInfo.serverHost = ip;
			TwitchBotListener.connectToServer(ip);
			if(!MinecraftServerHandler.listenerAttached)
				messageQueue.add("I'm Playing on " + ip + "  Come and Join me!!!");
			return;
		}
		
		if(MinecraftCurrentInfo.serverHost != null) {
			if(MinecraftCurrentInfo.serverHost.toLowerCase().contains("mineplex"))
				new Mineplex().handle(message);
			else if(MinecraftCurrentInfo.serverHost.toLowerCase().contains("hypixel"))
				new Hypixel().handle(message);
			else if(MinecraftCurrentInfo.serverHost.toLowerCase().contains("playmindcrack"))
				new Mindcrack().handle(message);
			else if(MinecraftCurrentInfo.serverHost.toLowerCase().contains("hivemc"))
				new HiveMC().handle(message);
			else if(MinecraftCurrentInfo.serverHost.toLowerCase().contains("mc-legends"))
				new McLegends().handle(message);
		}
	}
}
