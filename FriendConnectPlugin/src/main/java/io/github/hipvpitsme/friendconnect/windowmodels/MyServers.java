package io.github.hipvpitsme.friendconnect.windowmodels;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import io.github.hipvpitsme.friendconnect.Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MyServers {
    private static Map<String, FormWindowSimple> userWindows = new HashMap<String, FormWindowSimple>();
    public static FormWindowSimple createFormWindow(String uuid) {
        removeUser(uuid);
        FormWindowSimple window = new FormWindowSimple("My Servers", "");
        try {
            ResultSet rs = Main.c.createStatement().executeQuery("select * from players.servers where uuid = '"+uuid+"'");
            Boolean dataPresent = false;
            window.setContent("You do not have any servers!");
            window.addButton(new ElementButton("Add a Server"));
            window.addButton(new ElementButton("Remove a Server"));
            window.addButton(new ElementButton("Back To Main Screen"));
            window.addButton(new ElementButton("-----------------------------"));
            while (rs.next()) {
                dataPresent = true;
                window.addButton(new ElementButton(rs.getString("ip")+":"+rs.getString("port")));
            }
            if(dataPresent) {
                window.setContent("");
            }
        } catch(SQLException e) {
            System.out.println(e);
            return ConfirmationScreen.createFormWindow(uuid, "An Error Occurred!", "An error occurred while trying to load your servers!", MainWindow.createFormWindow(uuid));
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
