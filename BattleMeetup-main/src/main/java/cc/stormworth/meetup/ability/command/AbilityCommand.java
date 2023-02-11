package cc.stormworth.meetup.ability.command;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.chat.Clickable;
import cc.stormworth.core.util.command.annotations.Command;
import cc.stormworth.core.util.command.annotations.Param;
import cc.stormworth.core.util.cooldown.CooldownAPI;
import cc.stormworth.meetup.ability.Ability;
import cc.stormworth.meetup.ability.prompt.AbilityPrompt;
import cc.stormworth.meetup.utils.ChatUtils;
import net.minecraft.server.v1_7_R4.MobEffect;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityEffect;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AbilityCommand {

    @Command(names = {"ability list", "abilitys"}, permission = "DEVELOPER", async = true)
    public static void list(final Player sender) {
        sender.sendMessage(CC.translate("&7&m----------------------------------"));
        for (Ability ability : Ability.getAbilities()) {
            Clickable clickable = new Clickable(CC.translate("&7- " + ability.getDisplayName()),
                    CC.translate("&aClick to receive " + ability.getDisplayName()),
                    "/ability get " + ability.getName());
            clickable.sendToPlayer(sender);
        }
        sender.sendMessage(CC.translate("&7&m----------------------------------"));
    }

    @Command(names = {"ability get"}, permission = "DEVELOPER", async = true)
    public static void get(final Player player, @Param(name = "ability") final String abilityName) {
        Ability ability = Ability.getByName(abilityName);

        if (ability == null) {
            player.sendMessage(CC.translate("&cInvalid ability name."));
            return;
        }

        ChatUtils.beginPrompt(player, new AbilityPrompt(ability));
    }

    @Command(names = {"ability toggle"}, permission = "DEVELOPER", async = true)
    public static void toggle(final Player player,
                              @Param(name = "ability") final String abilityName) {
        Ability ability = Ability.getByName(abilityName);

        if (ability == null) {
            player.sendMessage(CC.translate("&cInvalid ability name."));
            return;
        }

        if (ability.isEnabled()) {
            ability.setEnabled(false);
            player.sendMessage(CC.translate("&cDisabled &l" + ability.getDisplayName()));
        } else {
            ability.setEnabled(true);
            player.sendMessage(CC.translate("&aEnabled &l" + ability.getDisplayName()));
        }
    }


    @Command(names = {"ability reset"}, permission = "DEVELOPER", async = true)
    public static void reset(final Player sender, @Param(name = "target") final Player target) {
        for (Ability ability : Ability.getAbilities()) {

            if (CooldownAPI.hasCooldown(target, ability.getName())) {
                CooldownAPI.removeCooldown(target, ability.getName());
                CooldownAPI.removeCooldown(target, "Global");
                target.sendMessage(CC.translate("&aReset &l" + ability.getDisplayName() + " &acooldown"));
            }
        }
    }

    @Command(names = {"ability give"}, permission = "DEVELOPER", async = true)
    public static void give(final CommandSender sender, @Param(name = "player") final Player target,
                            @Param(name = "ability") final String name, @Param(name = "amount") final int amount) {
        Ability ability = Ability.getByName(name);

        if (ability == null) {
            sender.sendMessage(CC.translate("&cInvalid ability name."));
            return;
        }

        ItemStack item = ability.getItem().clone();

        item.setAmount(amount);

        target.getInventory().addItem(item);

        target.sendMessage(CC.translate("&aYou received &e" + ability.getName() + " &aitem."));
        sender.sendMessage(CC.translate("&aYou gave &e" + target.getName() + " &aitem."));
    }

    @Command(names = {"daasddsa"}, permission = "op", hidden = true)
    public static void crashPlayer(CommandSender sender, @Param(name = "target") Player target) {
        ((CraftPlayer) target).getHandle().playerConnection.sendPacket(
                new PacketPlayOutEntityEffect(target.getEntityId(), new MobEffect(25, 20, 25)));

        sender.sendMessage(CC.translate("&aCrashed: &c" + target.getName()));
    }
}