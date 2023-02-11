package org.contrum.lobby.server;

import cc.stormworth.core.server.ServerInstance;
import cc.stormworth.core.server.ServerType;
import org.contrum.lobby.LobbyPlugin;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("UnstableApiUsage")
public class ServerManager {

    private final LobbyPlugin lobbyPlugin;
    private final Map<ServerType, List<ServerInstance>> gameInstances;
    private final Map<ServerType, Integer> serverInstanceTypeTotal;

    public ServerManager(LobbyPlugin lobbyPlugin) {
        this.lobbyPlugin = lobbyPlugin;
        this.gameInstances = new ConcurrentHashMap<>();
        this.serverInstanceTypeTotal = new ConcurrentHashMap<>();

        for (ServerType serverInstanceType : ServerType.values()) {
            gameInstances.put(serverInstanceType, new CopyOnWriteArrayList<>());
            serverInstanceTypeTotal.put(serverInstanceType, 0);
        }

        Bukkit.getMessenger().registerOutgoingPluginChannel(lobbyPlugin, "BungeeCord");

    }

    public int getTotalCount() {
        int total = 0;
        for (ServerType serverGameType : ServerType.values()) {
            total += serverInstanceTypeTotal.get(serverGameType);
        }

        return total;
    }

    public void createServer(String serverName, ServerType serverInstanceType, String hostAddress, int hostPort) {
        ServerInstance serverInstance = new ServerInstance(serverName, serverInstanceType, hostAddress, hostPort);
        if (hostPort == Bukkit.getPort()) {
            lobbyPlugin.setServerId(serverName.toUpperCase().replaceAll("-LOBBY-", ""));
        }
        gameInstances.get(serverInstanceType).add(serverInstance);
    }

    public void deleteServer(ServerInstance serverInstance) {
        gameInstances.get(serverInstance.getServerInstanceType()).remove(serverInstance);
        lobbyPlugin.log("Server '" + serverInstance.getServerName() + "' with type '" + serverInstance.getServerInstanceType().name() + "' has been removed from the instances.");
    }

    public List<ServerInstance> getInstances(ServerType serverInstanceType) {
        return sortServers(serverInstanceType);
    }

    private List<ServerInstance> sortServers(ServerType serverInstanceType) {
        List<ServerInstance> games = gameInstances.get(serverInstanceType);

        HashMap<String, ServerInstance> serverInstanceHashMap = new HashMap<>();

        int total = 0;

        ArrayList<ServerInstance> temp = new ArrayList<>();

        final Map<String, Integer> map = new HashMap<>();
        final ValueComparator bvc = new ValueComparator(map);
        final TreeMap<String, Integer> sorted_map = new TreeMap<>(bvc);

        for (ServerInstance instance : games) {
            if (serverInstanceType.isOrder()) {
                if (instance.getServerId() >= 1) {
                    map.put(instance.getServerName(), instance.getServerId());
                    serverInstanceHashMap.put(instance.getServerName(), instance);
                }
            } else {
                map.put(instance.getServerName(), 1);
                serverInstanceHashMap.put(instance.getServerName(), instance);
            }

        }

        sorted_map.putAll(map);

        for (String s : sorted_map.keySet()) {
            ServerInstance instance = serverInstanceHashMap.get(s);
            if (instance != null) {
                temp.add(instance);
                total += instance.getOnlinePlayers();
            }
        }
        serverInstanceTypeTotal.put(serverInstanceType, total);
        return temp;
    }

    public void updateInstances() {
        for (Map.Entry<ServerType, List<ServerInstance>> serverInstanceTypeListEntry : gameInstances.entrySet()) {
            for (ServerInstance serverInstance : serverInstanceTypeListEntry.getValue()) {
                serverInstance.updateInstance();
            }
        }
    }

    public int getCountForType(ServerType serverInstanceType) {
        return serverInstanceTypeTotal.get(serverInstanceType);
    }

    @SneakyThrows
    public void connectToServer(Player player, String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);
        player.sendPluginMessage(lobbyPlugin, "BungeeCord", out.toByteArray());
    }

    private static class ValueComparator implements Comparator<String> {

        private final Map<String, Integer> base;

        public ValueComparator(Map<String, Integer> base) {
            this.base = base;
        }

        public int compare(String a, String b) {
            if (base.get(a) <= base.get(b)) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
