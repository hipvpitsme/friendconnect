package io.github.hipvpitsme.friendconnect.windowmodels;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import io.github.hipvpitsme.friendconnect.Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AddServer {
    private static Map<String, FormWindowCustom> userWindows = new HashMap<String, FormWindowCustom>();
    public static FormWindowCustom createFormWindow(String uuid) {
        removeUser(uuid);
        FormWindowCustom window = new FormWindowCustom("Add A Server");
        window.addElement(new ElementInput("Server IP", "Server IP"));
        window.addElement(new ElementInput("Server Port", "Server Port", "19132"));
        userWindows.put(uuid, window);
        return window;
    }

    public static void removeUser(String uuid) {
        if(userWindows.containsKey(uuid)) {
            userWindows.remove(uuid);
        }
        return;
    }

    public static String getServerIP(String uuid, Integer id) {
        return findFormWindow(uuid).getResponse().getInputResponse(id);
    }

    public static Integer getServerPort(String uuid, Integer id) {
        if(!findFormWindow(uuid).getResponse().getInputResponse(id).isEmpty()) {
            try {
                return Integer.parseInt(findFormWindow(uuid).getResponse().getInputResponse(id));
            } catch(Exception e) {
                return null;
            }
        }
        return null;
    }

    public static FormWindowCustom findFormWindow(String uuid) {
        if(userWindows.containsKey(uuid)) {
            FormWindowCustom window = userWindows.get(uuid);
            return window;
        }
        return null;
    }
}
