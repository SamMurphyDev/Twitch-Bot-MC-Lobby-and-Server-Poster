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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

public class BotHandler implements Runnable {
	private PircBotX bot;
	private Launcher launcher;
	private String username;
	private String password;
	private boolean settings;
	private String channel;
	
	public BotHandler(String username, String password, String channel, boolean settings, Launcher launcher) {
		this.username = username;
		this.password = password;
		this.channel = channel;
		this.settings = settings;
		this.launcher = launcher;
		
	}
	
	private void write(String username, String password, String channel) {
		if(!new File(Launcher.defaultDirectory() + "\\twitch\\bots\\mc").exists())
			new File(Launcher.defaultDirectory() + "\\twitch\\bots\\mc").mkdirs();
		
		String location = Launcher.defaultDirectory() + "\\twitch\\bots\\mc\\config.properties";
		Properties properties = new Properties();
		properties.put("username", username);
		properties.put("oauth", password);
		properties.put("channel", channel);
		try {
			properties.store(new FileWriter(new File(location)), "MC Bot Config");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		MinecraftCurrentInfo.channel = "#" + channel;
		
		if(settings)
			write(username, password, channel);
		
		Configuration<PircBotX> ircConfig = new Configuration.Builder<PircBotX>()
				.setLogin(username)
				.setName(username)
				.setServer("irc.twitch.tv", 6667, password)
				.addAutoJoinChannel("#" + channel)
				.addListener(new ChatListener(launcher))
				.buildConfiguration();
		
		launcher.setBot(bot = new PircBotX(ircConfig));
		try {
			bot.startBot();
		} catch(Exception e) {
			e.printStackTrace();
			//launcher.tryReconnection();
			launcher.changeStatusLabel("Bot Library error thrown. Trying to restart", "red");
		}
	}
}
