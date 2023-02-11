package org.contrum.lobby.inventory.inventories;

import cc.stormworth.core.server.ServerType;
import cc.stormworth.core.util.inventory.ItemBuilder;
import org.contrum.lobby.inventory.InventoryManager;
import org.contrum.lobby.inventory.ServerInventory;
import org.contrum.lobby.server.ServerManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MainMenuInventory extends ServerInventory {

    private final ServerManager serverManager;
    private final InventoryManager inventoryManager;

    public MainMenuInventory(ServerManager serverManager, InventoryManager inventoryManager) {
        super(ChatColor.DARK_GRAY + "\u00bb " + ChatColor.RED + "Server Selector", 45);

        this.serverManager = serverManager;
        this.inventoryManager = inventoryManager;

        update();
    }

    public void update() {

        // HCF
        setItem(12, new ItemBuilder(Material.FISHING_ROD)
                .setName(ChatColor.GOLD + "HCF")
                .addLore(ChatColor.DARK_GRAY + "HardCore Factions")
                .addLore(ChatColor.GRAY + "")
                .addLore(ChatColor.WHITE + "Create a team with other players")
                .addLore(ChatColor.WHITE + "and compete with other factions")
                .addLore(ChatColor.WHITE + "to become the best. Free Kits,")
                .addLore(ChatColor.WHITE + "Battle Pass and un-seen items.")
                .addLore(ChatColor.GRAY + "")
                .addLore(ChatColor.WHITE + "Regions" + ChatColor.GRAY + ":" + ChatColor.YELLOW + " North America")
                .addLore(ChatColor.WHITE + "Total Players" + ChatColor.GRAY + ":" + ChatColor.YELLOW + serverManager.getCountForType(ServerType.HCF) + ChatColor.YELLOW + " / 1,700 ")
                .addLore(ChatColor.GRAY + "")
                .addLore(ChatColor.YELLOW + "Click view Servers!")
                .toItemStack(), event -> {
            if (serverManager.getInstances(ServerType.HCF).size() == 1) {
                serverManager.connectToServer((Player) event.getWhoClicked(), serverManager.getInstances(ServerType.HCF).get(0).getServerName());
                return;
            }
            inventoryManager.getInventory(ServerType.HCF).open((Player) event.getWhoClicked());
        });

        // KitMap
        setItem(14, new ItemBuilder(Material.BOW)
                .setName(ChatColor.GOLD + "KitMap")
                .addLore(ChatColor.DARK_GRAY + "HardCore Factions")
                .addLore(ChatColor.GRAY + "")
                .addLore(ChatColor.WHITE + "KitMap is a based HCF prototype")
                .addLore(ChatColor.WHITE + "with free kits to train teamfights.")
                .addLore(ChatColor.WHITE + "Fight to get the #1 on the")
                .addLore(ChatColor.WHITE + "kills leaderboards to get pypal")
                .addLore(ChatColor.WHITE + "and buycraft rewards!")
                .addLore(ChatColor.GRAY + "")
                .addLore(ChatColor.WHITE + "Regions" + ChatColor.GRAY + ":" + ChatColor.YELLOW + " North America")
                .addLore(ChatColor.WHITE + "Total Players" + ChatColor.GRAY + ":" + ChatColor.YELLOW + serverManager.getCountForType(ServerType.HCF) + ChatColor.YELLOW + " / 1,700 ")
                .addLore(ChatColor.GRAY + "")
                .addLore(ChatColor.YELLOW + "Click view Servers!")
                .toItemStack(), event -> {
            if (serverManager.getInstances(ServerType.KITS).size() == 1) {
                serverManager.connectToServer((Player) event.getWhoClicked(), serverManager.getInstances(ServerType.KITS).get(0).getServerName());
                return;
            }
            inventoryManager.getInventory(ServerType.KITS).open((Player) event.getWhoClicked());
        });

        // Hosted UHC
        setItem(10, new ItemBuilder(Material.GOLDEN_APPLE)
                .setName(ChatColor.GOLD + "Hosted UHC")
                .addLore(ChatColor.DARK_GRAY + "UltraHardCore")
                .addLore(ChatColor.GRAY + "")
                .addLore(ChatColor.WHITE + "Gear up and fight to be the last")
                .addLore(ChatColor.WHITE + "man/team standing without natural")
                .addLore(ChatColor.WHITE + "regeneration, a closing border")
                .addLore(ChatColor.WHITE + "and different Scenarios")
                .addLore(ChatColor.GRAY + "")
                .addLore(ChatColor.GRAY + "⎮ " + ChatColor.YELLOW + "Classic UltraHardcore")
                .addLore(ChatColor.GRAY + "⎮ " + ChatColor.YELLOW + "Different Scenarios")
                .addLore(ChatColor.GRAY + "⎮ " + ChatColor.YELLOW + "FFA and Team Games")
                .addLore(ChatColor.GRAY + "")
                .addLore(ChatColor.GREEN + "Season II Payout")
                .addLore(ChatColor.YELLOW + "➸" + ChatColor.GREEN + "#1" + ChatColor.GRAY + " ● " + ChatColor.WHITE + "$100" + ChatColor.DARK_BLUE + "Pay" + ChatColor.BLUE + "Pal")
                .addLore(ChatColor.YELLOW + "➸" + ChatColor.WHITE + "#2" + ChatColor.GRAY + " ● " + ChatColor.WHITE + "$50" + ChatColor.DARK_BLUE + "Pay" + ChatColor.BLUE + "Pal")
                .addLore(ChatColor.YELLOW + "➸" + ChatColor.GOLD + "#3" + ChatColor.GRAY + " ● " + ChatColor.WHITE + "$25" + ChatColor.DARK_BLUE + "Pay" + ChatColor.BLUE + "Pal")
                .addLore(ChatColor.GRAY + "")
                .addLore(ChatColor.WHITE + "Regions" + ChatColor.GRAY + ":" + ChatColor.YELLOW + " North America" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "Europe")
                .addLore(ChatColor.WHITE + "Total Players" + ChatColor.GRAY + ":" + ChatColor.YELLOW + serverManager.getCountForType(ServerType.UHC) + ChatColor.YELLOW + " / 1,800 ")
                .addLore(ChatColor.GRAY + "")
                .addLore(ChatColor.YELLOW + "Click view Servers!")
                .toItemStack(), event -> inventoryManager.getInventory(ServerType.UHC).open((Player) event.getWhoClicked()));

        // ArenaPvP
        setItem(16, new ItemBuilder(Material.DIAMOND_SWORD)
                .setName("&6&lArenaPvP")
                .addLore(ChatColor.DARK_GRAY + "Competitive")
                .addLore(ChatColor.GRAY + "")
                .addLore(ChatColor.WHITE + "Looking for a place to warm up?")
                .addLore(ChatColor.WHITE + "Try out our ArenaPvP and gain skils")
                .addLore(ChatColor.WHITE + "with oúr ladders like FinalUHC,")
                .addLore(ChatColor.WHITE + "Boxing, HCF Trapping and more.")
                .addLore(ChatColor.GRAY + "")
                .addLore(ChatColor.GREEN + "Season II Payout")
                .addLore(ChatColor.YELLOW + "➸" + ChatColor.GREEN + "#1" + ChatColor.GRAY + " ● " + ChatColor.WHITE + "$100" + ChatColor.DARK_BLUE + "Pay" + ChatColor.BLUE + "Pal")
                .addLore(ChatColor.YELLOW + "➸" + ChatColor.WHITE + "#2" + ChatColor.GRAY + " ● " + ChatColor.WHITE + "$50" + ChatColor.DARK_BLUE + "Pay" + ChatColor.BLUE + "Pal")
                .addLore(ChatColor.YELLOW + "➸" + ChatColor.GOLD + "#3" + ChatColor.GRAY + " ● " + ChatColor.WHITE + "$25" + ChatColor.DARK_BLUE + "Pay" + ChatColor.BLUE + "Pal")
                .addLore(ChatColor.YELLOW + "➸" + ChatColor.RED + "#1 Each Ladder" + ChatColor.GRAY + " ● " + ChatColor.WHITE + "$30 Giftcard")
                .addLore(ChatColor.GRAY + "")
                .addLore(ChatColor.WHITE + "Regions" + ChatColor.GRAY + ":" + ChatColor.YELLOW + " North America" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "Europe")
                .addLore(ChatColor.WHITE + "Total Players" + ChatColor.GRAY + ":" + ChatColor.YELLOW + serverManager.getCountForType(ServerType.HCF) + ChatColor.YELLOW + " / 2,000 ")
                .addLore(ChatColor.GRAY + "")
                .addLore(ChatColor.YELLOW + "Click view Servers!")
                .toItemStack(), event -> {
            if (serverManager.getInstances(ServerType.ARENA).size() == 1) {
                serverManager.connectToServer((Player) event.getWhoClicked(), serverManager.getInstances(ServerType.ARENA).get(0).getServerName());
                return;
            }
            inventoryManager.getInventory(ServerType.ARENA).open((Player) event.getWhoClicked());
        });

        // Meetup
        setItem(31, new ItemBuilder(Material.APPLE)
                .setName(ChatColor.GOLD + "Meetup")
                .addLore(ChatColor.DARK_GRAY + "UHC & HCF Simulator")
                .addLore(ChatColor.GRAY + "")
                .addLore(ChatColor.WHITE + "A UHC and HCF Simulator.")
                .addLore(ChatColor.WHITE + "You are spawned with a random")
                .addLore(ChatColor.WHITE + "kit and only have to fight to")
                .addLore(ChatColor.WHITE + "be the last man standing!")
                .addLore(ChatColor.GRAY + "")
                .addLore(ChatColor.GRAY + "⎮ " + ChatColor.YELLOW + "UHC and HCF Combat Simulator")
                .addLore(ChatColor.GRAY + "⎮ " + ChatColor.YELLOW + "Fair kits and rerolls!")
                .addLore(ChatColor.GRAY + "⎮ " + ChatColor.YELLOW + "Scenarios and Abilites")
                .addLore(ChatColor.GRAY + "")
                .addLore(ChatColor.GREEN + "Season II Payout")
                .addLore(ChatColor.YELLOW + "➸" + ChatColor.GREEN + "#1" + ChatColor.GRAY + " ● " + ChatColor.WHITE + "$50" + ChatColor.DARK_BLUE + "Pay" + ChatColor.BLUE + "Pal")
                .addLore(ChatColor.YELLOW + "➸" + ChatColor.WHITE + "#2" + ChatColor.GRAY + " ● " + ChatColor.WHITE + "$25" + ChatColor.DARK_BLUE + "Pay" + ChatColor.BLUE + "Pal")
                .addLore(ChatColor.YELLOW + "➸" + ChatColor.GOLD + "#3" + ChatColor.GRAY + " ● " + ChatColor.WHITE + "$1ß" + ChatColor.DARK_BLUE + "Pay" + ChatColor.BLUE + "Pal")
                .addLore(ChatColor.GRAY + "")
                .addLore(ChatColor.WHITE + "Regions" + ChatColor.GRAY + ":" + ChatColor.YELLOW + " North America" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "Europe" )
                .addLore(ChatColor.WHITE + "Total Players" + ChatColor.GRAY + ":" + ChatColor.YELLOW + serverManager.getCountForType(ServerType.UHC) + ChatColor.YELLOW + " / 400 ")
                .addLore(ChatColor.GRAY + "")
                .addLore(ChatColor.YELLOW + "Click view Servers!")
                .toItemStack(), event -> inventoryManager.getInventory(ServerType.UHC).open((Player) event.getWhoClicked()));

        ItemStack[] contents = getInventory().getContents();
        for(int i = 0; i < contents.length; i++) {
            ItemStack stack = contents[i];
            if(stack != null && stack.getType() != Material.AIR) continue;
            contents[i] = new ItemBuilder(Material.STAINED_GLASS_PANE)
                    .setName("")
                    .setDurability((short)7)
                    .toItemStack();
        }
        getInventory().setContents(contents);
    }
}
