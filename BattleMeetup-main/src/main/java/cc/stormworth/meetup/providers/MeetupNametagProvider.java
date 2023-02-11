package cc.stormworth.meetup.providers;

import cc.stormworth.core.kt.nametag.NametagInfo;
import cc.stormworth.core.kt.nametag.NametagProvider;
import cc.stormworth.core.profile.Profile;
import cc.stormworth.core.rank.Rank;
import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.managers.TeamManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.scenarios.Scenario;
import cc.stormworth.meetup.scenarios.impl.NoCleanPlusScenario;
import cc.stormworth.meetup.states.PlayerState;
import cc.stormworth.meetup.team.Team;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.util.EloCalculator;
import com.google.common.collect.Lists;
import com.lunarclient.bukkitapi.LunarClientAPI;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MeetupNametagProvider extends NametagProvider {

    public MeetupNametagProvider() {
        super("Meetup", 1);
    }

    private static NametagInfo createNametag(Player toRefresh, Player refreshFor, String prefix, String suffix) {
        return createNametag(prefix, suffix);
    }

    public static NametagInfo getNameTag(Player toRefresh, Player refreshFor) {
        NametagInfo nametagInfo = null;

        Profile profile = Profile.getByUuidIfAvailable(toRefresh.getUniqueId());

        String color = (profile == null || toRefresh.isDisguised() ? Rank.DEFAULT : profile.getRank()).getColor();

        if (GameManager.getInstance().isIngame()) {
            UserData user = UserManager.getInstance().getUser(toRefresh.getUniqueId());
            Team team = TeamManager.getInstance().getTeam(user.getTeamNumber());

            if (user.getPlayerState() == PlayerState.SPECTATOR) {
                color = "&7";
            } else if (user.isInvincible()) {
                color = "&6";
            } else if (Scenario.getByName("NoClean+").isActive() && NoCleanPlusScenario.isDisturbActive(toRefresh.getUniqueId())) {

                if (NoCleanPlusScenario.getUSERS().get(toRefresh.getUniqueId()).equals(refreshFor.getName())) {
                    color = "&4";
                }
            } else if (toRefresh == refreshFor || (team != null && team.getMembers().contains(refreshFor.getUniqueId()))) {
                color = "&a";
            } else {
                color = "&c";
            }
        }

        color = CC.translate(color);

        if (refreshFor == toRefresh) {
            nametagInfo = createNametag(toRefresh, refreshFor, color, "");
        }

        return (nametagInfo == null ? createNametag(toRefresh, refreshFor, color, "") : nametagInfo);
    }

    public static void updateLC(Player toRefresh, Player refreshFor) {

        if (toRefresh.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            LunarClientAPI.getInstance().overrideNametag(toRefresh, Lists.newArrayList(), refreshFor);
        } else {
            List<String> nametag;
            String prefix = CC.translate(getNameTag(toRefresh, refreshFor).getPrefix());
            if (!GameManager.getInstance().isIngame()) {

                UserData userData = UserManager.getInstance().getUser(toRefresh.getUniqueId());

                int elo = userData == null ? 0 : userData.getStatistics().getElo();
                EloCalculator.Division division = GameManager.getInstance().getEloCalculator().getDivision(elo);

                Profile profile = Profile.getByUuidIfAvailable(toRefresh.getUniqueId());
                Rank rank = profile == null || toRefresh.isDisguised() ? Rank.DEFAULT : profile.getRank();

                nametag = Arrays.asList(
                        CC.translate("&7[" + division.getColor() + division.getName() + "&7]"),
                        CC.translate("&7[" + rank.getDisplayName() + "&7] " + rank.getColor() + toRefresh.getDisguisedName()));
                LunarClientAPI.getInstance().overrideNametag(toRefresh, nametag, refreshFor);
            } else {
                nametag = Collections.singletonList(prefix + toRefresh.getDisguisedName());
                LunarClientAPI.getInstance().overrideNametag(toRefresh, nametag, refreshFor);
            }
        }
    }

    @Override
    public NametagInfo fetchNametag(Player toRefresh, Player refreshFor) {
        updateLC(toRefresh, refreshFor);
        return getNameTag(toRefresh, refreshFor);
    }
}
