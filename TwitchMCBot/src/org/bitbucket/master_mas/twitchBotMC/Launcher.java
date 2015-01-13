package org.bitbucket.master_mas.twitchBotMC;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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

public class Launcher extends JFrame {

	private static final long serialVersionUID = -6830326952068691403L;
	private JLabel connectionStatus;
	private PircBotX bot;
	
	private Thread botThread;
	private Thread checkerThread;
	
	private Launcher instance;
	
	public static void main(String[] args) {
		new Launcher();
	}
	
	public Launcher() {
		this.instance = this;
		
		this.setSize(new Dimension(400, 300));
		this.setLocationRelativeTo(null);
		this.setTitle("Twitch MC Bot - 1.0.0");
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
		});
		
		this.add(buildInterface(read()));
		
		this.setVisible(true);
	}

	private JPanel buildInterface(Map<String, String> settings) {
		JPanel container = new JPanel();
		container.setBorder(new EmptyBorder(10, 10, 10, 10));
		container.setLayout(new GridLayout(11, 2, 5, 5));
		
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
		
		JLabel saveSettingsLabel = new JLabel("Save Configuration");
		container.add(saveSettingsLabel);
		
		final JCheckBox saveSettings = new JCheckBox();
		saveSettings.setSelected(true);
		container.add(saveSettings);
		
		JButton confirm = new JButton();
		confirm.setText("Start Bot");
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				((JButton)e.getSource()).setVisible(false);
				MinecraftCurrentInfo.castersChannel = username.getText();
				botThread = new Thread(new BotHandler(username.getText(), new String(oauthKey.getPassword()), channel.getText(), saveSettings.isSelected(), instance), "IRC Connection");
				botThread.start();
				checkerThread = new Thread(new BotConnectionChecker(instance), "Checker");
				checkerThread.start();
				new MinecraftPoller(instance);
				new Thread(new BotMessagePoster(instance)).start();
			}
		});
		container.add(confirm);
		
		connectionStatus = new JLabel("", SwingConstants.CENTER);
		changeStatusLabel("Bot not Started", "gray");
		container.add(connectionStatus);
		
		JLabel me = new JLabel("<html><font color='#bdbdbd'>Developed by Sam Murphy Independent Software Development</font></html>");
		container.add(me);
		
		return container;
	}
	
	private Map<String, String> read() {
		String location = System.getenv("APPDATA") + "\\twitch\\bots\\mc\\config.properties";
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
	
	public void changeStatusLabel(String status, String color) {
		connectionStatus.setText("<html><font color='" + color + "'>" + status + "</font></html>");
	}

	public void setBot(PircBotX pircBotX) {
		this.bot = pircBotX;
	}

	public PircBotX getBot() {
		return bot;
	}
	
	public void stopCheckerThread() {
		checkerThread.stop();
	}
	
	public void startCheckerThread() {
		checkerThread.start();
	}
}
