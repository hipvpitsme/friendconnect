package io.github.hipvpitsme.friendconnect;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerLocallyInitializedEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.utils.TextFormat;
import io.github.hipvpitsme.friendconnect.windowmanager.WindowManager;
import io.github.hipvpitsme.friendconnect.windowmodels.*;

import java.net.InetSocketAddress;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class EventListener implements Listener {
    Main plugin;
    FormWindowCustom window;
    public EventListener(Main plugin) {
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
    public void onPlayerSpawn(PlayerLocallyInitializedEvent event) {
        String uuid = event.getPlayer().getUniqueId().toString();
        window = MainWindow.createFormWindow(uuid);
        event.getPlayer().showFormWindow(window);
    }
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
    public void onPlayerResponse(PlayerFormRespondedEvent event) {
        String uuid = event.getPlayer().getUniqueId().toString();
        Player player = event.getPlayer();
        if(event.getWindow() instanceof FormWindowCustom) {
            //Main Window
            if(((FormWindowCustom) event.getWindow()).getTitle().equals("Select A Category")) {
                if (FindWindow.find("Select A Category", uuid).wasClosed()) {
                    player.showFormWindow(MainWindow.createFormWindow(uuid));
                } else {
                    if(MainWindow.getDropdownSelection(uuid, 0).equals("My Servers")) {
                        player.showFormWindow(MyServers.createFormWindow(uuid));
                    } else if(MainWindow.getDropdownSelection(uuid, 0).equals("Exit Server List")) {
                        WindowManager.removePlayerFromWindows(uuid);
                        player.kick("Thank you for using FriendConnect DNS!");
                    } else if(MainWindow.getDropdownSelection(uuid, 0).equals("Default Servers")) {
                        player.showFormWindow(DefaultServers.createFormWindow(uuid));
                    } else if(MainWindow.getDropdownSelection(uuid, 0).equals("Promoted Servers")) {
                        player.showFormWindow(PromotedServers.createFormWindow(uuid));
                    }
                }
            }
            //Add Server
            if(((FormWindowCustom) event.getWindow()).getTitle().equals("Add A Server")) {
                if (FindWindow.find("Add A Server", uuid).wasClosed()) {
                    player.showFormWindow(MyServers.createFormWindow(uuid));
                } else {
                    if(AddServer.getServerIP(uuid, 0).isEmpty() != true && AddServer.getServerPort(uuid, 1) != null) {
                        if(AddServer.getServerPort(uuid, 1) <= 65535 && AddServer.getServerPort(uuid, 1) >= 1024) {
                            boolean serverFound = false;
                            try {
                                ResultSet rs = plugin.c.createStatement().executeQuery("select * from players.servers where uuid = '" + uuid + "' and ip = '" + AddServer.getServerIP(uuid, 0) + "' and port = " + AddServer.getServerPort(uuid, 1) + ";");
                                while (rs.next()) {
                                    if (!rs.getString("uuid").isEmpty()) {
                                        serverFound = true;
                                    }
                                }
                            } catch (SQLException e) {
                                player.showFormWindow(ConfirmationScreen.createFormWindow(uuid, "An Error Occurred!", "An SQL Exception occured while trying to add that server! Please contact support!", AddServer.findFormWindow(uuid)));
                                plugin.getLogger().info(e.toString());
                            }
                            if(!serverFound) {
                                try {
                                    Statement addServer = plugin.c.createStatement();
                                    String sql = "insert into players.servers (uuid, ip, port) values ('" + uuid + "', '" + AddServer.getServerIP(uuid, 0) + "', " + AddServer.getServerPort(uuid, 1) + ")";
                                    addServer.executeUpdate(sql);
                                    addServer.close();
                                    player.showFormWindow(ConfirmationScreen.createFormWindow(uuid, "Success!", "The server was added to the list!", MyServers.createFormWindow(uuid)));
                                } catch (SQLException e) {
                                    player.showFormWindow(ConfirmationScreen.createFormWindow(uuid, "An Error Occurred!", "An SQL Exception occured while trying to add that server! Please contact support!", AddServer.findFormWindow(uuid)));
                                    plugin.getLogger().info(e.toString());
                                }
                            } else {
                                player.showFormWindow(ConfirmationScreen.createFormWindow(uuid, "An Error Occurred!", "This server already exists!", MyServers.findFormWindow(uuid)));
                            }
                        } else {
                            player.showFormWindow(ConfirmationScreen.createFormWindow(uuid, "An Error Occurred!", "Please Make Sure You Entered A Port Below The Number 65535 And Above 1024!", AddServer.findFormWindow(uuid)));
                        }
                    } else {
                        player.showFormWindow(ConfirmationScreen.createFormWindow(uuid, "An Error Occurred!", "Please Make Sure You Entered A Server IP And A Port!", AddServer.findFormWindow(uuid)));
                    }
                }
            }
            //Remove Server
            if(((FormWindowCustom) event.getWindow()).getTitle().equals("Server Removal")) {
                if(FindWindow.find("Server Removal",uuid).wasClosed()) {
                    player.showFormWindow(RemoveServer.createFormWindow(uuid));
                } else {
                    try {
                        Statement deleteserver = plugin.c.createStatement();
                        String sql = "delete from players.servers where uuid = '" + uuid + "' and ip = '" + RemoveServer.getUserResponse(uuid).get(0) + "' and port = " + RemoveServer.getUserResponse(uuid).get(1) + "";
                        deleteserver.executeUpdate(sql);
                        deleteserver.close();
                        player.showFormWindow(ConfirmationScreen.createFormWindow(uuid, "Success!", "You successfully deleted the server "+RemoveServer.getUserResponse(uuid).get(0)+":"+RemoveServer.getUserResponse(uuid).get(1)+" from your server list!", MyServers.createFormWindow(uuid)));
                    } catch (SQLException e) {
                        player.showFormWindow(ConfirmationScreen.createFormWindow(uuid, "An Error Occurred!", "An SQL exception occured while trying to delete that server!", MyServers.createFormWindow(uuid)));
                        plugin.getLogger().info(e.toString());
                    }
                }
            }
        } else if(event.getWindow() instanceof FormWindowSimple) {
            //My Servers
            if(((FormWindowSimple) event.getWindow()).getTitle().equals("My Servers")) {
                if(FindWindow.find("My Servers", uuid).wasClosed()) {
                    player.showFormWindow(MainWindow.createFormWindow(uuid));
                } else {
                    String buttonText = MyServers.getClickedButton(uuid).getText();
                    if(buttonText.contains(":")) {
                        try {
                            event.getPlayer().transfer(new InetSocketAddress(buttonText.split(":")[0], Integer.parseInt(buttonText.split(":")[1])));
                        } catch (Exception e) {
                            player.showFormWindow(ConfirmationScreen.createFormWindow(uuid,"An Error Occurred!","Error while transferring player " + event.getPlayer().getName() + " to server " + buttonText.split(":")[0] + ":" + buttonText.split(":")[1],MyServers.createFormWindow(uuid)));
                        }
                    } else if(buttonText.contains("-----------------------------")) {
                        player.showFormWindow(MyServers.createFormWindow(uuid));
                    } else if(buttonText.contains("Add a Server")) {
                        player.showFormWindow(AddServer.createFormWindow(uuid));
                    } else if(buttonText.contains("Remove a Server")) {
                        player.showFormWindow(RemoveServer.createFormWindow(uuid));
                    } else if(buttonText.contains("Back To Main Screen")) {
                        player.showFormWindow(MainWindow.createFormWindow(uuid));
                    }
                }
            }
            //Error Screen
            if(((FormWindowSimple) event.getWindow()).getTitle().equals("An Error Occurred!")) {
                if(FindWindow.find("An Error Occurred!", uuid).wasClosed()) {
                    player.showFormWindow(ConfirmationScreen.getRefWindow(uuid));
                } else {
                    player.showFormWindow(ConfirmationScreen.getRefWindow(uuid));
                }
            }
            //Success Screen
            if(((FormWindowSimple) event.getWindow()).getTitle().equals("Success!")) {
                if(FindWindow.find("Success!", uuid).wasClosed()) {
                    player.showFormWindow(ConfirmationScreen.findFormWindow(uuid));
                } else {
                    player.showFormWindow(ConfirmationScreen.getRefWindow(uuid));
                }
            }
            //Promoted Servers
            if(((FormWindowSimple) event.getWindow()).getTitle().equals("Promoted Servers")) {
                if(FindWindow.find("Promoted Servers", uuid).wasClosed()) {
                    player.showFormWindow(PromotedServers.createFormWindow(uuid));
                } else {
                    String buttonText = PromotedServers.getClickedButton(uuid).getText();
                    if(buttonText.equals("Back To Main Screen")) {
                        player.showFormWindow(MainWindow.createFormWindow(uuid));
                    } else if(buttonText.equals("-----------------------------")) {
                        player.showFormWindow(PromotedServers.createFormWindow(uuid));
                    } else {
                        String ip = "null";
                        int port = 19132;
                        try {
                            ResultSet rs = plugin.c.createStatement().executeQuery("select * from players.promotedservers where name = '" + buttonText + "'");
                            while(rs.next()) {
                                ip = rs.getString("ip");
                                port = rs.getInt("port");
                                Array playersFound = rs.getArray("clicks");
                                String[] players = (String[]) playersFound.getArray();
                                boolean playerFound = false;
                                for(String player1 : players) {
                                    if(player1.equals(uuid)) {
                                        playerFound = true;
                                    }
                                }
                                if(!playerFound) {
                                    Statement addPlayer = plugin.c.createStatement();
                                    String sql = "update players.promotedservers set clicks = array_append(clicks, '"+uuid+"') where name = '"+buttonText+"'";
                                    addPlayer.executeUpdate(sql);
                                    addPlayer.close();
                                }
                                player.transfer(new InetSocketAddress(ip, port));
                            }
                        } catch (SQLException e) {
                            plugin.getLogger().info(e.toString());
                        } catch (Exception e) {
                            player.showFormWindow(ConfirmationScreen.createFormWindow(uuid,"An Error Occurred!","Error while transferring player " + event.getPlayer().getName() + " to server " + ip + ":" + port,MyServers.createFormWindow(uuid)));
                        }
                    }
                }
            }
            //Default Servers
            if(((FormWindowSimple) event.getWindow()).getTitle().equals("Default Servers")) {
                if(FindWindow.find("Default Servers", uuid).wasClosed()) {
                    player.showFormWindow(DefaultServers.createFormWindow(uuid));
                } else {
                    String buttonText = DefaultServers.getClickedButton(uuid).getText();
                    if(buttonText.equals("Back To Main Screen")) {
                        player.showFormWindow(MainWindow.createFormWindow(uuid));
                    } else if(buttonText.equals("-----------------------------")) {
                        player.showFormWindow(DefaultServers.createFormWindow(uuid));
                    } else {
                        String ip = "mco.lbsg.net";
                        int port = 19132;
                        try {
                            ResultSet rs = plugin.c.createStatement().executeQuery("select * from players.defaultservers where name = '" + buttonText + "'");
                            while(rs.next()) {
                                ip = rs.getString("ip");
                                port = rs.getInt("port");
                            }
                            player.transfer(new InetSocketAddress(ip, port));
                        } catch (SQLException e) {
                            plugin.getLogger().info(e.toString());
                        } catch (Exception e) {
                            player.showFormWindow(ConfirmationScreen.createFormWindow(uuid,"An Error Occurred!","Error while transferring player " + event.getPlayer().getName() + " to server " + ip + ":" + port,MainWindow.createFormWindow(uuid)));
                            plugin.getLogger().info(e.toString());
                        }
                    }
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
    public void onPlayerLeave(PlayerQuitEvent event) {
        WindowManager.removePlayerFromWindows(event.getPlayer().getUniqueId().toString());
    }
}
