package me.darrionat.essentialsNearAddon.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.darrionat.essentialsNearAddon.EssentialsNearAddon;
import me.darrionat.essentialsNearAddon.NearbyPlayer;
import me.darrionat.essentialsNearAddon.services.DistanceService;
import me.darrionat.essentialsNearAddon.utils.Utils;

public class NearCommand implements CommandExecutor {

	private final String squareUnicode = "\\u2B1B";
	private FileConfiguration config;
	private DistanceService distanceService;

	private final String permission = "essentials.near";

	public NearCommand(EssentialsNearAddon plugin, DistanceService distanceService) {
		this.config = plugin.getConfig();
		this.distanceService = distanceService;
		plugin.getCommand("near").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}

		Player p = (Player) sender;

		if (!p.hasPermission(permission)) {
			p.sendMessage(Utils.chat(config.getString("messages.noPermission").replace("%permission%", permission)));
			return true;
		}

		List<NearbyPlayer> closestPlayers = distanceService.getClosestPlayers(p);

		if (closestPlayers.size() == 0) {
			p.sendMessage(Utils.chat(config.getString("messages.noPlayersNearby")));
			return true;
		}

		if (config.getBoolean("sendHeader"))
			p.sendMessage(Utils.chat(config.getString("messages.header")));

		sendPlayerMessages(p, closestPlayers);

		if (config.getBoolean("sendFooter"))
			p.sendMessage(Utils.chat(config.getString("messages.footer")));
		return true;
	}

	private void sendPlayerMessages(Player p, List<NearbyPlayer> closestPlayers) {
		for (int i = 0; i < closestPlayers.size(); i++) {
			NearbyPlayer nearbyPlayer = closestPlayers.get(i);
			String msg = getNearMessage(nearbyPlayer.getPlayer().getName(), nearbyPlayer.getDistance());
			if (msg != null)
				p.sendMessage(msg);
			if (config.getInt("maxListSize") == i + 1)
				break;
		}
	}

	private String getNearMessage(String playerName, double distance) {
		if (distance > config.getInt("indicators.5.max"))
			return null;

		String msg = config.getString("messages.nearMessage");
		int value = 1;
		for (int i = 2; i <= 5; i++) {
			if (distance > config.getInt("indicators." + i + ".min")) {
				value = i;
			}
		}

		msg = msg.replace("%indicator%", config.getString("indicators." + value + ".message"));
		msg = msg.replace("%square%", squareUnicode);
		msg = msg.replace("%playerName%", playerName);
		msg = msg.replace("%distance%", String.valueOf((int) distance));
		return Utils.chat(msg);
	}
}