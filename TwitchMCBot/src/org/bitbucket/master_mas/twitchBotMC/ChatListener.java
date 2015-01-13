package org.bitbucket.master_mas.twitchBotMC;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class ChatListener extends ListenerAdapter<PircBotX> {
	private Launcher launcher;

	public ChatListener(Launcher launcher) {
		this.launcher = launcher;
	}
	
	@Override
	public void onMessage(MessageEvent<PircBotX> event) throws Exception {
		System.out.println("This Happens");
		if(event.getMessage().startsWith("!server")) {
			MinecraftChatHandler.getInstance().messageQueue.add("The Server that I'm currently playing on is " + MinecraftCurrentInfo.serverHost);
			return;
		}
		
		if(event.getMessage().startsWith("!lobby")) {
			MinecraftChatHandler.getInstance().messageQueue.add("The Lobby that I'm currently at is " + MinecraftCurrentInfo.currentServerRoomUUID);
			return;
		}
		
		
		
//		if(event.getMessage().startsWith("!setserver")){
//			String message = event.getMessage();
//				MinecraftCurrentInfo.serverHost = message.substring(10);
//		}
		
		super.onMessage(event);
	}
}
