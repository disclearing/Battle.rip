package org.contrum.lobby.managelobby;

import cc.stormworth.core.util.skin.SkinTexture;
import org.contrum.lobby.LobbyPlugin;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import rip.battle.entity.hologram.CraftHologram;
import rip.battle.entity.hologram.api.Hologram;
import rip.battle.entity.hologram.impl.UpdatableHologram;
import rip.battle.entity.hologram.serialization.HologramTypeAdapter;
import rip.battle.entity.npc.NPCEntity;
import rip.battle.entity.npc.api.NPC;
import rip.battle.entity.npc.serialization.NPCTypeAdapter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author UKry
 * Created: 18/11/2022
 * Project BattleHub
 **/

@AllArgsConstructor
public class LobbyManager {

    private LobbyPlugin plugin;
    @Getter private final Map<String, NPCEntity> npcs = new HashMap<>();
    @Getter private final Map<String, Hologram> holograms = new HashMap<>();

    public void saveAll() {
        FileConfiguration config = plugin.getConfig();
        npcs.values().forEach(npc -> {
            config.set("NPC." + npc.getName().toLowerCase(), NPCTypeAdapter.toJson(npc).toString());
            plugin.saveConfig();
        });
        holograms.values().forEach(holo -> {
            config.set("HOLOGRAMS." + holo.getName().toLowerCase(), HologramTypeAdapter.toJson(holo).toString());
            plugin.saveConfig();
        });
    }

    public void loadAll() {
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection npcSection = config.getConfigurationSection("NPC");
        ConfigurationSection holoSection = config.getConfigurationSection("HOLOGRAMS");
        Location location = Bukkit.getWorld("spawn").getSpawnLocation().add(0, 200, 0);
        if(holoSection == null) {
            CraftHologram welcomeHolo = new CraftHologram("Welcome", location);
            welcomeHolo.addLine("&eWelcome"); //Change this text
            holograms.put("welcome", welcomeHolo);
        } else {
            AtomicInteger i = new AtomicInteger();
            holoSection.getKeys(false).forEach(s -> {
                i.getAndIncrement();
                holograms.put(s, HologramTypeAdapter.fromJson(new JsonParser().parse(config.getString("HOLOGRAMS." + s))));
            });
            System.out.println("Loaded " + i + " holograms!");
        }
        if(npcSection == null) {
            NPCEntity hcf = new NPCEntity("HCF", location);
            hcf.setOnRightClick(p -> p.performCommand("joinqueue hcf"));
            NPCEntity uhcf = new NPCEntity("UHCF", location);
            uhcf.setOnRightClick(p -> p.performCommand("joinqueue uhcf"));
            NPCEntity uhc = new NPCEntity("UHC", location);
            uhc.setOnRightClick(p -> p.performCommand("joinqueue uhc"));
            NPCEntity kitmap = new NPCEntity("KitMap", location);
            kitmap.setOnRightClick(p -> p.performCommand("joinqueue kitmap"));
            NPCEntity sg = new NPCEntity("SG", location);
            sg.setOnRightClick(p -> p.performCommand("joinqueue sg"));
            NPCEntity skywars = new NPCEntity("SkyWars", location);
            skywars.setOnRightClick(p -> p.performCommand("joinqueue skywars"));
            NPCEntity bedwars = new NPCEntity("BedWars", location);
            bedwars.setOnRightClick(p -> p.performCommand("joinqueue bedwars"));
            NPCEntity meetup = new NPCEntity("Meetup", location);
            meetup.setOnRightClick(p -> p.performCommand("joinqueue meetup"));
            NPCEntity fightclub = new NPCEntity("FightClub", location);
            meetup.setOnRightClick(p -> p.performCommand("joinqueue fightclub"));
            NPCEntity arenapvp = new NPCEntity("ArenaPvP", location);
            arenapvp.setOnRightClick(p -> p.performCommand("joinqueue arenapvp"));
            NPCEntity comingsoon = new NPCEntity("ComingSoon", location);
            arenapvp.setMessages(Collections.singletonList("Coming Soon.."));
            try {
                hcf.setSkin(SkinTexture.getSkinFromUsername("Houp").get());
                uhcf.setSkin(SkinTexture.getSkinFromUsername("Palikka").get());
                uhc.setSkin(SkinTexture.getSkinFromUsername("Bloje").get());
                kitmap.setSkin(SkinTexture.getSkinFromUsername("Frozeado").get());
                sg.setSkin(SkinTexture.getSkinFromUsername("Average").get());
                skywars.setSkin(SkinTexture.getSkinFromUsername("LetsHugo").get());
                bedwars.setSkin(SkinTexture.getSkinFromUsername("SpookyScaryPlaty").get());
                meetup.setSkin(SkinTexture.getSkinFromUsername("maksitaxi").get());
                meetup.setSkin(SkinTexture.getSkinFromUsername("maksitaxi").get());
                fightclub.setSkin(SkinTexture.getSkinFromUsername("Dream").get());
                arenapvp.setSkin(SkinTexture.getSkinFromUsername("Stimpay").get());
                comingsoon.setSkin(SkinTexture.getSkinFromUsername("Marcel").get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }

            npcs.put("hcf", hcf);
            npcs.put("uhcf", uhcf);
            npcs.put("uhc", uhc);
            npcs.put("kitmap", kitmap);
            npcs.put("sg", sg);
            npcs.put("skywars", skywars);
            npcs.put("bedwars", bedwars);
            npcs.put("meetup", meetup);
            npcs.put("fightclub", fightclub);
            npcs.put("arenapvp", arenapvp);
            npcs.put("comingsoon", comingsoon);
        } else {
            AtomicInteger i = new AtomicInteger();
            npcSection.getKeys(false).forEach(s -> {
                i.getAndIncrement();
                NPCEntity entity = (NPCEntity) NPCTypeAdapter.fromJson(new JsonParser().parse(config.getString("NPC." + s)));
                entity.setOnRightClick(p -> p.performCommand("joinqueue " + entity.getName().toLowerCase()));
                npcs.put(s, entity);
            });
            System.out.println("Loaded " + i + " npc!");
        }
    }
}