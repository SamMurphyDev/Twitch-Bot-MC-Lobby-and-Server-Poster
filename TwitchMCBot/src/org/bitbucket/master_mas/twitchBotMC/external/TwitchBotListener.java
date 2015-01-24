package org.bitbucket.master_mas.twitchBotMC.external;

import java.util.ArrayList;
import java.util.List;

import org.bitbucket.master_mas.twitchBotMC.MinecraftChatHandler;
import org.bitbucket.master_mas.twitchBotMC.servers.MinecraftServerHandler;

public abstract class TwitchBotListener {

	//Static Reference
	private static List<TwitchBotListener> listeners = new ArrayList<TwitchBotListener>();
	
	public static void connectToServer(String serverIP) {
		for(TwitchBotListener listener : listeners)
			listener.joinServer(serverIP);
	}
	
	public static void connectToRoom(String room) {
		for(TwitchBotListener listener : listeners)
			listener.joinRoom(room);
	}
	
	//Object Reference
	public TwitchBotListener() {
		TwitchBotListener.listeners.add(this);
		if(TwitchBotListener.listeners.size() == 1)
			MinecraftServerHandler.listenerAttached = true;
	}
	
	public final void post(String message) {
		MinecraftChatHandler.getInstance().messageQueue.add(message);
	}
	
	public abstract void joinServer(String serverIP);
	
	public abstract void joinRoom(String room);
}
