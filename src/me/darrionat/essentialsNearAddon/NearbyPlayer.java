package me.darrionat.essentialsNearAddon;

import org.bukkit.entity.Player;

public class NearbyPlayer {

	private Player player;
	private double distance;

	public NearbyPlayer(Player player, double distance) {
		this.player = player;
		this.distance = distance;
	}

	public Player getPlayer() {
		return player;
	}

	public double getDistance() {
		return distance;
	}
}