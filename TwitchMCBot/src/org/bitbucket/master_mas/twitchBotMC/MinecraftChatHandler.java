package org.bitbucket.master_mas.twitchBotMC;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MinecraftChatHandler {

	private static MinecraftChatHandler instance;
	
	public ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<String>();
	
	public MinecraftChatHandler() {
		MinecraftChatHandler.instance = this;
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
			System.out.println("Added " + ip + " to post queue");
			return;
		}
		
		if(MinecraftCurrentInfo.serverHost != null) {
			if(MinecraftCurrentInfo.serverHost.toLowerCase().contains("mineplex")) {
				if(message.contains("Portal>")) {
					String bit[] = message.split("You have been sent to");
					MinecraftCurrentInfo.currentServerRoomUUID = bit[1].trim();
					messageQueue.add("I've just entered the game " + bit[1].trim() + " on " + MinecraftCurrentInfo.serverHost);
					System.out.println("Added " + bit[1] + " to post queue");
				}
				return;
			}
			
			if(MinecraftCurrentInfo.serverHost.toLowerCase().contains("hypixel")) {
				if(message.contains("Sending you to")) {
					String text[] = message.split("Sending you to");
					text[1].replace("!", "");
					MinecraftCurrentInfo.currentServerRoomUUID = text[1];
					messageQueue.add("I've just entered the game " + text[1] + " on " + MinecraftCurrentInfo.serverHost);
					System.out.println("Added " + text[1] + " to post queue");
				}
				return;
			}
			
			if(MinecraftCurrentInfo.serverHost.toLowerCase().contains("playmindcrack")) {
				if(message.contains("Warping to")) {
					String bits[] = message.split("Warping to");
					bits[1].replace(".", "");
					MinecraftCurrentInfo.currentServerRoomUUID = bits[1].trim();
					messageQueue.add("I've just entered the game " + bits[1].trim() + " on " + MinecraftCurrentInfo.serverHost);
					System.out.println("Added " + bits[1] + " to post queue");
				}
				return;
			}
		}
	}
}
