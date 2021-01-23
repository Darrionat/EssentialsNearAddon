package me.darrionat.essentialsNearAddon;

import org.bukkit.plugin.java.JavaPlugin;

import me.darrionat.essentialsNearAddon.commands.NearCommand;
import me.darrionat.essentialsNearAddon.services.DistanceService;

public class EssentialsNearAddon extends JavaPlugin {

	@Override
	public void onEnable() {
		if (getServer().getPluginManager().getPlugin("Essentials") == null) {
			getLogger().severe(String.format(getName() + " - Disabled due to no Essentials dependency found!",
					getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		saveDefaultConfig();
		DistanceService distanceService = new DistanceService(this);
		new NearCommand(this, distanceService);
	}

	@Override
	public void onDisable() {

	}
}