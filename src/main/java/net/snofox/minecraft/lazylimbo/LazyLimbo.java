package net.snofox.minecraft.lazylimbo;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import la.loa.crispy1989.LOALib.Teleports.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class LazyLimbo extends JavaPlugin {
    final private BaseComponent[] message = new ComponentBuilder("Nexus is down; reconnecting you shortly")
            .color(ChatColor.RED).create();
    final private Cache<UUID, Boolean> recentSends = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build();

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
                () -> Bukkit.getOnlinePlayers().stream().filter(
                        (player) -> !player.isOp() && recentSends.getIfPresent(player.getUniqueId()) == null)
                        .findFirst().ifPresent(this::sendToNexus),
                20, 20);
    }

    private void sendToNexus(final Player player) {
        recentSends.put(player.getUniqueId(), true);
        BungeeCord.movePlayerToServer(player, "nexus");
    }
}
