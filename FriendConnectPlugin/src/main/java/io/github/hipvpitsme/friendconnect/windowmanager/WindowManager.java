package io.github.hipvpitsme.friendconnect.windowmanager;

import io.github.hipvpitsme.friendconnect.windowmodels.*;

public class WindowManager {
    public static void removePlayerFromWindows(String uuid) {
        MainWindow.removeUser(uuid);
        MyServers.removeUser(uuid);
        AddServer.removeUser(uuid);
        ConfirmationScreen.removeUser(uuid);
        PromotedServers.removeUser(uuid);
        RemoveServer.removeUser(uuid);
        DefaultServers.removeUser(uuid);
    }
}
