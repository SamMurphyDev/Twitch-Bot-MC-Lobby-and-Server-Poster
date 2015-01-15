This bot is a Twitch bot that you run from your computer that notify's Twitch of what Server and Lobby or Game your in.

This bot will post out any server you join. The list below tells you what servers the bot can also post out the Mini-Game you enter and/or the lobby you enter.

1. us.mineplex.com (Lobby / Mini-Game[ID])
2. eu.mineplex.com (Lobby / Mini-Game[ID])
3. mc.hypixel.net (Mini-Game[ID])
4. us.playmindcrack.com (Lobby / Mini-Game[Name])
5. play.hivemc.com (Mini-Game[Name])
6. play.mc-legends.com (Mini-Game[Name])

* Lobby - Can Post which Lobby you are in or section of lobby (E.g. Lobby or Island in Lobby)
* Lobby[ID] - Can Post which specific Lobby you are in (E.g Lobby-5)
* Mini-Game[ID] - Can post which specific mini-game you have joined (E.g. SG-5)
* Mini-Game[Name] - Can post the name of the mini-game you have joined (E.g. Survival Games)

Version - 1.0.2

[Download Here](https://bitbucket.org/master_mas/twitch-bot-mc-lobby-and-server-poster/downloads)

[License Agreement](https://bitbucket.org/master_mas/twitch-bot-mc-lobby-and-server-poster/src/4365330098ff0a859ee0544f315340ba381f2b96/license.txt)

### How do I get set up? ###

To run the program you double click it. Doesn't need to be installed or anything.

You will need the following for the program:

* The Twitch Username that the bot will be posting under. This could be a separate account or your main account
* The OAuth key for that account. It's like a password but different. This can be obtained from [Twitch Apps](http://twitchapps.com/tmi/)
* The Twitch username that the bot will be posting under. So if your the channel your live streaming on is http://twitch.tv/master_mas then the Twitch username is master_mas

Once done you can select if you would like the program to save your settings for next time.
You then can click 'Start Bot' If the status message changes to a Green 'Bot Connected and Running' then the bot is working.
If the app never changes to that state then it means something is wrong in the details.

**Make sure you start the program before you join a server otherwise it will never print any messages out**

To exit the program click the exit button and the bot will auto close itself.

### Once it's running ###
Once the program is running whenever you change lobbies, exit a mingame or join one, etc the program will tell Twitch chat of which lobby or minigame on which server you are.

##Errors and Status Messages##
The messages that appear in the bottom of the program appear in 4 different colours. Green, Blue, Red, Gray.
Gray - Means the bot is currently not started
Green - Means the bot is working normally
Blue - For a reason the bot has been disconnected from the channel
Red - Some sort of error has occured.

Gray - Bot Not Started : Bot hasn't yet started
Green - Bot Connected and Running : The bot has successfully connected to Twitch and has access to the channel for posting
Blue - Bot awaiting to initialise : The bot is in the process of starting
Blue - Bot has been removed by Streamer : The bot has been disconnected from the channel by Streamer request.
Red - Bot not Connected : Bot failed connection to Twitch server.
Red - A another bot instance already exists in chat : Another instance of the MCBot is already in the chat.

## Commands ##
* !server - Will post out the current server you're on
* !lobby - Will post out the current lobby you are in
* !minigame - WWill post out the current mini-game you are in
* !mcbotdisconnect <BotName> - Will disconnect that instance of the McBot
* !mcbotgetuser - Will get the username which the bot is using
* !mcbotmute - Will toggle the ability for the bot to post or not

## Caution ##
* **Never join a server that you don't want to release the ip of. It will auto post it before you know it.**