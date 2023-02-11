package org.contrum.lobby.adapters.tab;

import cc.stormworth.core.profile.Profile;
import cc.stormworth.core.server.ServerType;
import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.tablist.adapter.TabListAdapter;
import cc.stormworth.core.util.tablist.utils.BufferedTabObject;
import cc.stormworth.core.util.tablist.utils.TabColumn;
import org.contrum.lobby.LobbyPlugin;
import org.contrum.lobby.server.ServerManager;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * @author UKry
 * Created: 18/11/2022
 * Project BattleHub
 **/

@SuppressWarnings("UnusedLabel")
@AllArgsConstructor
public class TablistAdapter implements TabListAdapter {

    private LobbyPlugin plugin;

    @Override
    public Set<BufferedTabObject> getSlots(Player player) {
        Set<BufferedTabObject> objects = new HashSet<>();
        Profile profile = plugin.getBattleAPI().getProfileManager().getProfile(player);
        ServerManager manager = plugin.getServerManager();
        objects.add(new BufferedTabObject().slot(4).column(TabColumn.LEFT).text(CC.translate("&6&lCoins &7(&e") + profile.getCurrencyData().getCoins() + "&7)"));


        left : {
            //Kits
            objects.add(new BufferedTabObject().slot(6).column(TabColumn.LEFT).text(CC.translate("&6&lKits&7(" + manager.getCountForType(ServerType.KITS))));
            objects.add(new BufferedTabObject().slot(7).column(TabColumn.LEFT).text(CC.translate("&fKills&7: &e" + 0)));
            objects.add(new BufferedTabObject().slot(8).column(TabColumn.LEFT).text(CC.translate("&fDeaths&7: &e" + 0)));
            //UHC
            objects.add(new BufferedTabObject().slot(10).column(TabColumn.LEFT).text(CC.translate("&6&lHosted UHC")));
            objects.add(new BufferedTabObject().slot(11).column(TabColumn.LEFT).text(CC.translate("&fOnline&7: &e" + manager.getCountForType(ServerType.UHC))));
            //Meetup
            objects.add(new BufferedTabObject().slot(13).column(TabColumn.LEFT).text(CC.translate("&6&lMeetup")));
            objects.add(new BufferedTabObject().slot(14).column(TabColumn.LEFT).text(CC.translate("&fOnline&7: &e" + manager.getCountForType(ServerType.MEETUP))));

            objects.add(new BufferedTabObject().slot(17).column(TabColumn.LEFT).text(CC.translate("&6&lDiscord")));
            objects.add(new BufferedTabObject().slot(18).column(TabColumn.LEFT).text("dsc.gg/battle"));
        }

        // Middle Bar
        middle : {
            objects.add(new BufferedTabObject().slot(1).column(TabColumn.MIDDLE).text(CC.translate("&6&lBattle Network")));
            objects.add(new BufferedTabObject().slot(2).column(TabColumn.MIDDLE).text(CC.translate("&fOnline&7: &e" + manager.getTotalCount() + " / 5,000")));
            objects.add(new BufferedTabObject().slot(4).column(TabColumn.MIDDLE).text(CC.translate("&6&lProfile")));
            objects.add(new BufferedTabObject().slot(5).column(TabColumn.MIDDLE).text(CC.translate("&7[&f" + profile.getHighestAssigned().getRankName() + "&7] &f" + player.getName())));
            objects.add(new BufferedTabObject().slot(17).column(TabColumn.MIDDLE).text(CC.translate("&6&lWebsite")));
            objects.add(new BufferedTabObject().slot(18).column(TabColumn.MIDDLE).text("www.battle.rip"));
        }

        //Right Bar
        right : {
            objects.add(new BufferedTabObject().slot(4).column(TabColumn.RIGHT).text(CC.translate("&6&lGOLD &7(&e" + profile.getCurrencyData().getGold() + "&7)")));
            objects.add(new BufferedTabObject().slot(6).column(TabColumn.RIGHT).text(CC.translate("&6&lHCF&7(&e" + manager.getCountForType(ServerType.HCF) + "&7)")));
            objects.add(new BufferedTabObject().slot(7).column(TabColumn.RIGHT).text(CC.translate("&fLives&7: &e" + 0 )));
            objects.add(new BufferedTabObject().slot(8).column(TabColumn.RIGHT).text(CC.translate("&fDeathban&7: &e" + "None")));
            objects.add(new BufferedTabObject().slot(10).column(TabColumn.RIGHT).text(CC.translate("&6&lArenaPvP")));
            objects.add(new BufferedTabObject().slot(11).column(TabColumn.RIGHT).text(CC.translate("&fOnline&7: &e" + manager.getCountForType(ServerType.ARENA) + " / 1200")));
            objects.add(new BufferedTabObject().slot(13).column(TabColumn.RIGHT).text(CC.translate("&6&lComing Soon")));
            objects.add(new BufferedTabObject().slot(14).column(TabColumn.RIGHT).text(CC.translate("&fOnline: &e" + manager.getCountForType(ServerType.UNKNOWN) + " / 800")));
            objects.add(new BufferedTabObject().slot(17).column(TabColumn.RIGHT).text(CC.translate("&6&lTwitter")));
            objects.add(new BufferedTabObject().slot(18).column(TabColumn.RIGHT).text("@BattleRipNet"));
        }
        return objects;
    }

    @Override
    public String getHeader() {
        return CC.translate("&6&lBattle Network &7| &f" + plugin.getServerManager().getTotalCount() + " / 1000");
    }

    @Override
    public String getFooter() {
        return CC.translate("&fYou are playing &e" + plugin.getServerId() + " &fon &ebattle.rip");
    }
}