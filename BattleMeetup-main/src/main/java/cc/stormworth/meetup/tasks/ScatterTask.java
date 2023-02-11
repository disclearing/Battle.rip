package cc.stormworth.meetup.tasks;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.onedoteight.TitleBuilder;
import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.states.PlayerState;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.util.Colors;
import cc.stormworth.meetup.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class ScatterTask extends BukkitRunnable {

    private final List<UUID> playersToScatter = new ArrayList<>();
    private int playersScattered = 0;
    private final int scatterFrequency;

    public ScatterTask() {
        Bukkit.getOnlinePlayers().forEach(p -> playersToScatter.add(p.getUniqueId()));
        this.scatterFrequency = playersToScatter.size() > 15 ? 10 : 20;

        new StartingTask();

        runTaskTimer(Meetup.getInstance(), 0L, this.scatterFrequency);
    }

    @Override
    public void run() {

        if (this.playersScattered == this.playersToScatter.size()) {
            Bukkit.getLogger().log(Level.INFO, "[Meetup] Successfully finished the scatter! Players scattered: " + playersScattered);
            this.cancel();


            TitleBuilder titleBuilder = new TitleBuilder(CC.translate("&6&lMeetup"), CC.translate("&eEveryone has been scattered!"), 10, 10, 10);

            Bukkit.getOnlinePlayers().forEach(online -> {
                titleBuilder.send(online);

                online.playSound(online.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
            });

            return;
        }

        Player p = Bukkit.getPlayer(this.playersToScatter.get(this.playersScattered));

        if (p == null) {
            this.playersScattered++;
            return;
        }

        UserData user = UserManager.getInstance().getUser(p.getUniqueId());
        user.setPlayerState(PlayerState.SCATTERED);
        GameManager.getInstance().getAlivePlayers().add(p.getUniqueId());
        GameManager.getInstance().setTotalPlayers(GameManager.getInstance().getTotalPlayers() + 1);

        PlayerUtil.scatterPlayer(p);
        p.sendMessage(Colors.SECONDARY + "You have been scattered.");

        this.playersScattered++;
    }
}
