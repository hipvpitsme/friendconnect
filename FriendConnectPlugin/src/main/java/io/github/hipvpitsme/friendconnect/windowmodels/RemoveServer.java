package io.github.hipvpitsme.friendconnect.windowmodels;

import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowCustom;
import io.github.hipvpitsme.friendconnect.Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RemoveServer {
    private static Map<String, FormWindowCustom> userWindows = new HashMap<String, FormWindowCustom>();
    public static FormWindow createFormWindow(String uuid) {
        removeUser(uuid);
        FormWindowCustom window = new FormWindowCustom("Server Removal");
        try {
            ResultSet rs = Main.c.createStatement().executeQuery("select * from players.servers where uuid = '"+uuid+"'");
            List<String> servers = new ArrayList<>();
            Boolean dataPresent = false;
            while(rs.next()) {
                dataPresent = true;
                servers.add(rs.getString("ip")+":"+rs.getString("port"));
            }
            if(dataPresent) {
                window.addElement(new ElementDropdown("Select A Server To Remove!", servers));
            } else {
                return ConfirmationScreen.createFormWindow(uuid, "An Error Occurred!", "There are no servers to display!", MyServers.createFormWindow(uuid));
            }
        } catch (SQLException e) {
            return ConfirmationScreen.createFormWindow(uuid, "An Error Occurred!", "There was an error while getting your servers!", MyServers.createFormWindow(uuid));
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

    public static List<String> getUserResponse(String uuid) {
        List<String> ipPort = new ArrayList<>();
        ipPort.add(findFormWindow(uuid).getResponse().getDropdownResponse(0).getElementContent().split(":")[0]);
        ipPort.add(findFormWindow(uuid).getResponse().getDropdownResponse(0).getElementContent().split(":")[1]);
        return ipPort;
    }

    public static FormWindowCustom findFormWindow(String uuid) {
        if(userWindows.containsKey(uuid)) {
            FormWindowCustom window = userWindows.get(uuid);
            return window;
        }
        return null;
    }
}
