package io.github.hipvpitsme.friendconnect.windowmodels;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import io.github.hipvpitsme.friendconnect.Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PromotedServers {
    private static Map<String, FormWindowSimple> userWindows = new HashMap<String, FormWindowSimple>();
    public static FormWindowSimple createFormWindow(String uuid) {
        removeUser(uuid);
        FormWindowSimple window = new FormWindowSimple("Promoted Servers", "");
        try {
            ResultSet rs = Main.c.createStatement().executeQuery("select * from players.promotedservers order by amountpaid desc");
            Boolean dataPresent = false;
            window.setContent("There Are No Servers To Display!");
            window.addButton(new ElementButton("Back To Main Screen"));
            window.addButton(new ElementButton("-----------------------------"));
            while (rs.next()) {
                dataPresent = true;
                window.addButton(new ElementButton(rs.getString("name")));
            }
            if(dataPresent) {
                window.setContent("");
            }
        } catch(SQLException e) {
            System.out.println(e);
            return ConfirmationScreen.createFormWindow(uuid, "An Error Occurred!", "An error occurred while trying to load the promoted servers!", MainWindow.createFormWindow(uuid));
        }

        userWindows.put(uuid, window);
        return window;
    }

    public static void removeUser(String uuid) {
        if(userWindows.containsKey(uuid)) {
            userWindows.remove(uuid);
        }
        return;
    }

    public static ElementButton getClickedButton(String uuid) {
        return findFormWindow(uuid).getResponse().getClickedButton();
    }

    public static FormWindowSimple findFormWindow(String uuid) {
        if(userWindows.containsKey(uuid)) {
            FormWindowSimple window = userWindows.get(uuid);
            return window;
        }
        return null;
    }
}
