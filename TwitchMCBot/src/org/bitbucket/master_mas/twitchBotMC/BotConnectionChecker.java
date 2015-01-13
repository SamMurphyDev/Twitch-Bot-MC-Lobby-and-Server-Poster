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
						launcher.changeStatusLabel("Bot awaiting to initialise", "red");
					}
				});
			else if(launcher.getBot().isConnected())
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						launcher.changeStatusLabel("Bot Connected and Running", "green");
					}
				});
			else if(!launcher.getBot().isConnected())
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						launcher.changeStatusLabel("Bot not Connected", "red");
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
