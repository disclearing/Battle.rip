package cc.stormworth.meetup.user;

import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.kits.KitInventorySortation;
import cc.stormworth.meetup.kits.KitItemContainer;
import cc.stormworth.meetup.managers.KitManager;
import cc.stormworth.meetup.managers.TeamManager;
import cc.stormworth.meetup.states.PlayerState;
import cc.stormworth.meetup.style.hcf.enchantment.CustomEnchant;
import cc.stormworth.meetup.style.hcf.kit.HCFKitSortation;
import cc.stormworth.meetup.user.statistics.LeaderboardType;
import cc.stormworth.meetup.user.statistics.LeaderboardUser;
import cc.stormworth.meetup.user.statistics.Statistics;
import cc.stormworth.meetup.util.Cooldown;
import cc.stormworth.meetup.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UserData {

    @Getter
    private final UUID uniqueId;
    @Getter
    private final String name;
    @Getter
    private final Statistics statistics;
    @Getter
    private final Set<CustomEnchant> activeEnchants = new HashSet<>();
    @Getter
    @Setter
    private PlayerState playerState = PlayerState.LOBBY;
    @Getter
    @Setter
    private int kills = 0;
    @Getter
    @Setter
    private boolean joined = false;
    @Getter
    @Setter
    private int teamNumber = -1;
    @Getter
    @Setter
    private int votePower;
    @Getter
    @Setter
    private KitInventorySortation sortation = new KitInventorySortation();
    private KitItemContainer kitItemContainer;
    @Getter
    @Setter
    private HCFKitSortation hcfSortation;
    @Getter
    @Setter
    private Cooldown invincibilityTimer = new Cooldown(0);
    @Getter
    @Setter
    private boolean invincible = false;

    public UserData(UUID uuid, Statistics statistics, HCFKitSortation hcfSortation) {
        this.uniqueId = uuid;
        this.name = Bukkit.getOfflinePlayer(uuid).getName();

        this.statistics = statistics;
        this.hcfSortation = hcfSortation;

        this.loadKitInventorySortation();

        this.votePower = 1;
    }

    public LeaderboardUser findLeaderboardUser() {
        return Meetup.getInstance().getLeaderboards().get(LeaderboardType.ELO).getUsers().stream().filter(lu -> lu.getUuid().equals(this.uniqueId)).findFirst().orElse(null);
    }

    public boolean isAlive() {

        return this.playerState == PlayerState.INGAME;
    }

    public boolean hasTeam() {
        return this.teamNumber != -1;
    }

    public boolean hasFullTeam() {
        return this.hasTeam() && TeamManager.getInstance().getTeam(this.teamNumber).getMembers().size() >= 2;
    }

    private void loadKitInventorySortation() {

        if (this.getStatistics().getInventory().equals("None")) {
            return;
        }

        Inventory inventory = StringUtil.stringToInventory(this.getStatistics().getInventory());

        if (inventory == null) {
            //Bukkit.getLogger().log(Level.INFO, "[Meetup] Failed to load kit sortation from database!");
            return;
        }

        final ItemStack[] matrix = new ItemStack[inventory.getSize()];

        for (int slot = 0; slot < matrix.length; ++slot) {
            matrix[slot] = inventory.getItem((slot < 9) ? (slot + 27) : (slot - 9));
        }

        this.setSortation(KitInventorySortation.of(matrix));
        this.getStatistics().setInventory(StringUtil.inventoryToString(inventory));
    }

    public KitItemContainer getKitItemContainer() {

        if (kitItemContainer == null) {
            kitItemContainer = KitManager.getInstance().getRandomKit();
        }

        return kitItemContainer;
    }
}