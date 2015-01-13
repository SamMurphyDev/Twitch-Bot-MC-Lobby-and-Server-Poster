package org.bitbucket.master_mas.twitchBotMC;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

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
		if(!new File(System.getenv("APPDATA") + "\\twitch\\bots\\mc").exists())
			new File(System.getenv("APPDATA") + "\\twitch\\bots\\mc").mkdirs();
		
		String location = System.getenv("APPDATA") + "\\twitch\\bots\\mc\\config.properties";
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
		} catch (IOException e) {
			e.printStackTrace();
			launcher.changeStatusLabel("Internals Failed. Restart Program", "red");
		} catch (IrcException e) {
			e.printStackTrace();
			launcher.changeStatusLabel("IRC Failed. Restart Program", "red");
		}
	}
}
