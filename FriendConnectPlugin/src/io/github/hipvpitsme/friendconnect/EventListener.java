package io.github.hipvpitsme.friendconnect;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;

public class EventListener implements Listener {
    Main plugin;
    public EventListener(Main plugin) {
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
    public void onResponse(PlayerFormRespondedEvent event) {
        plugin.getLogger().info(plugin.window.getResponse().getClickedButton().getText());
    }
}
