package org.bitbucket.master_mas.twitchBotMC;

public class BotMessagePoster implements Runnable {
	
	private Launcher launcher;
	private String channel = null;

	public BotMessagePoster(Launcher launcher) {
		this.launcher = launcher;
	}
	
	@Override
	public void run() {
		String line;
		
		while(true) {
			if(channel == null) {
				try {
					for(String channel : launcher.getBot().getConfiguration().getAutoJoinChannels().keySet()) {
						this.channel = channel;
					}
				} catch (Exception e)  { }
			} else {
				if((line = MinecraftChatHandler.getInstance().messageQueue.poll()) != null)
					launcher.getBot().sendIRC().message(channel, line);
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
