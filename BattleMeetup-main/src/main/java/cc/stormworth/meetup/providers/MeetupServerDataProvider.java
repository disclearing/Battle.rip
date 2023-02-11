package cc.stormworth.meetup.providers;

import cc.stormworth.core.server.ServerState;
import cc.stormworth.core.server.impl.ServerDataProvider;
import cc.stormworth.core.util.gson.json.JsonChain;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.util.TimeUtil;
import org.bukkit.Bukkit;

public class MeetupServerDataProvider implements ServerDataProvider {
    private final int requiredPlayers = GameManager.getInstance().getRequiredPlayers() - Bukkit.getOnlinePlayers().size();

    @Override
    public ServerState getState() {
        return ServerState.ACTIVE;
    }

    @Override
    public int getPlayerCount() {
        return Bukkit.getOnlinePlayers().size();
    }

    @Override
    public String getExtraData() {
        return new JsonChain()
                .addProperty("state", GameManager.getInstance().getGameState().name())
                .addProperty("mode", GameManager.getInstance().getMode().toString())
                .addProperty("scatteringIn", GameManager.getInstance().getScatteringIn())
                .addProperty("startingIn", GameManager.getInstance().getStartingIn())
                .addProperty("required", requiredPlayers)
                .addProperty("gameTime", TimeUtil.format(GameManager.getInstance().getFinalGameTime()))
                .addProperty("alive", GameManager.getInstance().getAlivePlayers().size())
                .get()
                .toString();
    }
}
