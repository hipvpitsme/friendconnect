package io.github.hipvpitsme.friendconnect;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerLocallyInitializedEvent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class EventListener implements Listener {
    Main plugin;
    public EventListener(Main plugin) {
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
    public void onPlayerSpawn(PlayerLocallyInitializedEvent event) throws IOException {
//        MainWindow window = new MainWindow("Test", null);
////        try { TimeUnit.SECONDS.sleep(5); } catch (Exception e) {}
//        event.getPlayer().showFormWindow(window);
    }
}
