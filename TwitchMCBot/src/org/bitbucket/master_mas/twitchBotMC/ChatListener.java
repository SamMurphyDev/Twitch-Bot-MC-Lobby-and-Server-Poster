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

import java.awt.EventQueue;

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
		if(event.getMessage().startsWith("!server")) {
			MinecraftChatHandler.getInstance().messageQueue.add("The Server that I'm currently playing on is " + MinecraftCurrentInfo.serverHost);
			return;
		}
		
		if(event.getMessage().startsWith("!lobby")) {
			MinecraftChatHandler.getInstance().messageQueue.add("The Lobby that I'm currently at is " + MinecraftCurrentInfo.currentServerRoomUUID);
			return;
		}
		
		if(event.getMessage().startsWith("!mcbottestavailable")) {
			MinecraftChatHandler.getInstance().messageQueue.add("!mcbottestnot");
			return;
		}
		
		if(event.getMessage().startsWith("!mcbottestnot")) {
			if(!event.getUser().getLogin().equalsIgnoreCase(launcher.getBot().getConfiguration().getName())) {
				launcher.getBot().sendIRC().quitServer();
				launcher.stopCheckerThread();
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						launcher.changeStatusLabel("A another bot instance already exists in chat", "red");
					}
				});
			}
			return;
		}
		
		if(event.getMessage().startsWith("!mcbotgetuser")) {
			MinecraftChatHandler.getInstance().messageQueue.add("MC BOT POSTING. Currently Using Username - " + launcher.getBot().getConfiguration().getName());
			return;
		}
		
//		if(event.getMessage().startsWith("!setserver")){
//			String message = event.getMessage();
//				MinecraftCurrentInfo.serverHost = message.substring(10);
//		}
		
		super.onMessage(event);
	}
}
