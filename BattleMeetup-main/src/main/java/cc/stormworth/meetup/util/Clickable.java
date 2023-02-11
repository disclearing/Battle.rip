package cc.stormworth.meetup.util;

import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Clickable {
    private final List<TextComponent> components = new ArrayList<>();

    public Clickable(String msg) {
        this.components.add(new TextComponent(msg));
    }

    public Clickable(String msg, String hoverMsg, String clickString) {
        add(msg, hoverMsg, clickString);
    }

    public Clickable() {
    }

    public TextComponent add(String msg, String hoverMsg, String clickString) {
        TextComponent message = new TextComponent(msg);
        if (hoverMsg != null)
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(hoverMsg))
                    .create()));
        if (clickString != null && !clickString.equals(""))
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickString));
        this.components.add(message);
        return message;
    }

    public void add(String message) {
        this.components.add(new TextComponent(message));
    }

    public void sendToPlayer(Player player) {
        player.spigot().sendMessage((BaseComponent[]) this.components.toArray((Object[]) new TextComponent[0]));
    }
}
