package org.bitbucket.master_mas.twitchBotMC;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MinecraftChatHandler {

	private static MinecraftChatHandler instance;
	
	public ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<String>();
	
	public MinecraftChatHandler() {
		MinecraftChatHandler.instance = this;
		
		messageQueue.add("MC Lobby and Status Poster has entered the chat");
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
			messageQueue.add("I'm Playing on " + ip);
			messageQueue.add("Come and join me!!!");
			System.out.println("Added " + ip + " to post queue");
			return;
		}
		
		if(MinecraftCurrentInfo.serverHost != null) {
			if(MinecraftCurrentInfo.serverHost.toLowerCase().contains("mineplex")) {
				if(message.contains("Portal>")) {
					String bit[] = message.split("You have been sent to");
					MinecraftCurrentInfo.currentServerRoomUUID = bit[1].trim();
					System.out.println("Test Lock Up");
					messageQueue.add("I've just entered the game " + bit[1].trim() + " on " + MinecraftCurrentInfo.serverHost);
					System.out.println("Lock Up");
					System.out.println("Added " + bit[1] + " to post queue");
				}
				return;
			}
		}
	}
}
