package org.contrum.lobby;

import cc.stormworth.core.api.BattleAPI;
import cc.stormworth.core.server.ServerType;
import cc.stormworth.core.util.scoreboard.ScoreboardManager;
import cc.stormworth.core.util.tablist.TabListManager;
import org.contrum.lobby.inventory.InventoryManager;
import org.contrum.lobby.listener.*;
import org.contrum.lobby.managelobby.LobbyManager;
import org.contrum.lobby.managelobby.command.ManageLobbyCommand;
import org.contrum.lobby.managelobby.command.sub.LobbyHoloSubCommand;
import org.contrum.lobby.managelobby.command.sub.LobbyNPCSubCommand;
import org.contrum.lobby.adapters.board.LobbyScoreboard;
import org.contrum.lobby.server.ServerManager;
import org.contrum.lobby.adapters.tab.TablistAdapter;
import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import fr.mrmicky.fastinv.FastInvManager;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class LobbyPlugin extends JavaPlugin {

    BattleAPI battleAPI;
    private ServerManager serverManager;
    private LobbyManager lobbyManager;
    private InventoryManager inventoryManager;
    @Setter private String serverId = "Unknwon";

    @Override
    public void onEnable() {

        saveDefaultConfig();
        battleAPI = BattleAPI.getBattleAPI(this);

        serverManager = new ServerManager(this);

        FastInvManager.register(this);
        inventoryManager = new InventoryManager(this, serverManager);


        register(new ServersListeners(serverManager));
        register(new EntityListeners());
        register(new BlockListeners());
        register(new InventoryListeners());
        register(new PlayerListeners(this, inventoryManager));

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, serverManager::updateInstances, 20L, 20L);

        for (Document document : battleAPI.getBattleCollection("server_data").find()) {
            serverManager.createServer(
                    document.getString("server_name"),
                    ServerType.valueOf(document.getString("server_type")),
                    document.getString("server_address"),
                    document.getInteger("server_port")
            );
        }
        lobbyManager = new LobbyManager(this);
        lobbyManager.loadAll();
        CommandService drink = Drink.get(this);
        drink.register(new ManageLobbyCommand(), "managelobby")
                .registerSub(new LobbyNPCSubCommand(this))
                .registerSub(new LobbyHoloSubCommand(this));
        drink.registerCommands();
        new TabListManager(this, new TablistAdapter(this), battleAPI);
        new ScoreboardManager(this, new LobbyScoreboard(this));
    }


    @Override
    public void onDisable() {
        lobbyManager.saveAll();
    }

    public void log(String message) {
        System.out.println("[" + getDescription().getFullName() + "] " + message);
    }

    private void register(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

}