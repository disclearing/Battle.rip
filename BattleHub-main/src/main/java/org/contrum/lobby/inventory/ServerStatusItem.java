package org.contrum.lobby.inventory;

import cc.stormworth.core.server.ServerInstance;
import cc.stormworth.core.util.inventory.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class ServerStatusItem {

    private final String SPLIT = "#~#";

    private final ServerInstance serverInstance;

    private final ItemBuilder itemBuilder;

    public ServerStatusItem(ServerInstance serverInstance) {
        this.serverInstance = serverInstance;

        this.itemBuilder = new ItemBuilder(serverInstance.getServerInstanceType().getInventoryItem().clone());
        this.itemBuilder.setName(ChatColor.YELLOW + serverInstance.getServerName());

        this.itemBuilder.addLore("");
        setup();
    }

    private void setup() {

        String[] motd = serverInstance.getServerMotd().split(SPLIT);

        switch (serverInstance.getServerInstanceType()) {


            case MEETUP: {

                switch (motd[0]) {
                    default:
                        this.itemBuilder.addLore(ChatColor.GRAY + "Status: " + ChatColor.DARK_RED + "OFFLINE");
                        this.itemBuilder.setDurability((short) 15);
                        break;

                    case "LOBBY":
                        this.itemBuilder.addLore(ChatColor.GRAY + "Status: " + ChatColor.GREEN + "WAITING");
                        this.itemBuilder.addLore("");
                        this.itemBuilder.addLore(ChatColor.GRAY + "Players: " + ChatColor.WHITE + serverInstance.getOnlinePlayers());
                        this.itemBuilder.addLore("");
                        this.itemBuilder.addLore(ChatColor.GREEN.toString() + ChatColor.BOLD + "Click to Join!");
                        this.itemBuilder.setDurability((short) 5);
                        break;

                    case "PREGAME":
                    case "RUNNING":
                    case "INGAME":
                        this.itemBuilder.addLore(ChatColor.GRAY + "Status: " + ChatColor.RED + "INGAME");
                        this.itemBuilder.addLore("");
                        this.itemBuilder.addLore("§7Players: §f" + serverInstance.getOnlinePlayers() + "/24");
                        this.itemBuilder.addLore("");
                        this.itemBuilder.addLore(ChatColor.LIGHT_PURPLE + "Click to Spectate " + ChatColor.GRAY + "(Donators Only)");
                        this.itemBuilder.setDurability((short) 14);
                        break;

                }
                break;
            }

            case PRIVATE:
            case UHC: {

                if (motd.length >= 4 && motd[1] != null) {
                    String[] config = motd[2].split("///");
                    String[] scenarios = motd[3].split("###");

                    this.itemBuilder.addLore(ChatColor.RED + "Host: " + ChatColor.WHITE + config[3]);

                    this.itemBuilder.addLore("");

                    this.itemBuilder.addLore(ChatColor.RED + "Players: " + ChatColor.WHITE + serverInstance.getOnlinePlayers() + "/" + config[1]);

                    this.itemBuilder.addLore(ChatColor.RED + "Game Type: " + ChatColor.WHITE + motd[1].replace("[", "").replace("]", ""));

                    this.itemBuilder.addLore("");

                    this.itemBuilder.addLore(ChatColor.RED + "Scenarios:");

                    if (scenarios.length == 1) {
                        this.itemBuilder.addLore(ChatColor.GRAY + "- " + ChatColor.WHITE + "None");
                    } else {
                        for (String scen : scenarios) {
                            if (!scen.equalsIgnoreCase("Scenarios")) {
                                this.itemBuilder.addLore(ChatColor.GRAY + "- " + ChatColor.WHITE + scen);
                            }
                        }
                    }

                    this.itemBuilder.addLore("");

                    this.itemBuilder.addLore(ChatColor.RED + "Config:");
                    this.itemBuilder.addLore(ChatColor.GRAY + "Nether: " + ChatColor.WHITE + config[2]);
                    this.itemBuilder.addLore("");

                    switch (config[0]) {
                        case "OPEN":
                            this.itemBuilder.addLore(ChatColor.GREEN.toString() + ChatColor.BOLD + "Click to Join! ");
                            break;

                        case "PRE_WHITELIST":
                            this.itemBuilder.addLore(ChatColor.GREEN.toString() + ChatColor.BOLD + "Click to Join! " + ChatColor.GRAY + "(Pre Whitelisted)");
                            break;

                        case "CLOSED":
                            if (config[4].equalsIgnoreCase("true")) {
                                this.itemBuilder.addLore(ChatColor.RED.toString() + ChatColor.BOLD + "Game has already started.");
                                break;
                            }

                            this.itemBuilder.addLore(ChatColor.RED.toString() + ChatColor.BOLD + "The server is whitelisted.");
                            break;
                    }

                } else {
                    itemBuilder.addLore(ChatColor.GRAY + "Join " + ChatColor.YELLOW + serverInstance.getOnlinePlayers() + ChatColor.GRAY + " other players on " + ChatColor.YELLOW + serverInstance.getServerName() + ChatColor.GRAY + "!");
                }

                break;
            }

            default: {
                itemBuilder.addLore(ChatColor.GRAY + "Join " + ChatColor.YELLOW + serverInstance.getOnlinePlayers() + ChatColor.GRAY + " other players on " + ChatColor.YELLOW + serverInstance.getServerName() + ChatColor.GRAY + "!");
                break;
            }

        }
    }

    public ItemStack getItem() {
        return itemBuilder.toItemStack();
    }
}
