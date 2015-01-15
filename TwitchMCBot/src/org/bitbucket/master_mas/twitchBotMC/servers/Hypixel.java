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

package org.bitbucket.master_mas.twitchBotMC.servers;

import org.bitbucket.master_mas.twitchBotMC.MinecraftCurrentInfo;

public class Hypixel extends MinecraftServerHandler {
	//mc.hypixel.net
	
	@Override
	public void handle(String message) {
		if(message.contains("Sending you to")) {
			String text[] = message.split("Sending you to");
			text[1].replace("!", "");
			MinecraftCurrentInfo.currentServerRoomUUID = text[1];
			messageQueue(text[1], false);
		}
		return;
	}
}
