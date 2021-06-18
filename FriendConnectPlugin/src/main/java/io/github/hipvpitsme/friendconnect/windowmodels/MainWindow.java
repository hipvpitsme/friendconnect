package io.github.hipvpitsme.friendconnect.windowmodels;

import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.window.FormWindowCustom;

import java.sql.Array;
import java.util.*;

public class MainWindow {
    private static Map<String, FormWindowCustom> userWindows = new HashMap<String, FormWindowCustom>();
    public static FormWindowCustom createFormWindow(String uuid) {
        removeUser(uuid);
        List<String> choices = Arrays.asList("Promoted Servers", "Default Servers", "My Servers", "Exit Server List");
        FormWindowCustom window = new FormWindowCustom("Select A Category");
        window.addElement(new ElementDropdown("", choices));
        userWindows.put(uuid, window);
        return window;
    }

    public static void removeUser(String uuid) {
        if(userWindows.containsKey(uuid)) {
            userWindows.remove(uuid);
        }
        return;
    }

    public static String getDropdownSelection(String uuid, Integer id) {
        if(findFormWindow(uuid) != null) {
            return findFormWindow(uuid).getResponse().getDropdownResponse(id).getElementContent();
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
