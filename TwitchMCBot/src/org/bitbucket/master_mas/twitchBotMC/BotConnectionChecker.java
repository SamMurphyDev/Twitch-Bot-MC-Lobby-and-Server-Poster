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

public class BotConnectionChecker implements Runnable {

	private final Launcher launcher;
	
	public BotConnectionChecker(Launcher launcher) {
		this.launcher = launcher;
	}
	
	@Override
	public void run() {
		while(true) {
			if(launcher.getBot() == null)
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						launcher.changeConnectionStatusLabel("Bot awaiting to initialise", "blue");
					}
				});
			else if(launcher.getBot().isConnected())
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						launcher.changeConnectionStatusLabel("Bot Connected and Running", "green");
					}
				});
			else if(!launcher.getBot().isConnected())
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						launcher.changeConnectionStatusLabel("Bot not Connected", "red");
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
