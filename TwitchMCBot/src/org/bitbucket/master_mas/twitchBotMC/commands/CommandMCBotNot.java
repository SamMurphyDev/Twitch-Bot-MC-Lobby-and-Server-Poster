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

package org.bitbucket.master_mas.twitchBotMC.commands;

import java.awt.EventQueue;

import org.bitbucket.master_mas.twitchBotMC.Launcher;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;

public class CommandMCBotNot extends Command {

	public CommandMCBotNot(String command, String[] args, final Launcher launcher, MessageEvent<PircBotX> event) {
		super(command, args, launcher, event);
		if(!event.getUser().getLogin().equalsIgnoreCase(launcher.getBot().getConfiguration().getName())) {
			launcher.getBot().sendIRC().quitServer();
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					launcher.changeStatusLabel("A another bot instance already exists in chat", "red");
				}
			});
		}
	}
}
