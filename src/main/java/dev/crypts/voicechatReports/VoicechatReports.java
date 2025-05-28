package dev.crypts.voicechatReports;

import de.maxhenkel.voicechat.api.BukkitVoicechatService;
import dev.crypts.voicechatReports.commands.SaveToFileCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class VoicechatReports extends JavaPlugin {

    public static final String PLUGIN_ID = "voicechat-reports";

    @Override
    public void onEnable() {
        BukkitVoicechatService service = getServer().getServicesManager().load(BukkitVoicechatService.class);
        if (service != null) {
            service.registerPlugin(new VoicechatReportsPlugin());
        }

        this.getCommand("save-to-file").setExecutor(new SaveToFileCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
