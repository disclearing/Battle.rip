package rip.battle.entity.npc.commands.sub;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.core.util.skin.SkinTexture;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.OptArg;
import com.jonahseguin.drink.annotation.Sender;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import rip.battle.entity.npc.NPCManager;
import rip.battle.entity.npc.api.NPC;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class NPCSetSkinSubCommand {

    private final NPCManager npcManager;

    @Command(name = "setskin", desc = "Allows you to set the skin of a npc", usage = "<name> <skin>")
    public void setSkin(@Sender Player player, String name, @OptArg String username) {

        if (username == null) {
            username = player.getName();
        }

        NPC npc = npcManager.getNPC(name);

        if (npc == null) {
            player.sendMessage(CC.translate("&cA npc with that name does not exist."));
            return;
        }

        if (Objects.equals(username, player.getName())){
            npc.setSkin(SkinTexture.getPlayerTexture(player));
            player.sendMessage(CC.translate("&aYou have set your skin to the npc &f" + name + "&a."));
        } else {
            CompletableFuture<SkinTexture> skinFuture = SkinTexture.getSkinFromUsername(username);

            String finalUsername = username;
            skinFuture.thenAccept(skin -> {
                if (skin == null) {
                    player.sendMessage(CC.translate("&cA skin with that name does not exist."));
                    return;
                }

                npc.setSkin(skin);
                player.sendMessage(CC.translate("&aYou have set the skin of the npc &f" + name + "&a to &f" + finalUsername + "&a."));
            });

        }

    }
}
