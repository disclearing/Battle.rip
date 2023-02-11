package cc.stormworth.meetup.ability.impl.tpclutch;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.time.TimeUtil;
import cc.stormworth.meetup.ability.InteractAbility;
import cc.stormworth.meetup.managers.TeamManager;
import cc.stormworth.meetup.team.Team;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class TpClutch extends InteractAbility {

    public TpClutch() {
        super("TpClutch",
                "&bTp Clutch",
                Lists.newArrayList(
                        "",
                        "&7A fiend in a hurry? teleport to",
                        "&7 him using this ability, but be careful, you will be blinded once teleported",
                        ""
                ),
                new ItemStack(Material.EMPTY_MAP),
                TimeUtil.parseTimeLong("8m"));
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Team team = TeamManager.getInstance().getTeam(player);

        if (team == null) {
            player.sendMessage(CC.translate("&cTo use this ability you must be in a faction."));
            event.setCancelled(true);
            player.updateInventory();
            return;
        }

        if (team.getOnlineMembers().size() < 2) {
            player.sendMessage(
                    CC.translate(
                            "&cTo use this ability you must have at least 2 online members in your faction."));
            event.setCancelled(true);
            player.updateInventory();
            return;
        }

        player.updateInventory();
        event.setCancelled(true);
        new TpClutchMenu(team).openMenu(player);
    }

    @Override
    public List<PotionEffect> getPotionEffects() {
        return null;
    }
}