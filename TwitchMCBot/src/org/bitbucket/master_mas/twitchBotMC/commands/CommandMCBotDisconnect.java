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
import org.bitbucket.master_mas.twitchBotMC.MinecraftCurrentInfo;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;

public class CommandMCBotDisconnect extends Command {

	public CommandMCBotDisconnect(String command, String[] args,
			final Launcher launcher, MessageEvent<PircBotX> event) {
		super(command, args, launcher, event);
		
		if(event.getUser().getNick().equalsIgnoreCase(new String(MinecraftCurrentInfo.channel).replace("#", ""))) {
			String usernameOfBot = event.getMessage().replace("!mcbotdisconnect", "").trim();
			if(!(usernameOfBot.length() > 0))
				return;
			if(usernameOfBot.equalsIgnoreCase(launcher.getBot().getConfiguration().getName())) {
				launcher.getBot().sendIRC().quitServer();
				launcher.stopCheckerThread();
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						launcher.changeStatusLabel("Bot has been removed by Streamer", "blue");
					}
				});
			}
		}
		return;
	}

}
