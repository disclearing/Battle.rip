package cc.stormworth.meetup.scenarios;

import cc.stormworth.meetup.Meetup;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.managers.UserManager;
import cc.stormworth.meetup.scenarios.impl.NoCleanPlusScenario;
import cc.stormworth.meetup.scenarios.impl.NoCleanScenario;
import cc.stormworth.meetup.scenarios.impl.TimebombScenario;
import cc.stormworth.meetup.states.Mode;
import cc.stormworth.meetup.util.Colors;
import cc.stormworth.meetup.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public abstract class Scenario {

    private static final Map<String, Scenario> scenarios = new HashMap<>();

    private final String name;

    private final ItemStack icon;

    private final String[] description;

    private boolean active;

    private final boolean special;

    public Scenario(String name, ItemStack icon, boolean special, String... description) {
        this.name = name;
        this.icon = icon;
        this.special = special;
        this.description = description;
        scenarios.put(name, this);
    }

    public static Map<String, Scenario> getScenarios() {
        return scenarios;
    }

    protected static boolean isScenarioActive(String input) {
        return getByName(input).isActive();
    }

    public static Scenario getByName(String input) {
        return scenarios.get(input);
    }

    public static int getActiveScenarios() {
        return (int) scenarios.values().stream().filter(Scenario::isActive).count();
    }

    public static List<Scenario> getEnabledScenarios() {
        return scenarios.values().stream().filter(Scenario::isActive).collect(Collectors.toList());
    }

    public static List<Scenario> getOrdinaryScenarios() {
        return scenarios.values().stream().filter(scenario -> !scenario.isSpecial()).collect(Collectors.toList());
    }

    public static List<Scenario> getSpecialScenarios() {
        return scenarios.values().stream().filter(scenario -> scenario.isSpecial()).collect(Collectors.toList());
    }

    public static void enableRandomScenarios() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int scenarioCount = random.nextInt(1, 2);
        List<Scenario> scenarioList = getOrdinaryScenarios();

        Collections.shuffle(scenarioList);

        for (int i = 0; i <= scenarioCount; i++) {
            Scenario scenario = scenarioList.get(random.nextInt(scenarioList.size()));

            while (scenario.isActive()) {
                scenario = scenarioList.get(random.nextInt(scenarioList.size()));
            }

            while (scenario.getName().equals("NoClean+") && (getByName("NoClean").isActive()
                    || getByName("Timebomb").isActive() || getByName("SafeLoot").isActive())) {
                scenario = scenarioList.get(random.nextInt(scenarioList.size()));
            }

            while (scenario.getName().equals("NoClean") && (getByName("NoClean+").isActive())) {
                scenario = scenarioList.get(random.nextInt(scenarioList.size()));
            }

            while (scenario.getName().equals("SafeLoot")
                    && (getByName("NoClean+").isActive() || getByName("Timebomb").isActive())) {
                scenario = scenarioList.get(random.nextInt(scenarioList.size()));
            }

            while (scenario.getName().equals("Timebomb")
                    && (getByName("NoClean+").isActive() || getByName("SafeLoot").isActive())) {
                scenario = scenarioList.get(random.nextInt(scenarioList.size()));
            }

            scenario.toggle(null, false);
        }

        if (ThreadLocalRandom.current().nextFloat() < 0.06f && !getByName("Absorptionless").isActive()) {
            getByName("Absorptionless").toggle(null, false);
        }
    }

    public String getName() {
        return name;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public String[] getDescription() {
        return description;
    }

    public boolean isActive() {
        boolean to2 = GameManager.getInstance().getMode() == Mode.TO2;
        if (name.equals("NoClean+") && to2) {
            return false;
        }

        if (name.equals("NoClean+") && to2) {
            return false;
        }

        return active;
    }

    public boolean isSpecial() {
        return special;
    }

    public void disable(boolean broadcast) {
        this.active = false;
        HandlerList.unregisterAll((Listener) this);
        if (broadcast)
            Bukkit.broadcastMessage(Colors.SECONDARY + "Scenario " + Colors.PRIMARY + this.name + Colors.SECONDARY + " is "
                    + (this.active ? "now" : "no longer") + Colors.SECONDARY + " active.");
        switch (this.name) {
            case "NoClean":
                NoCleanScenario.cleanup();
                break;
            case "NoClean+":
                NoCleanPlusScenario.cleanup();
                break;
            case "Timebomb":
                TimebombScenario.cleanup();
                break;
        }
    }

    public void toggle(Player player, boolean broadcast) {
        this.active = !this.active;
        if (this instanceof Listener) {
            if (this.active) {
                Bukkit.getPluginManager().registerEvents((Listener) this, Meetup.getInstance());
            } else {
                HandlerList.unregisterAll((Listener) this);
            }
        }

        if (player != null)
            player.playSound(player.getLocation(), this.active ? Sound.NOTE_PIANO : Sound.GLASS,
                    this.active ? 1.0F : 20.0F, this.active ? 1.0F : 5.0F);
        if (broadcast && !this.name.equals("Rush") && player != null) {
            player.sendMessage(Colors.SECONDARY + "Scenario " + Colors.PRIMARY + this.name + Colors.SECONDARY + " is "
                    + (this.active ? "now" : "no longer") + Colors.SECONDARY + " active.");
        }
        switch (this.name) {
            case "NoClean":
                if (this.active) {
                    NoCleanScenario.start();
                    break;
                }
                NoCleanScenario.cleanup();
                break;
            case "NoClean+":
                if (this.active) {
                    NoCleanPlusScenario.start();
                    break;
                }
                NoCleanPlusScenario.cleanup();
                break;
            case "Timebomb":
                if (this.active) {
                    TimebombScenario.start();
                    break;
                }
                TimebombScenario.cleanup();
                break;
        }
    }

    public ItemStack getItem(boolean editor) {
        List<String> lore = new ArrayList<>();
        if (editor)
            lore.add(Colors.DARK_GRAY + "Scenario");
        lore.add("");
        Arrays.stream(this.description).map(description -> Colors.GRAY + description).forEach(lore::add);
        if (editor) {
            lore.add("");
            lore.add(Colors.GRAY + "Status: " + (this.active ? (Colors.GREEN + "Enabled") : (Colors.RED + "Disabled")));
            lore.add("");
            lore.add(Colors.SECONDARY + "Click to toggle!");
        }
        ItemBuilder item = (new ItemBuilder(this.icon.getType())).setDurability(this.icon.getDurability())
                .setAmount(this.icon.getAmount())
                .setName((!editor ? Colors.PRIMARY : (this.active ? Colors.GREEN : Colors.RED)) + this.name)
                .setLore(lore);
        return (this.active && editor) ? item.addEnchant(Enchantment.LUCK, 1).build() : item.build();
    }

    protected boolean notPlaying(Player player) {
        return !UserManager.getInstance().getUser(player.getUniqueId()).isAlive();
    }
}
