package cc.stormworth.meetup.profile.struct;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.trail.ParticleEffect;
import cc.stormworth.meetup.profile.Profile;
import cc.stormworth.meetup.utils.countdown.Countdown;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Setter
@Getter
public class Teleport {

    private final Player player;
    private final Location startLocation;
    private final Location to;
    private final int countdown;
    private int maxMoveDistance = 3;
    private boolean cancelOnMove = true;
    private Consumer<Player> onTeleport = null;
    private Player target;
    private boolean cancelledOnDamage = true;
    private boolean showParticles = true;

    public Countdown start() {
        return Countdown.of(countdown, TimeUnit.SECONDS)
                .players(Lists.newArrayList(player))
                .withMessage(
                        "&6&l[&e✷&6&l] &eTeleporting in &6&l{time}&e." + (cancelOnMove
                                ? "" : "&c&l Do not move or teleport will be cancelled!"))
                .onTick(() -> {
                    if (cancelOnMove
                            && player.getLocation().distanceSquared(startLocation) > maxMoveDistance) {
                        player.sendMessage(CC.translate("&cTeleport cancelled. You moved too far."));
                        Profile profile = Profile.get(player);

                        profile.getCountdown().cancel();
                    }

                    if (showParticles) {
                        ParticleEffect.PORTAL.sphere(player, player.getLocation().clone().add(0, 1, 0), 2);
                    }
                })
                .onBroadcast(() -> player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 1))
                .onFinish(() -> {

                    if (target != null) {
                        player.teleport(target.getLocation());
                    } else {
                        player.teleport(to);
                    }

                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
                    player.sendMessage(CC.translate("&eYou have been teleported!"));

                    Profile profile = Profile.get(player);
                    profile.setCountdown(null);
                    profile.setTeleport(null);

                    if (onTeleport != null) {
                        onTeleport.accept(player);
                    }

                })
                .start();
    }

}