package org.contrum.lobby.listener;

import cc.stormworth.core.event.ServerCreateEvent;
import cc.stormworth.core.event.ServerDeleteEvent;
import cc.stormworth.core.server.ServerInstance;
import cc.stormworth.core.server.ServerType;
import org.contrum.lobby.server.ServerManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

@RequiredArgsConstructor
public class ServersListeners implements Listener {

    private final ServerManager serverManager;

    @EventHandler
    public void onServerCreate(ServerCreateEvent serverCreateEvent) {
        serverManager.createServer(serverCreateEvent.getServerName(), serverCreateEvent.getServerType(), serverCreateEvent.getServerAddress(), serverCreateEvent.getServerPort());
    }

    @EventHandler
    public void onServerDelete(ServerDeleteEvent serverDeleteEvent) {
        for (ServerType serverInstanceType : ServerType.values()) {
            List<ServerInstance> instances = serverManager.getInstances(serverInstanceType);

            for (ServerInstance serverInstance : instances) {
                if (serverInstance.getServerName().equalsIgnoreCase(serverDeleteEvent.getServerName()))
                    serverManager.deleteServer(serverInstance);
            }
        }
    }
}