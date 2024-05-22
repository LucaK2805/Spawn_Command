package test.spawn_command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Spawn_Command extends JavaPlugin {

    private Location spawnLocation;

    @Override
    public void onEnable() {
        getLogger().info("SpawnPlugin aktiviert!");

        // Lade den gespeicherten Spawnpunkt
        if (getConfig().contains("spawn")) {
            double x = getConfig().getDouble("spawn.x");
            double y = getConfig().getDouble("spawn.y");
            double z = getConfig().getDouble("spawn.z");
            float yaw = (float) getConfig().getDouble("spawn.yaw");
            float pitch = (float) getConfig().getDouble("spawn.pitch");
            spawnLocation = new Location(Bukkit.getWorld(getConfig().getString("spawn.world")), x, y, z, yaw, pitch);
        } else {
            spawnLocation = new Location(Bukkit.getWorlds().get(0), 0, 100, 0); // Standard-Spawnpunkt
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("SpawnPlugin deaktiviert!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spawn")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.teleport(spawnLocation);
                player.sendMessage(ChatColor.GREEN + "Du wurdest zum Spawn teleportiert!");
                return true;
            } else if (sender instanceof ConsoleCommandSender) {
                sender.sendMessage("Dieser Befehl kann nur von einem Spieler ausgeführt werden.");
                return true;
            }
        }

        if (cmd.getName().equalsIgnoreCase("setspawn")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                spawnLocation = player.getLocation();
                saveSpawnLocation();
                player.sendMessage(ChatColor.GREEN + "Spawnpunkt wurde gesetzt!");
                return true;
            } else {
                sender.sendMessage("Dieser Befehl kann nur von einem Spieler ausgeführt werden.");
                return true;
            }
        }

        return false;
    }

    private void saveSpawnLocation() {
        getConfig().set("spawn.world", spawnLocation.getWorld().getName());
        getConfig().set("spawn.x", spawnLocation.getX());
        getConfig().set("spawn.y", spawnLocation.getY());
        getConfig().set("spawn.z", spawnLocation.getZ());
        getConfig().set("spawn.yaw", spawnLocation.getYaw());
        getConfig().set("spawn.pitch", spawnLocation.getPitch());
        saveConfig();
    }
}
