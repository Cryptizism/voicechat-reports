package dev.crypts.voicechatReports.commands;

import dev.crypts.voicechatReports.data.PCMBufferStore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

public class SaveToFileCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            return false;
        }

        UUID uuid = player.getUniqueId();

        try {
            PCMBufferStore.getInstance().getOrCreateBuffer(uuid).convertPCMToWav();
        } catch (IOException e) {
            player.sendMessage(ChatColor.RED + "Could not save to file.");
            Bukkit.getLogger().log(Level.SEVERE, "Could not save to file", e);
        }

        return true;
    }
}
