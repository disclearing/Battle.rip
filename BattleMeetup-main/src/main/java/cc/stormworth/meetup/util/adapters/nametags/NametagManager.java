package cc.stormworth.meetup.util.adapters.nametags;

import cc.stormworth.meetup.nms.NMSHelper;
import cc.stormworth.meetup.util.PlayerUtil;
import cc.stormworth.meetup.util.TaskUtil;
import cc.stormworth.meetup.util.adapters.NametagAdapter;
import lombok.Getter;
import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NametagManager {

    static int COUNT = 1;
    private static List<NametagInfo> registeredTeams;
    private final ExecutorService executor;
    @Getter
    private final NametagAdapter adapter;

    public NametagManager(NametagAdapter adapter) {
        this.adapter = adapter;
        this.executor = Executors.newSingleThreadExecutor(TaskUtil.newThreadFactory("Nametag Update Thread"));

        registeredTeams = this.adapter.getNametags();
    }

    public static void updateNametag(Player refreshFor, Player toRefresh, String name) {
        PacketPlayOutScoreboardTeam team = new PacketPlayOutScoreboardTeam();

        String teamName = "§8§" + getId(name) + name;

        if (teamName.length() > 16) {
            teamName = teamName.substring(0, 16);
        }

        NMSHelper.setValueStatic(team, "a", teamName);
        NMSHelper.setValueStatic(team, "f", 3);
        Collection<String> e = new ArrayList<>((Collection<String>) NMSHelper.getValueStatic(team, "e"));
        e.add(toRefresh.getDisguisedName());
        NMSHelper.setValueStatic(team, "e", e);
        PlayerUtil.getNMSPlayer(refreshFor).playerConnection.sendPacket(team);
    }

    public static void clearNametag(Player refreshFor, Player toRefresh, String name) {
        PacketPlayOutScoreboardTeam team = new PacketPlayOutScoreboardTeam();

        String teamName = "§8§" + getId(name) + name;

        if (teamName.length() > 16) {
            teamName = teamName.substring(0, 16);
        }

        NMSHelper.setValueStatic(team, "a", teamName);
        NMSHelper.setValueStatic(team, "f", 3);
        Collection<String> e = new ArrayList<>((Collection<String>) NMSHelper.getValueStatic(team, "e"));
        e.add("");
        NMSHelper.setValueStatic(team, "e", e);
        PlayerUtil.getNMSPlayer(refreshFor).playerConnection.sendPacket(team);
    }

    private static int getId(String name) {
        int count = 1;

        for (NametagInfo nametag : registeredTeams) {
            if (nametag.getName().equalsIgnoreCase(name)) {
                count = nametag.getCount();
            }
        }

        return count;
    }

    public void join(Player player) {
        registeredTeams.forEach(info -> PlayerUtil.getNMSPlayer(player).playerConnection.sendPacket(info.getPacket()));

        this.reloadPlayer(player, true);
        this.reloadOthersFor(player, true);
    }

    public void reloadPlayer(Player toRefresh, boolean async) {
        apply(new NametagUpdate(toRefresh.getUniqueId(), null), async);
    }

    public void reloadOthersFor(Player refreshFor, boolean async) {
        Bukkit.getOnlinePlayers().stream()
                .filter(toRefresh -> !refreshFor.getUniqueId().equals(toRefresh.getUniqueId()))
                .forEach(toRefresh -> this.reloadPlayer(toRefresh, refreshFor, async));
    }

    public void reloadPlayer(Player toRefresh, Player refreshFor, boolean async) {
        apply(new NametagUpdate(toRefresh.getUniqueId(), refreshFor.getUniqueId()), async);
    }

    private void apply(NametagUpdate update, boolean async) {
        if (async) {
            this.executor.execute(() -> this.applyUpdate(update));
        } else {
            this.applyUpdate(update);
        }
    }

    private void applyUpdate(NametagUpdate update) {
        Player toRefresh = Bukkit.getPlayer(update.getToRefresh());

        if (toRefresh == null) {
            return;
        }

        if (update.getRefreshFor() != null) {
            Player refreshFor = Bukkit.getPlayer(update.getRefreshFor());

            if (refreshFor != null) {
                this.adapter.updateNametags(toRefresh, refreshFor);
            }
        } else {
            Bukkit.getOnlinePlayers().forEach(refreshFor -> this.adapter.updateNametags(toRefresh, refreshFor));
        }
    }
}
