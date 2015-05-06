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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.bitbucket.master_mas.twitchBotMC.Launcher;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;

import com.sun.istack.internal.logging.Logger;

public class CommandHandler {

	private Launcher launcher;

	public enum CommandsList {
		IP,
		SERVER,
		LOBBY,
		MINIGAME,
		MCBOTNOT,
		MCBOTGETUSER,
		MCBOTDISCONNECT,
		MCBOTMUTE,
		MCBOTAVAILABLE;
	}
	
	private final List<CommandsList> commandsThatHaveBeenRun = new ArrayList<CommandsList>();
	private final Map<CommandsList, Integer> spamCounter = new HashMap<CommandsList, Integer>();
	
	public CommandHandler(Launcher launcher) {
		this.launcher = launcher;
		
		for(CommandsList value : CommandsList.values())
			spamCounter.put(value, 0);
	}
	
	public void handle(String command, String[] args, MessageEvent<PircBotX> event) {
		Logger.getLogger(CommandHandler.class).info("Handling Command: " + command);
		
		final CommandsList commandV;
		try {
			commandV = CommandsList.valueOf(command.replace("!", "").split(" ")[0].toUpperCase());
		} catch (Exception e) {
			return;
		}
		
		if(commandV == null)
			return;
		
		if(commandsThatHaveBeenRun.contains(commandV)) {
			if(spamCounter.containsKey(commandV)) {
				spamCounter.put(commandV, spamCounter.get(commandV) + 1);
				
				if(spamCounter.get(commandV) > 2)
					launcher.changeStatusLabel("The chat is spamming the command: " + commandV, "red");
			}
			
			return;
		}
			
		
		commandsThatHaveBeenRun.add(commandV);
		launcher.getTimer().schedule(new TimerTask() {
			@Override
			public void run() {
				commandsThatHaveBeenRun.remove(commandV);
				spamCounter.put(commandV, 0);
			}
		}, 5000);
		
		switch(commandV) {
			case LOBBY:
				new CommandLocation(command, args, launcher, event);
				break;
			case MCBOTDISCONNECT:
				new CommandMCBotDisconnect(command, args, launcher, event);
				break;
			case MCBOTGETUSER:
				new CommandMCBotGetUser(command, args, launcher, event);
				break;
			case MCBOTMUTE:
				new CommandMCBotMute(command, args, launcher, event);
				break;
			case MCBOTNOT:
				new CommandMCBotNot(command, args, launcher, event);
				break;
			case MINIGAME:
				new CommandLocation(command, args, launcher, event);
				break;
			case IP:
			case SERVER:
				new CommandServer(command, args, launcher, event);
				break;
			case MCBOTAVAILABLE:
				new CommandMCBotAvailable(command, args, launcher, event);
			default:
				System.err.println(commandV + " doesn't have a registered command class");
				break;
			
		}
	}
}
