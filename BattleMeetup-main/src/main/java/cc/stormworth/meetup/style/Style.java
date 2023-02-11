package cc.stormworth.meetup.style;

import cc.stormworth.meetup.managers.UserManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
@RequiredArgsConstructor
public enum Style {
    UHC("UHC"),
    HCF("HCF");

    // Separate name attribute in case we add more game types that might not exactly match toString()
    private final String name;
    // Store votes here
    private final Set<UUID> playersVoted = new HashSet<>();
    // Needed for vote power
    @Setter
    private int voteCount = 0;

    public static Style getByName(String name) {
        return Arrays.stream(values()).filter(style -> style.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static Style getCurrentlyVoted() {
        return Arrays.stream(values()).max(Comparator.comparingInt(Style::getVoteCount)).orElse(null);
    }

    public String getVotePercentage() {
        int totalVotes = Arrays.stream(values()).mapToInt(Style::getVoteCount).sum();
        return Math.round(this.voteCount * 100 / (Math.max(totalVotes, 1))) + "%";
    }

    public void addVote(Player player) {
        this.playersVoted.add(player.getUniqueId());
        this.voteCount = this.voteCount + UserManager.getInstance().getUser(player.getUniqueId()).getVotePower();
    }

    public void removeVote(Player player) {
        if (!this.playersVoted.contains(player.getUniqueId())) return;

        this.playersVoted.remove(player.getUniqueId());
        this.voteCount = this.voteCount - UserManager.getInstance().getUser(player.getUniqueId()).getVotePower();
    }
}
