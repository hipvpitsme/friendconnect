package io.github.hipvpitsme.friendconnect.windowmodels;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowSimple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfirmationScreen {
    private static Map<String, FormWindowSimple> userWindows = new HashMap<>();
    private static Map<String, FormWindow> windowRef = new HashMap<>();
    public static FormWindowSimple createFormWindow(String uuid, String title, String content, FormWindow refWindow) {
        int baseSpace = 13;
        removeUser(uuid);
        List<String> contentFull = new ArrayList<>();
        contentFull.add(content);
        for(int i = 0; i <= baseSpace - Math.ceil(content.length() / 34.0); i++) {
            contentFull.add("\n");
        }
        FormWindowSimple window = new FormWindowSimple(title, String.join(" ", contentFull));
        window.addButton(new ElementButton("Okay"));
        userWindows.put(uuid, window);
        windowRef.put(uuid, refWindow);
        return window;
    }

    public static void removeUser(String uuid) {
        if(userWindows.containsKey(uuid)) {
            userWindows.remove(uuid);
            windowRef.remove(uuid);
        }
        return;
    }

    public static FormWindow getRefWindow(String uuid) {
        return windowRef.get(uuid);
    }


    public static FormWindowSimple findFormWindow(String uuid) {
        if(userWindows.containsKey(uuid)) {
            FormWindowSimple window = userWindows.get(uuid);
            return window;
        }
        return null;
    }
}
