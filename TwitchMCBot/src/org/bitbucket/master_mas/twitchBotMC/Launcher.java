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

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.pircbotx.PircBotX;

import com.sun.net.ssl.HttpsURLConnection;

@SuppressWarnings("deprecation")
public class Launcher extends JFrame {

	private static final long serialVersionUID = -6830326952068691403L;
	private static Dimension programRes = new Dimension(400, 330);
	private final static String programTitle = "MC Bot";
	private JLabel connectionStatus;
	private JLabel otherStatus;
	private PircBotX bot;
	private Timer timer;
	private TrayIcon trayIcon = null;
	
	private Thread botThread;
	private Thread checkerThread;
	
	private Launcher instance;
	private String oauthKey;
	private String channel;
	private boolean saveSettings;
	
	public static void main(String[] args) {
		new Launcher(true);
	}
	
	public Launcher(boolean gui) {
		this.instance = this;
		timer = new Timer();
		
		if(gui) {
			this.setSize(programRes);
			this.setLocationRelativeTo(null);
			this.setTitle(programTitle + " - 1.0.3");
			this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			this.setResizable(false);
			this.setIconImage(null);
			
			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					super.windowClosing(e);
					String buttons[] = {"Yes", "No"};
					int result = JOptionPane.showOptionDialog(
							null, "Are you sure you want to exit?", 
							instance.getTitle(), 
							JOptionPane.DEFAULT_OPTION, 
							JOptionPane.WARNING_MESSAGE, 
							null, buttons, buttons[1]);
					if(result == 0)
						System.exit(0);
				}
				
				@Override
				public void windowIconified(WindowEvent e) {
					super.windowIconified(e);
					setVisible(false);
				}
			});
			
			this.add(buildInterface(read()));
			
			this.setVisible(true);
			
			if(SystemTray.isSupported()) {
				SystemTray tray = SystemTray.getSystemTray();
				
				Image image = Toolkit.getDefaultToolkit().getImage("assets/twitchMCBot.png");
				
				ActionListener listener = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
					}
				};
				
				PopupMenu popup = new PopupMenu();
				
				MenuItem defaultItem = new MenuItem("IDK");
				defaultItem.addActionListener(listener);
				
				popup.add(defaultItem);
				
				trayIcon = new TrayIcon(image, programTitle);
				trayIcon.setImageAutoSize(true);
				trayIcon.addMouseListener(new MouseListener() {
					@Override
					public void mouseReleased(MouseEvent arg0) { }
					
					@Override
					public void mousePressed(MouseEvent e) {
						if(e.getClickCount() >= 2) {
							setVisible(true);
							setState(JFrame.NORMAL);
						}
					}
					
					@Override
					public void mouseExited(MouseEvent arg0) { }
					
					@Override
					public void mouseEntered(MouseEvent arg0) { }
					
					@Override
					public void mouseClicked(MouseEvent arg0) { }
				});
				
				try {
					tray.add(trayIcon);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private JPanel buildInterface(Map<String, String> settings) {
		final JPanel container = new JPanel();
		container.setBorder(new EmptyBorder(10, 10, 10, 10));
		container.setLayout(new GridLayout(12, 2, 5, 5));
		
		JLabel userNameLabel = new JLabel("Username of bot");
		container.add(userNameLabel);
		
		final JTextField username = new JTextField();
		username.setToolTipText("Username of the Account to Post");
		if(settings != null)
			username.setText(settings.get("username"));
		container.add(username);
		
		JLabel oAuthKeyLabel = new JLabel("OAuth Key of Bot");
		container.add(oAuthKeyLabel);
		
		final JPasswordField oauthKey = new JPasswordField();
		oauthKey.setToolTipText("Get a OAuth Key at http://twitchapps.com/tmi/");
		if(settings != null)
			oauthKey.setText(settings.get("oauth"));
		container.add(oauthKey);
		
		JLabel channelLabel = new JLabel("Username of Twitch Channel to post on");
		container.add(channelLabel);
		
		final JTextField channel = new JTextField();
		channel.setToolTipText("Username of Channel to Post On");
		if(settings != null)
			channel.setText(settings.get("channel"));
		container.add(channel);
		
		final JLabel saveSettingsLabel = new JLabel("Save Configuration");
		container.add(saveSettingsLabel);
		
		final JCheckBox saveSettings = new JCheckBox();
		saveSettings.setSelected(true);
		container.add(saveSettings);
		
		JButton confirm = new JButton();
		confirm.setText("Start Bot");
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connectBot(username.getText(), new String(oauthKey.getPassword()), channel.getText(), saveSettings.isSelected());
				
				container.remove((JButton)e.getSource());
				container.remove(saveSettings);
				container.remove(saveSettingsLabel);
				username.setEditable(false);
				oauthKey.setEditable(false);
				channel.setEditable(false);
				programRes.height = 300;
				instance.setSize(programRes);
				
				container.setLayout(new GridLayout(9, 2, 5, 5));
			}
		});
		container.add(confirm);
		
		connectionStatus = new JLabel("", SwingConstants.CENTER);
		changeConnectionStatusLabel("Bot not Started", "gray");
		container.add(connectionStatus);
		
		otherStatus = new JLabel("", SwingConstants.CENTER);
		changeStatusLabel("Nothing to Report", "gray");
		container.add(otherStatus);
		
		JLabel me = new JLabel("<html><font color='#bdbdbd'>Developed by Sam Murphy Independent Software Development</font></html>", SwingConstants.CENTER);
		me.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if(Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().browse(new URI("http://www.sammurphysoftware.com"));
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		container.add(me);
		
		return container;
	}
	
	public void tryReconnection() {
		System.out.println("Happened");
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					System.out.println("Happening");
					EventQueue.invokeLater(new Runnable() {
						@Override
						public void run() {
							changeStatusLabel("Attempting Reconnection to Twitch", "red");
						}
					});
					
					try {
						URL url = new URL("http://twitch.tv");
						HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
						con.connect();
						if(con.getResponseCode() == 200)
							break;
					} catch (Exception e) { }
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				changeStatusLabel("Internet Connection Established", "green");
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						connectBot(MinecraftCurrentInfo.castersChannel, oauthKey, channel, saveSettings);
					}
				});
			}
		}).start();
	}
	
	public void connectBot(String username, String oauthKey, String channel, boolean saveSettings) {
		MinecraftCurrentInfo.castersChannel = username;
		this.oauthKey = oauthKey;
		this.channel = channel;
		this.saveSettings = saveSettings;
		botThread = new Thread(new BotHandler(username, oauthKey, channel, saveSettings, instance), "IRC Connection");
		botThread.start();
		checkerThread = new Thread(new BotConnectionChecker(instance), "Checker");
		checkerThread.start();
		new MinecraftPoller(instance);
		new Thread(new BotMessagePoster(instance)).start();
	}
	
	public void stopBot() {
		System.exit(0);
	}
	
	private Map<String, String> read() {
		String location = defaultDirectory() + "\\twitch\\bots\\mc\\config.properties";
		System.out.println("Location: " + location);
		File file = new File(location);
		if(!file.exists())
			return null;
		
		Properties properties = new Properties();
		Map<String, String> data = new HashMap<String, String>();
		try {
			properties.load(new FileReader(file));
			data.put("username", properties.getProperty("username"));
			data.put("oauth", properties.getProperty("oauth"));
			data.put("channel", properties.getProperty("channel"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	private TimerTask currentStatusTimer = null;
	private String lastOtherStatus = "";
	public void changeStatusLabel(String status, String color) {
		if(otherStatus == null)
			return;
		
		otherStatus.setText("<html><font color='" + color + "'>" + status + "</font></html>");
		
		if(currentStatusTimer != null)
			currentStatusTimer.cancel();
		
		if(!this.isVisible())
			if(trayIcon != null)
				if(!status.equals("Nothing to Report"))
					if(!status.equals(lastOtherStatus)) {
						trayIcon.displayMessage("Warning", status, MessageType.WARNING);
						lastOtherStatus = status;
					}
		
		timer.schedule(currentStatusTimer = new TimerTask() {
			@Override
			public void run() {
				instance.changeStatusLabel("Nothing to Report", "gray");
				currentStatusTimer = null;
			}
		}, 1000 * 30);
	}
	
	public void changeConnectionStatusLabel(String status, String color) {
		if(color.equalsIgnoreCase("red"))
			if(trayIcon != null)
				trayIcon.displayMessage("Connection Problem", status, MessageType.ERROR);
		
		if(connectionStatus != null)
			connectionStatus.setText("<html><font color='" + color + "'>" + status + "</font></html>");
	}

	public void setBot(PircBotX pircBotX) {
		this.bot = pircBotX;
	}

	public PircBotX getBot() {
		return bot;
	}
	
	public void startCheckerThread() {
		checkerThread.start();
	}
	
	public Timer getTimer() {
		return timer;
	}
	
	public static String defaultDirectory() {
	    String OS = System.getProperty("os.name").toUpperCase();
	    if (OS.contains("WIN"))
	        return System.getenv("APPDATA");
	    else if (OS.contains("MAC"))
	        return System.getProperty("user.home") + "/Library/Application "
	                + "Support";
	    else if (OS.contains("NUX"))
	        return System.getProperty("user.home");
	    return System.getProperty("user.dir");
	}
	
	public Launcher getInstance() {
		return instance;
	}
}
