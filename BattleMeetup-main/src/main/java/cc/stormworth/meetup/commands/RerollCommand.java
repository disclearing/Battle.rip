package cc.stormworth.meetup.commands;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.managers.KitManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.scenarios.Scenario;
import cc.stormworth.meetup.states.GameState;
import cc.stormworth.meetup.style.Style;
import cc.stormworth.meetup.style.hcf.kit.Kit;
import cc.stormworth.meetup.util.Colors;
import cc.stormworth.meetup.util.Msg;
import cc.stormworth.meetup.util.PlayerUtil;
import cc.stormworth.meetup.util.TaskUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RerollCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) {
            Msg.logConsole(CC.SECONDARY + "This" + CC.PRIMARY + " Meetup" + CC.SECONDARY + " command can only be performed by players.");
            return false;
        }

        Player player = (Player) commandSender;

        if (GameManager.getInstance().isLobby()) {
            player.sendMessage(Colors.RED + "You cannot use this command right now.");
            return false;
        }

        if (GameManager.getInstance().getGameState() != GameState.SCATTER) {
            player.sendMessage(Colors.RED + "You cannot use this command right now.");
            return false;
        }

        if (UserManager.getInstance().getUser(player.getUniqueId()).getStatistics().getRerolls() <= 0) {
            player.sendMessage(Colors.RED + "You do not have any more rerolls.");
            return false;
        }

        PlayerUtil.clearPlayer(player);

        player.sendMessage(Colors.SECONDARY + "You have rerolled your kit.");
        UserManager.getInstance().getUser(player.getUniqueId()).getStatistics().setRerolls(UserManager.getInstance().getUser(player.getUniqueId()).getStatistics().getRerolls() - 1);

        TaskUtil.runLater(() -> {

            if (GameManager.getInstance().getStyle() == Style.UHC) {
                if (Scenario.getByName("OP Kits").isActive())
                    KitManager.getInstance().giveKitItemContainerToPlayer(KitManager.getInstance().getRandomOPKit(), player);
                else
                    KitManager.getInstance().giveKitItemContainerToPlayer(KitManager.getInstance().getRandomKit(), player);
            } else {

                Kit kit = Kit.findAny();

                kit.apply(player);
            }
        }, 1L);

        return false;
    }
}
