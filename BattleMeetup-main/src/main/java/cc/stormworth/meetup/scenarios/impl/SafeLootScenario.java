package cc.stormworth.meetup.scenarios.impl;

import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.managers.TeamManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.scenarios.Scenario;
import cc.stormworth.meetup.states.Mode;
import cc.stormworth.meetup.team.Team;
import cc.stormworth.meetup.util.Colors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SafeLootScenario extends Scenario implements Listener {
    public SafeLootScenario() {
        super("SafeLoot", new ItemStack(Material.CHEST), false, "Upon death, a chest will spawn", "containing all the deceased player's loot.", "This chest is locked to the player/team", "who killed the respective player.");
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (notPlaying(event.getPlayer()))
            return;
        Block block = event.getBlock();
        if (block.getType() == Material.CHEST) {
            Chest chest = (Chest) block.getState();
            if (chest.hasMetadata("SafeLoot") && !chest.getMetadata("SafeLoot").get(0).asString().equals(event.getPlayer().getName())) {
                String owner = chest.getMetadata("SafeLoot").get(0).asString();
                if (GameManager.getInstance().getMode() == Mode.TO2) {
                    Team ownerTeam = TeamManager.getInstance().getTeam(UserManager.getInstance().getUser(Bukkit.getOfflinePlayer(owner).getUniqueId()).getTeamNumber()) != null ? TeamManager.getInstance().getTeam(UserManager.getInstance().getUser(Bukkit.getOfflinePlayer(owner).getUniqueId()).getTeamNumber()) : TeamManager.getInstance().getTeam(UserManager.getInstance().getUser(Bukkit.getPlayer(owner).getUniqueId()).getTeamNumber());
                    Team team = TeamManager.getInstance().getTeam(UserManager.getInstance().getUser(event.getPlayer().getUniqueId()).getTeamNumber());
                    if (ownerTeam.equals(team))
                        return;
                }
                event.setCancelled(true);
                event.getPlayer().sendMessage(Colors.RED + "This chest is protected.");
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (notPlaying(event.getPlayer()) || !event.hasBlock())
            return;
        Block block = event.getClickedBlock();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && block.getType() == Material.CHEST) {
            Chest chest = (Chest) block.getState();
            if (chest.hasMetadata("SafeLoot") && !chest.getMetadata("SafeLoot").get(0).asString().equals(event.getPlayer().getName())) {
                String owner = chest.getMetadata("SafeLoot").get(0).asString();
                if (GameManager.getInstance().getMode() == Mode.TO2) {
                    Team ownerTeam = TeamManager.getInstance().getTeam(UserManager.getInstance().getUser(Bukkit.getOfflinePlayer(owner).getUniqueId()).getTeamNumber()) != null ? TeamManager.getInstance().getTeam(UserManager.getInstance().getUser(Bukkit.getOfflinePlayer(owner).getUniqueId()).getTeamNumber()) : TeamManager.getInstance().getTeam(UserManager.getInstance().getUser(Bukkit.getPlayer(owner).getUniqueId()).getTeamNumber());
                    Team team = TeamManager.getInstance().getTeam(UserManager.getInstance().getUser(event.getPlayer().getUniqueId()).getTeamNumber());
                    if (ownerTeam.equals(team))
                        return;
                }
                event.setCancelled(true);
                event.getPlayer().sendMessage(Colors.RED + "This chest is protected.");
            }
        }
    }
}
