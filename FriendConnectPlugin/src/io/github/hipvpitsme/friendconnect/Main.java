package io.github.hipvpitsme.friendconnect;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import java.sql.Connection;


public class Main extends PluginBase {
    FormWindowSimple window;
    @Override
    public void onLoad() {
        this.getLogger().info(TextFormat.WHITE + "I've been loaded!");
    }
    @Override
    public void onEnable() {
        this.getLogger().info(TextFormat.DARK_GREEN + "I've been enabled!");
        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
    }
    @Override
    public void onDisable() {
        this.getLogger().info(TextFormat.DARK_RED + "I've been disabled!");
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (command.getName()) {
            case "example":
                try {
                    window = new FormWindowSimple("Test", "Test");
                    window.addButton(new ElementButton("Test"));
                    sender.getServer().getPlayer(sender.getName()).showFormWindow(window);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        return true;
    }
}
