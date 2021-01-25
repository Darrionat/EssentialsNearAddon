package me.darrionat.essentialsNearAddon.services;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.darrionat.essentialsNearAddon.EssentialsNearAddon;
import me.darrionat.essentialsNearAddon.NearbyPlayer;

public class DistanceService {

	private EssentialsNearAddon plugin;

	public DistanceService(EssentialsNearAddon plugin) {
		this.plugin = plugin;
	}

	/**
	 * Returns all players on the server, excluding the passed player parameter, in
	 * Ascending order of distance.
	 * 
	 * @param player the center player of the search
	 * @return
	 */
	public List<NearbyPlayer> getClosestPlayers(Player player) {
		List<NearbyPlayer> playersToSort = new ArrayList<>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p == player) {
				continue;
			}
			if (!p.getWorld().equals(player.getWorld()))
				continue;
			double distance = getDistance(player, p);
			if (distance > plugin.getConfig().getInt("indicators.5.max"))
				continue;

			playersToSort.add(new NearbyPlayer(p, distance));
		}

		sortNearbyPlayers(playersToSort);
		return playersToSort;
	}

	private void sortNearbyPlayers(List<NearbyPlayer> toSort) {
		for (int i = 0; i < toSort.size(); i++) {
			for (int j = i + 1; j < toSort.size(); j++) {
				if (toSort.get(j).getDistance() < toSort.get(i).getDistance()) {
					NearbyPlayer temp = toSort.get(i);
					toSort.set(i, toSort.get(j));
					toSort.set(j, temp);
				}
			}
		}
	}

	/**
	 * Gets the distance between two players
	 * 
	 * @param p1 player one
	 * @param p2 player two
	 * @return returns the distance, in blocks, between two players
	 */
	private double getDistance(Player p1, Player p2) {
		Location loc1 = p1.getLocation();
		Location loc2 = p2.getLocation();

		double x1 = loc1.getX();
		double y1 = loc1.getY();
		double z1 = loc1.getZ();
		double x2 = loc2.getX();
		double y2 = loc2.getY();
		double z2 = loc2.getZ();

		double xDiffSqr = Math.pow(x1 - x2, 2);
		double yDiffSqr = Math.pow(y1 - y2, 2);
		double zDiffSqr = Math.pow(z1 - z2, 2);
		return Math.sqrt(xDiffSqr + yDiffSqr + zDiffSqr);
	}
}