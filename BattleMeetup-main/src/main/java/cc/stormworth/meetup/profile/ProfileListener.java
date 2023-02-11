package cc.stormworth.meetup.profile;

import cc.stormworth.meetup.managers.TeamManager;
import cc.stormworth.meetup.team.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class ProfileListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = new Profile(player.getUniqueId());

        Profile.getProfiles().put(player.getUniqueId(), profile);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        Player damager = (Player) event.getDamager();
        Player damaged = (Player) event.getEntity();

        Profile profile = Profile.get(damaged);
        Profile profileDamager = Profile.get(damager);

        profile.setLastDamager(damager);
        profileDamager.setLastDamaged(damaged);

        profileDamager.setLastDamagedTime(System.currentTimeMillis());
        profile.setLastDamagerTime(System.currentTimeMillis());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.get(event.getPlayer());

        if (profile.isTeamChat()) {
            Team team = TeamManager.getInstance().getTeam(player);

            if (team != null) {
                event.setCancelled(true);
                team.chat(player, event.getMessage());
            } else {
                profile.setTeamChat(false);
            }
        }

    }

}