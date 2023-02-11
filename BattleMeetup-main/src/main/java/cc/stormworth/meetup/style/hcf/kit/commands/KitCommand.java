package cc.stormworth.meetup.style.hcf.kit.commands;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.command.annotations.Command;
import cc.stormworth.core.util.command.annotations.Param;
import cc.stormworth.meetup.style.hcf.kit.Kit;
import cc.stormworth.meetup.util.TaskUtil;
import org.bukkit.entity.Player;

public class KitCommand {

    @Command(names = "kit create", permission = "HEADADMINISTRATOR")
    public static void create(Player player) {
        Kit kit = new Kit(Kit.getKits().size() + 1);

        if (player.getInventory().getArmorContents() != null) {
            kit.setArmor(player.getInventory().getArmorContents());
        }

        if (player.getInventory().getContents() != null) {
            kit.setContents(player.getInventory().getContents());
        }

        Kit.getKits().put(kit.getId(), kit);
        player.sendMessage(CC.translate("&eKit created with id &6" + kit.getId()));

        TaskUtil.runAsync(kit::saveKit);
    }

    @Command(names = {"kit setinventory", "kit set", "kit inventory"}, permission = "HEADADMINISTRATOR")
    public static void setinventory(Player player, @Param(name = "kit") int id) {
        Kit kit = Kit.getById(id);

        if (kit == null) {
            player.sendMessage(CC.translate("&cKit with id &6" + id + "&c does not exist"));
            return;
        }

        if (player.getInventory().getArmorContents() != null) {
            kit.setArmor(player.getInventory().getArmorContents());
        }

        if (player.getInventory().getContents() != null) {
            kit.setContents(player.getInventory().getContents());
        }


        player.sendMessage(CC.translate("&eKit &6#" + kit.getId() + "&e inventory updated."));

        TaskUtil.runAsync(kit::saveKit);
    }

    @Command(names = "kit remove", permission = "HEADADMINISTRATOR")
    public static void remove(Player player, @Param(name = "kit") int id) {
        Kit kit = Kit.getById(id);

        if (kit == null) {
            player.sendMessage(CC.translate("&cKit with id &6" + id + "&c does not exist"));
            return;
        }

        kit.remove();
        player.sendMessage(CC.translate("&eKit &6#" + kit.getId() + "&e removed."));
    }

    @Command(names = "kit list", permission = "HEADADMINISTRATOR")
    public static void list(Player player) {
        player.sendMessage(CC.translate("&eKits:"));
        for (Kit kit : Kit.getKits().values()) {
            player.sendMessage(CC.translate("&7- &6#" + kit.getId()));
        }
    }

    @Command(names = {"kit load", "kit apply", "kit get"}, permission = "HEADADMINISTRATOR")
    public static void load(Player player, @Param(name = "kit") int id) {
        Kit kit = Kit.getById(id);

        if (kit == null) {
            player.sendMessage(CC.translate("&cKit with id &6" + id + "&c does not exist"));
            return;
        }

        kit.apply(player);
        player.sendMessage(CC.translate("&eKit &6#" + kit.getId() + "&e inventory loaded."));
    }

}
