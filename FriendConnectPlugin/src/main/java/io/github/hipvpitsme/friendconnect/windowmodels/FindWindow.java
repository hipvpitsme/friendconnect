package io.github.hipvpitsme.friendconnect.windowmodels;

import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowCustom;

public class FindWindow {
    public static FormWindow find(String title, String uuid) {
        if(title.equals("Select A Category")) {
            return MainWindow.findFormWindow(uuid);
        } else if(title.equals("My Servers")) {
            return MyServers.findFormWindow(uuid);
        } else if(title.equals("Add A Server")) {
            return AddServer.findFormWindow(uuid);
        } else if(title.equals("An Error Occurred!") || title.equals("Success!")) {
            return ConfirmationScreen.findFormWindow(uuid);
        } else if(title.equals("Server Removal")) {
            return RemoveServer.findFormWindow(uuid);
        } else if(title.equals("Promoted Servers")) {
            return PromotedServers.findFormWindow(uuid);
        } else if(title.equals("Default Servers")) {
            return DefaultServers.findFormWindow(uuid);
        }
        return null;
    }
}
