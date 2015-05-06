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
import java.util.TimerTask;

import org.bitbucket.master_mas.twitchBotMC.commands.CommandMCBotNot;

public class BotConnectionChecker implements Runnable {

	private final Launcher launcher;
	private String lastMessage = "";
	
	public BotConnectionChecker(Launcher launcher) {
		this.launcher = launcher;
	}
	
	@Override
	public void run() {
		launcher.getTimer().schedule(new TimerTask() {
			@Override
			public void run() {
				CommandMCBotNot.timeup = true;
			}
		}, 10000);
		
		while(true) {
			if(launcher.getBot() == null)
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						String message = "Bot awaiting to initialise";
						if(!lastMessage.equals(message)) {
							launcher.changeConnectionStatusLabel(message, "blue");
							lastMessage = message;
						}
					}
				});
			else if(launcher.getBot().isConnected())
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						String message = "Bot Connected and Running";
						if(!lastMessage.equals(message)) {
							launcher.changeConnectionStatusLabel(message, "green");
							lastMessage = message;
						}
					}
				});
			else if(!launcher.getBot().isConnected())
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						String message = "Bot can't reach server";
						if(!lastMessage.equals(message)) {
							launcher.changeConnectionStatusLabel(message, "red");
							lastMessage = message;
						}
					}
				});
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
