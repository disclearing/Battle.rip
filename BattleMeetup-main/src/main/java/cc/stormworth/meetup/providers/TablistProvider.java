package cc.stormworth.meetup.providers;

import cc.stormworth.core.CorePlugin;
import cc.stormworth.core.kt.tab.LayoutProvider;
import cc.stormworth.core.kt.tab.TabLayout;
import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.states.GameState;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.util.EloCalculator;
import cc.stormworth.meetup.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public class TablistProvider implements LayoutProvider {

    @NotNull
    @Override
    public TabLayout provide(Player player) {
        UserData user = UserManager.getInstance().getUser(player.getUniqueId());

        TabLayout layout = TabLayout.create(player);
        layout.setHeaderText(CC.B_PRIMARY + "Battle Network &7┃ &f" + CorePlugin.getInstance().getCache().getOnlineCount() + " / 1,000");
        layout.setFooterText("&fYou are playing §6" + CorePlugin.getInstance().getServerId() + " &fon " + "§e" + "battle.rip");

        if (GameManager.getInstance().isIngame()) {
            layout.set(1, 1, CC.B_PRIMARY + (GameManager.getInstance().getStyle() == null ? "Meetup" : GameManager.getInstance().getStyle().getName() + " Meetup"), -1);
            layout.set(2, 19, CC.GRAY + "ts.battle.rip", -1);
            layout.set(0, 19, CC.GRAY + "store.battle.rip", -1);
            layout.set(1, 19, CC.PRIMARY + "battle.rip", -1);

            layout.set(0, 3, CC.B_PRIMARY + "Players", -1);
            layout.set(0, 4, "Alive: " + CC.SECONDARY + GameManager.getInstance().getAlivePlayers().size(), -1);

            layout.set(1, 3, CC.B_PRIMARY + "Teams", -1);
            layout.set(1, 4, "Mode: " + CC.SECONDARY + GameManager.getInstance().getMode().toString(), -1);

            layout.set(2, 3, CC.B_PRIMARY + "Server", -1);

            layout.set(2, 4, CorePlugin.getInstance().getServerId(), -1);

            displayUsers(layout, player);
        } else {
            layout.set(1, 1, CC.B_PRIMARY + (GameManager.getInstance().getStyle() == null ? "Meetup" : GameManager.getInstance().getStyle().getName() + " Meetup"), -1);
            layout.set(2, 19, CC.GRAY + "ts.battle.rip", -1);
            layout.set(0, 19, CC.GRAY + "store.battle.rip", -1);
            layout.set(1, 19, CC.PRIMARY + "battle.rip", -1);

            layout.set(0, 3, CC.B_PRIMARY + "Players", -1);
            layout.set(0, 4, "Online: " + CC.SECONDARY + Bukkit.getOnlinePlayers().size(), -1);

            layout.set(1, 3, CC.B_PRIMARY + "Teams", -1);
            layout.set(1, 4, "Mode: " + CC.SECONDARY + GameManager.getInstance().getMode().toString(), -1);

            if (GameManager.getInstance().getGameState() == GameState.SCATTER) {
                layout.set(2, 3, CC.B_PRIMARY + "Server", -1);
                layout.set(2, 4, CorePlugin.getInstance().getServerId(), -1);
            } else {
                layout.set(2, 3, CC.B_PRIMARY + "Waiting", -1);
                int requiredPlayers = GameManager.getInstance().getRequiredPlayers() - Bukkit.getOnlinePlayers().size();
                layout.set(2, 4, "Required: " + CC.SECONDARY + (Math.max(requiredPlayers, 0)), -1);
            }

            displayUsers(layout, player);
        }
        return layout;
    }

    private void displayUsers(TabLayout layout, Player viewer) {
        layout.set(0, 7, CC.B_GOLD + "Username ");
        layout.set(1, 7, CC.B_GOLD + "Rating ");
        layout.set(2, 7, CC.B_GOLD + "Division");

        AtomicInteger index = new AtomicInteger(8);
        boolean game = GameManager.getInstance().isIngame();

        Bukkit.getOnlinePlayers().stream().limit(10)
                .forEach(player -> {
                    if (index.get() > 18) return;
                    if (game && !UserManager.getInstance().getUser(player.getUniqueId()).isAlive()) return;

                    double targetAbsorption = PlayerUtil.getNMSPlayer(player).getAbsorptionHearts();
                    String targetHealth = " " + CC.GRAY + (int) Math.ceil(player.getHealth() + targetAbsorption) + CC.DARK_RED + '\u2764';

                    // This is the username tab
                    layout.set(0, index.get(), (viewer.getUniqueId().equals(player.getUniqueId()) ? CC.GREEN : CC.RED) + player.getDisguisedName() + targetHealth, PlayerUtil.getNMSPlayer(player).ping);

                    int elo = UserManager.getInstance().getUser(player.getUniqueId()).getStatistics().getElo();
                    EloCalculator.Division division = GameManager.getInstance().getEloCalculator().getDivision(elo);

                    // Here you display the elo
                    layout.set(1, index.get(), CC.YELLOW + elo + "", -1);

                    // Here you display the division
                    layout.set(2, index.getAndIncrement(), division.getColor() + division.getName(), -1);
                });
    }
}
