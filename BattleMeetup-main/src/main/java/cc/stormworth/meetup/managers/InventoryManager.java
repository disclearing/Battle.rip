package cc.stormworth.meetup.managers;

import cc.stormworth.core.rank.Rank;
import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.scenarios.Scenario;
import cc.stormworth.meetup.states.Mode;
import cc.stormworth.meetup.style.Style;
import cc.stormworth.meetup.user.UserData;
import cc.stormworth.meetup.util.Colors;
import cc.stormworth.meetup.util.ItemBuilder;
import cc.stormworth.meetup.util.MathUtil;
import cc.stormworth.meetup.util.PlayerUtil;
import lombok.Getter;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class InventoryManager {

    @Getter
    private static final InventoryManager instance = new InventoryManager();

    /**
     * Lobby inventory:
     */

    private final ItemStack configItem = new ItemBuilder(Material.BOOK).setName(Colors.PRIMARY + "Configuration")
            .setLore(CC.SECONDARY + "Click to open!").build();

    private final ItemStack styleItem = new ItemBuilder(Material.WATCH).setName(Colors.PRIMARY + "Game Style")
            .setLore(CC.SECONDARY + "Click to open!").build();

    private final ItemStack kitEditorItem = new ItemBuilder(Material.ITEM_FRAME)
            .setName(Colors.PRIMARY + "Kit Editor").setLore(CC.SECONDARY + "Click to open!").build();

    private final ItemStack leaderboardItem = new ItemBuilder(Material.EMERALD)
            .setName(Colors.PRIMARY + "Leaderboard").setLore(CC.SECONDARY + "Click to open!").build();

    private final ItemStack yourStatsItem = new ItemBuilder(Material.NETHER_STAR)
            .setName(Colors.PRIMARY + "Your Stats").setLore(CC.SECONDARY + "Click to open!").build();

    private final ItemStack teamsItem = new ItemBuilder(Material.SKULL_ITEM).setDurability(3).setName(Colors.PRIMARY + "Team")
            .setLore(Colors.SECONDARY + "Left click to invite!", Colors.SECONDARY + "Right click to open!")
            .setSkullOwner(null).build();
    /**
     * Spectator inventory:
     */

    private final ItemStack spectateMenuItem = new ItemBuilder(Material.ITEM_FRAME)
            .setName(Colors.PRIMARY + "Spectate Menu").setLore(CC.SECONDARY + "Click to open!").build();
    private final ItemStack centerTeleportItem = new ItemBuilder(Material.INK_SACK)
            .setName(Colors.PRIMARY + "Center Teleport").setLore(Colors.SECONDARY + "Click to teleport!")
            .setDurability(12).build();
    private final ItemStack randomTeleportItem = new ItemBuilder(Material.RECORD_12)
            .setName(Colors.PRIMARY + "Random Teleport").setLore(Colors.SECONDARY + "Click to teleport!")
            .build();
    private final ItemStack playAgainItem = new ItemBuilder(Material.PAPER)
            .setName(CC.PRIMARY + "Play Again")
            .setLore(CC.SECONDARY + "Click to open!")
            .build();
    private final ItemStack hideHandsItem = new ItemBuilder(Material.CARPET).setName(Colors.PRIMARY + "Hide Hands")
            .setDurability(8).build();

    public void setupLobbyInventory(Player p) {
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.getInventory().setItem(0, styleItem);
        p.getInventory().setItem(1, configItem);

        if (GameManager.getInstance().getMode() == Mode.TO2) {
            p.getInventory().setItem(4, teamsItem);
        }

        p.getInventory().setItem(6, kitEditorItem);
        p.getInventory().setItem(7, leaderboardItem);
        p.getInventory().setItem(8, yourStatsItem);
        p.updateInventory();
        p.getInventory().setHeldItemSlot(0);
    }

    public void setupSpectatorInventory(Player p) {
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.getInventory().setItem(0, spectateMenuItem);
        p.getInventory().setItem(1, centerTeleportItem);
        p.getInventory().setItem(2, randomTeleportItem);
        p.getInventory().setItem(7, playAgainItem);
        p.getInventory().setItem(8, hideHandsItem);
        p.updateInventory();
        p.getInventory().setHeldItemSlot(0);
    }

    public void openSpectateMenuInventory(Player p) {
        Inventory spectateMenuInventory = Bukkit.createInventory(null, 36, "Spectate Menu");

        int slot = 10;

        for (UserData user : UserManager.getInstance().getUsers()) {

            if (user.isAlive()) {

                if (slot < 26) {
                    ItemStack headItem = new ItemBuilder(Material.SKULL_ITEM)
                            .setName(Colors.PRIMARY + user.getName())
                            .setLore(Colors.SECONDARY + "Click to teleport!").setDurability(3)
                            .setSkullOwner(user.getName()).build();

                    spectateMenuInventory.setItem(slot, headItem);
                    slot++;
                }
            }
        }

        p.openInventory(spectateMenuInventory);
    }

    /**
     * Config inventory:
     */

    public void openConfigInventory(Player p) {
        Inventory configInventory = Bukkit.createInventory(null, 27, "Configuration");

        ItemStack teamSizeItem = new ItemBuilder(Material.PAPER).setName(Colors.PRIMARY + "Team Size")
                .setLore("", Colors.GRAY + "What is the team size?", "",
                        Colors.GRAY + "Status: " + Colors.RESET
                                + GameManager.getInstance().getMode().toString())
                .build();

        ItemStack firstBorderShrink = new ItemBuilder(Material.WATCH)
                .setName(Colors.PRIMARY + "First Border Shrink")
                .setLore("", Colors.GRAY + "When does the border start shrinking?", "",
                        Colors.GRAY + "Status: " + Colors.RESET
                                + GameManager.getInstance().getBorderTime() / 60)
                .build();

        ItemStack styleItem = new ItemBuilder(Material.BEACON).setName(Colors.PRIMARY + "Game Style")
                .setLore("",
                        CC.GRAY + "Currently voted: ",
                        Colors.GRAY + ' ' + '●' + ' ' + Colors.RESET + Style.getCurrentlyVoted(),
                        "",
                        CC.SECONDARY + "Click to vote!").build();

        ItemStack borderItem = new ItemBuilder(Material.BEDROCK).setName(Colors.PRIMARY + "Border")
                .setLore("", Colors.GRAY + "What is the world radius?", "",
                        Colors.GRAY + "Status: " + Colors.RESET
                                + GameManager.getInstance().getBorder().getSize(),
                        "", Colors.SECONDARY + "This value adapts on the",
                        Colors.SECONDARY + "amount of players playing.")
                .build();

        ItemStack slotsItem = new ItemBuilder(Material.SKULL_ITEM).setName(Colors.PRIMARY + "Slots")
                .setLore("", Colors.GRAY + "How many players are",
                        Colors.GRAY + "able to join the game?", "",
                        Colors.GRAY + "Status: " + Colors.RESET
                                + GameManager.getInstance().getSlots())
                .setDurability(3).build();

        configInventory.setItem(4, teamSizeItem);
        configInventory.setItem(12, firstBorderShrink);
        configInventory.setItem(13, styleItem);
        configInventory.setItem(14, borderItem);
        configInventory.setItem(22, slotsItem);

        p.openInventory(configInventory);
    }

    /**
     * Scenario inventory:
     */

    public void openScenarioInventory(Player p) {
        Inventory scenarioInventory = Bukkit.createInventory(null, 27, "Scenarios");
        List<Scenario> scenarios = Scenario.getScenarios().values().stream().filter(Scenario::isActive)
                .sorted(Comparator.comparing(Scenario::getName)).collect(Collectors.toList());
        int slot = 10;

        for (Scenario scenario : scenarios) {
            scenarioInventory.setItem(slot, scenario.getItem(false));
            slot++;
        }

        ItemStack specialScenariosItem = new ItemBuilder(Material.NETHER_STAR)
                .setName(Colors.PRIMARY + "Special Scenarios")
                .setLore("", Colors.GRAY + "Special scenarios", Colors.GRAY + "can be enabled here.",
                        "", Colors.SECONDARY + "Click to open!")
                .build();

        if (PlayerUtil.testPermission(p, Rank.HERO)) {
            scenarioInventory.setItem(26, specialScenariosItem);
        }

        p.openInventory(scenarioInventory);
    }

    public void openSpecialScenariosInventory(Player p) {
        Inventory specialScenariosInventory = Bukkit.createInventory(null, 27, "Special Scenarios");
        List<Scenario> specialScenarios = Scenario.getScenarios().values().stream().filter(Scenario::isSpecial)
                .sorted(Comparator.comparing(Scenario::getName)).collect(Collectors.toList());
        int slot = 10;

        for (Scenario scenario : specialScenarios) {
            specialScenariosInventory.setItem(slot, scenario.getItem(true));
            slot++;
        }

        p.openInventory(specialScenariosInventory);
    }

    public void reloadSpecialScenariosInventory() {

        for (Player p : Bukkit.getOnlinePlayers()) {

            if (p.getOpenInventory().getTopInventory().getName().equals("Special Scenarios")) {
                Inventory specialScenariosInventory = p.getOpenInventory().getTopInventory();
                specialScenariosInventory.clear();
                List<Scenario> specialScenarios = Scenario.getScenarios().values().stream()
                        .filter(Scenario::isSpecial)
                        .sorted(Comparator.comparing(Scenario::getName))
                        .collect(Collectors.toList());
                int slot = 10;

                for (Scenario scenario : specialScenarios) {
                    specialScenariosInventory.setItem(slot, scenario.getItem(true));
                    slot++;
                }

                p.updateInventory();
            }
        }
    }

    /**
     * Player inventory:
     */

    public Inventory getPlayerInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, "[I] " + player.getName());
        ItemStack[] contents = player.getInventory().getContents();
        ItemStack[] armor = player.getInventory().getArmorContents();
        double health = player.getHealth();
        double food = player.getFoodLevel();

        List<String> potionEffectStrings = new ArrayList<>();
        player.getActivePotionEffects().forEach(potionEffect -> potionEffectStrings.add(CC.WHITE
                + WordUtils.capitalizeFully(potionEffect.getType().getName().replace("_", " ")) + " "
                + MathUtil.convertToRomanNumeral(potionEffect.getAmplifier() + 1) + CC.GRAY
                + " (" + MathUtil.convertTicksToMinutes(potionEffect.getDuration()) + ")"));

        for (int i = 0; i < 9; i++) {
            inventory.setItem(i + 27, contents[i]);
            inventory.setItem(i + 18, contents[i + 27]);
            inventory.setItem(i + 9, contents[i + 18]);
            inventory.setItem(i, contents[i + 9]);
        }

        int gapples = Arrays.stream(contents).filter(Objects::nonNull)
                .filter(d -> (d.getType() == Material.GOLDEN_APPLE)).mapToInt(ItemStack::getAmount)
                .sum();
        inventory.setItem(47, (new ItemBuilder(Material.GOLDEN_APPLE))
                .setAmount(gapples)
                .setName(CC.GRAY + "Golden Apples: " + CC.WHITE + gapples).build());
        inventory.setItem(48,
                (new ItemBuilder(Material.SKULL_ITEM))
                        .setName(CC.GRAY + "Health: " + CC.WHITE
                                + MathUtil.roundToHalves(health / 2.0D) + "/10")
                        .setAmount((int) Math.round(health / 2.0D)).build());
        inventory.setItem(49,
                (new ItemBuilder(Material.COOKED_BEEF))
                        .setName(CC.GRAY + "Hunger: " + CC.WHITE
                                + MathUtil.roundToHalves(food / 2.0D) + "/10")
                        .setAmount((int) Math.round(food / 2.0D)).build());
        inventory.setItem(50, (new ItemBuilder(Material.BREWING_STAND_ITEM))
                .setName(CC.GRAY + "Potion Effects").setAmount(potionEffectStrings.size())
                .setLore(potionEffectStrings).build());

        UserData data = UserManager.getInstance().getUser(player.getUniqueId());

        inventory.setItem(51, (new ItemBuilder(Material.SKULL_ITEM))
                .setName(CC.PRIMARY + "Statistics")
                .setLore(Arrays.asList("",
                        CC.GRAY + "Ranking: " + CC.WHITE
                                + data.getStatistics().getWins(),
                        CC.GRAY + "Matches: " + CC.WHITE
                                + data.getStatistics().getGamesPlayed(),
                        CC.GRAY + "Wins: " + CC.WHITE + data.getStatistics().getWins()))
                .build());

        for (int j = 36; j < 40; j++)
            inventory.setItem(j, armor[39 - j]);
        return inventory;
    }
}
