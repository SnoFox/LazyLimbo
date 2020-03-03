package net.snofox.minecraft.lazylimbo;

import la.loa.crispy1989.LOALib.Teleports.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class LazyLimbo extends JavaPlugin {
    final private BaseComponent[] message = new ComponentBuilder("Nexus is down; reconnecting you shortly...")
            .color(ChatColor.RED).create();

    @Override
    public void onEnable() {
        registerTasks();
        getLogger().info("Lazily Launching Losers from Limbo");
    }

    void registerTasks() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this,
                () -> Bukkit.getOnlinePlayers().forEach(player -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, message)),
                20, 20);
        getServer().getScheduler().scheduleSyncRepeatingTask(this,
                () -> Bukkit.getOnlinePlayers().stream().filter((player) -> !player.isOp()).findFirst().ifPresent(this::sendToNexus),
                20, 20);
    }

    private void sendToNexus(final Player player) {
        BungeeCord.movePlayerToServer(player, "nexus");
    }
}
