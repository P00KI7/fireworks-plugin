package me.pook.welcome;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WelcomeFireworks extends JavaPlugin implements Listener  {@Override
public void onEnable() {
    getLogger().info("WelcomeFireworks has started!");
    getServer().getPluginManager().registerEvents(this, this);
}

    @Override
    public void onDisable() {
        getLogger().info("WelcomeFireworks has stopped!");
    }

    // Trigger fireworks when a player joins
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Location loc = event.getPlayer().getLocation();
        launchFirework(loc);
        event.getPlayer().sendMessage("§6§lWelcome to the server, " + event.getPlayer().getName() + "!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("fireworks")) {

            // Case 1: /fireworks (no args) -> launch at sender
            if (args.length == 0) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    launchMultipleFireworks(player.getLocation());
                    player.sendMessage("§4§lBoom! Fireworks just for you! 🎆");
                } else {
                    sender.sendMessage("§cOnly players can use this without a name!");
                }
                return true;
            }

            // Case 2: /fireworks <player>
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage("§cThat player is not online!");
                return true;
            }

            launchMultipleFireworks(target.getLocation());
            sender.sendMessage("§aLaunched fireworks for " + target.getName() + "!");
            target.sendMessage("§4§lBoom! Fireworks just for you! 🎆");
            return true;
        }
        return false;
    }

    // Launch multiple fireworks
    private void launchMultipleFireworks(Location loc) {
        for (int i = 0; i < 6; i++) {
            launchFirework(loc);
        }
    }


    // Helper method to launch a firework
    private void launchFirework(Location loc) {
        Firework fw = loc.getWorld().spawn(loc, Firework.class);
        FireworkMeta meta = fw.getFireworkMeta();
        meta.addEffect(FireworkEffect.builder()
                .withColor(Color.AQUA, Color.LIME, Color.RED, Color.PURPLE, Color.YELLOW, Color.ORANGE)
                .withFade(Color.WHITE, Color.SILVER) // fade-out colors
                .with(FireworkEffect.Type.BALL_LARGE)
                .trail(true)
                .flicker(true)
                .build());
        meta.setPower(0); // 0 = explode lower / right away
        fw.setFireworkMeta(meta);

        // Detonate quickly so it doesn't fly up too much
        Bukkit.getScheduler().runTaskLater(this, fw::detonate, 2L);
    }
}

