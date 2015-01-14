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

public class MinecraftChatHandler {

	private static MinecraftChatHandler instance;
	
	public ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<String>();
	
	public MinecraftChatHandler() {
		MinecraftChatHandler.instance = this;
		messageQueue.add("!mcbottestavailable");
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
			messageQueue.add("I'm Playing on " + ip + "  Come and Join me!!!");
			return;
		}
		
		//us.mineplex.com
		//eu.mineplex.com
		if(MinecraftCurrentInfo.serverHost != null) {
			if(MinecraftCurrentInfo.serverHost.toLowerCase().contains("mineplex")) {
				if(message.contains("Portal>")) {
					String bit[] = message.split("You have been sent to");
					MinecraftCurrentInfo.currentServerRoomUUID = bit[1].trim();
					messageQueue.add("I've just entered the game " + bit[1].trim() + " on " + MinecraftCurrentInfo.serverHost);
				}
				return;
			}
			
			//mc.hypixel.net
			if(MinecraftCurrentInfo.serverHost.toLowerCase().contains("hypixel")) {
				if(message.contains("Sending you to")) {
					String text[] = message.split("Sending you to");
					text[1].replace("!", "");
					MinecraftCurrentInfo.currentServerRoomUUID = text[1];
					messageQueue.add("I've just entered the game " + text[1] + " on " + MinecraftCurrentInfo.serverHost);
				}
				return;
			}
			
			//us.playmindcrack.com
			if(MinecraftCurrentInfo.serverHost.toLowerCase().contains("playmindcrack")) {
				if(message.contains("Warping to")) {
					String bits[] = message.split("Warping to");
					bits[1].replace(".", "");
					MinecraftCurrentInfo.currentServerRoomUUID = bits[1].trim();
					messageQueue.add("I've just entered the game " + bits[1].trim() + " on " + MinecraftCurrentInfo.serverHost);
				}
				return;
			}
			
			//play.hivemc.com
			if(MinecraftCurrentInfo.serverHost.toLowerCase().contains("hivemc")) {
				if(message.contains("Welcome to")) {
					String bits[] = message.split("\\?");
					MinecraftCurrentInfo.currentServerRoomUUID = bits[1].trim();
					messageQueue.add("I've just entered the game " + bits[1].trim() + " on " + MinecraftCurrentInfo.serverHost);
				}
				return;
			}
			
			//play.mc-legends.com
			if(MinecraftCurrentInfo.serverHost.toLowerCase().contains("mc-legends")) {
				if(message.contains("MCLegendsNetwork> Attempting to connect you to")) {
					String bits[] = message.split("Attempting to connect you to");
					bits[1] = bits[1].replace(".", "");
					MinecraftCurrentInfo.currentServerRoomUUID = bits[1].trim();
					messageQueue.add("I've just entered the game " + bits[1].trim() + " on " + MinecraftCurrentInfo.serverHost);
				}
				return;
			}
			
			
		}
	}
}
