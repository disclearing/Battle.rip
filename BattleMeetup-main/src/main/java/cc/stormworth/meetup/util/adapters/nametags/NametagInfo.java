package cc.stormworth.meetup.util.adapters.nametags;

import cc.stormworth.meetup.nms.NMSHelper;
import lombok.Getter;
import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardTeam;

@Getter
public class NametagInfo {

    private final int count;
    private final String name;
    private final String prefix;
    private final String suffix;
    private final PacketPlayOutScoreboardTeam packet;

    public NametagInfo(String name, String prefix, String suffix) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;

        this.count = NametagManager.COUNT++;

        this.packet = new PacketPlayOutScoreboardTeam();

        String teamName = "§8§" + this.count + name;

        if (teamName.length() > 16) {
            teamName = teamName.substring(0, 16);
        }

        NMSHelper.setValueStatic(packet, "a", teamName);
        NMSHelper.setValueStatic(packet, "f", 0);
        NMSHelper.setValueStatic(packet, "b", teamName);
        NMSHelper.setValueStatic(packet, "c", prefix);
        NMSHelper.setValueStatic(packet, "d", suffix);
        NMSHelper.setValueStatic(packet, "g", 3);
    }

    // We're having #equals here because we don't want to check is #packet equal to some other packet
    @Override
    public boolean equals(Object other) {
        if (other instanceof NametagInfo) {
            NametagInfo otherNametag = (NametagInfo) other;
            return this.name.equals(otherNametag.name) && this.prefix.equals(otherNametag.prefix) && this.suffix.equals(otherNametag.suffix);
        }

        return false;
    }
}