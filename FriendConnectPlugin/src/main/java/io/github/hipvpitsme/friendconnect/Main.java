package io.github.hipvpitsme.friendconnect;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.player.PlayerKickEvent;
import cn.nukkit.form.element.Element;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.postgresql.*;
import org.postgresql.Driver;

public class Main extends PluginBase {
    public static Connection c;
    @Override
    public void onEnable() {
        this.getLogger().info(TextFormat.DARK_GREEN + "I've been enabled!");
        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://127.0.0.1:5432/players", "admin", "admin");
        } catch (SQLException e) {
            this.getLogger().info("The plugin could not connect to the psotgresql server, therefore it will now exit.");
            getPluginLoader().disablePlugin(this);
        } catch (ClassNotFoundException e) {
            this.getLogger().info("The plugin could not find the postgresql driver, therefore it will now exit.");
            getPluginLoader().disablePlugin(this);
        }
    }
    @Override
    public void onDisable() {

        this.getLogger().info(TextFormat.DARK_RED + "I've been disabled!");
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (command.getName()) {
            case "addserver":
                if(args.length == 4) {
                    try {
                        Statement addserverstm = c.createStatement();
                        String sql = "insert into players.promotedservers (amountpaid, name, ip, port, clicks) values ("+ Float.parseFloat(args[0])+",'"+args[1].toString()+"','"+args[2].toString()+"',"+Long.parseLong(args[3])+", array[''])";
                        addserverstm.executeUpdate(sql);
                        addserverstm.close();
                        sender.sendMessage("Added the server " + args[2]+":"+args[3]+" to the server list, with the name of '"+args[1]+"' and the monthly payment being $"+args[0]);
                    } catch (SQLException e) {
                        sender.sendMessage("There was an error whilst adding this server! Please check the console to view the error further!");
                        this.getLogger().info(e.toString());
                    }
                } else {
                    sender.sendMessage("Please make sure you specified values for all of the fields!");
                }
                break;
            case "removeserver":
                if(args != null) {
                    try {
                        boolean serverFound = false;
                        ResultSet rs = c.createStatement().executeQuery("select * from players.promotedservers where name = '"+args[0]+"'");
                        while(rs.next()) {
                            if(!rs.getString("name").isEmpty()) {
                                serverFound = true;
                            }
                        }
                        if(serverFound) {
                            Statement deleteserverstm = c.createStatement();
                            String sql = "delete from players.promotedservers where name = '"+args[0]+"'";
                            deleteserverstm.executeUpdate(sql);
                            deleteserverstm.close();
                            sender.sendMessage("Deleted the server '"+args[0]+"'");
                        } else {
                            sender.sendMessage("No server with the name of '"+args[0]+"'");
                        }
                    } catch (SQLException e) {
                        sender.sendMessage("There was an error whilst deleting this server! Please check the console to view the error further!");
                        this.getLogger().info(e.toString());
                    }
                }else {
                    sender.sendMessage("Please make sure you specified values for all of the fields!");
                }
                break;
            case "listserver":
                try {
                    ResultSet rs = c.createStatement().executeQuery("select * from players.promotedservers order by amountpaid desc");
                    List<String> results = new ArrayList<>();
                    while(rs.next()) {
                        String result = "Server name: '"+rs.getString("name")+"', Server IP + Port: '"+rs.getString("ip")+":"+rs.getString("port")+"', Payment Amount: $"+rs.getString("amountpaid")+"\n";
                        results.add(result);
                    }
                    sender.sendMessage(String.join("", results));
                } catch (SQLException e) {
                    sender.sendMessage("There was an error while trying to list the promoted servers, please see console for more details!");
                    this.getLogger().info(e.toString());
                }
                break;
            case "adddefault":
                try {
                    Statement addserver = c.createStatement();
                    String sql = "insert into players.defaultservers (name, ip, port) values ('"+args[0]+"','"+args[1]+"',"+Long.parseLong(args[2])+")";
                    addserver.executeUpdate(sql);
                    addserver.close();
                    sender.sendMessage("Successfully added server '" + args[0] + "' to the server list!");
                } catch (Exception e) {
                    sender.sendMessage("There was an error while trying to add this server to the default server list!");
                }
                break;
            case "removedefault":
                try {
                    boolean serverFound = false;
                    ResultSet rs = c.createStatement().executeQuery("select * from players.defaultservers where name = '"+args[0]+"'");
                    while(rs.next()) {
                        if(!rs.getString("name").isEmpty()) {
                            serverFound = true;
                        }
                    }
                    if(serverFound) {
                        Statement removeserver = c.createStatement();
                        String sql = "delete from players.defaultservers where name = '"+args[0]+"'";
                        removeserver.executeUpdate(sql);
                        removeserver.close();
                        sender.sendMessage("Successfully removed server '" + args[0] + "' from the server list!");
                    } else {
                        sender.sendMessage("No server with the name '" + args[0] + "'!");
                    }
                } catch (Exception e) {
                    sender.sendMessage("There was an error while trying to remove this server from the default server list!");
                }
                break;
            case "serverstats":
                try {
                    ResultSet rs = c.createStatement().executeQuery("select * from players.promotedservers where name = '"+args[0]+"'");
                    while(rs.next()) {
                        Array playersFound = rs.getArray("clicks");
                        String[] players = (String[]) playersFound.getArray();
                        System.out.println(players.length-1);
                    }
                    rs.close();
                } catch (SQLException e) {
                    this.getLogger().info(e.toString());
                }
                break;
        }
        return true;
    }
}