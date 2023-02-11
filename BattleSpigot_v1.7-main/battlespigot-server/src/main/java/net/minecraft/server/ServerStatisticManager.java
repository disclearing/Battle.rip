package net.minecraft.server;

import net.minecraft.util.com.google.common.collect.Maps;
import net.minecraft.util.com.google.common.collect.Sets;
import net.minecraft.util.com.google.gson.JsonElement;
import net.minecraft.util.com.google.gson.JsonObject;
import net.minecraft.util.com.google.gson.JsonParseException;
import net.minecraft.util.com.google.gson.JsonParser;
import net.minecraft.util.org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spigotmc.SpigotConfig;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.Map.Entry;

public class ServerStatisticManager extends StatisticManager {

    private static final Logger b = LogManager.getLogger();
    private final MinecraftServer c;
    private final File d;
    private final Set e = Sets.newHashSet();
    private int f = -300;
    private boolean g = false;

    public ServerStatisticManager(MinecraftServer minecraftserver, File file1) {
        this.c = minecraftserver;
        this.d = file1;
        // Spigot start
        for ( String name : org.spigotmc.SpigotConfig.forcedStats.keySet() )
        {
            StatisticWrapper wrapper = new StatisticWrapper();
            wrapper.a( org.spigotmc.SpigotConfig.forcedStats.get( name ) );
            a.put( StatisticList.getStatistic( name ), wrapper );
        }
        // Spigot end
    }

    public void a() {
        if (SpigotConfig.disableSaving) return;
        if (this.d.isFile()) {
            try {
                this.a.clear();
                this.a.putAll(this.a(FileUtils.readFileToString(this.d)));
            } catch (IOException ioexception) {
                b.error("Couldn\'t read statistics file " + this.d, ioexception);
            } catch (JsonParseException jsonparseexception) {
                b.error("Couldn\'t parse statistics file " + this.d, jsonparseexception);
            }
        }
    }

    public void b() {
        if (SpigotConfig.disableSaving) return; // MineHQ
        if (org.spigotmc.SpigotConfig.disableStatSaving ) return; // Spigot
        try {
            FileUtils.writeStringToFile(this.d, a(this.a));
        } catch (IOException ioexception) {
            b.error("Couldn\'t save stats", ioexception);
        }
    }

    public void setStatistic(EntityHuman entityhuman, Statistic statistic, int i) {
        if (SpigotConfig.disableSaving) return; // MineHQ
        if (org.spigotmc.SpigotConfig.disableStatSaving ) return; // Spigot
        int j = statistic.d() ? this.getStatisticValue(statistic) : 0;

        super.setStatistic(entityhuman, statistic, i);
        this.e.add(statistic);
        if (statistic.d() && j == 0 && i > 0) {
            this.g = true;
            if (this.c.at()) {
                this.c.getPlayerList().sendMessage(new ChatMessage("chat.type.achievement", new Object[] { entityhuman.getScoreboardDisplayName(), statistic.j()}));
            }
        }
    }

    public Set c() {
        HashSet hashset = Sets.newHashSet(this.e);

        this.e.clear();
        this.g = false;
        return hashset;
    }

    public Map a(String s) {
        JsonElement jsonelement = (new JsonParser()).parse(s);

        if (!jsonelement.isJsonObject()) {
            return Maps.newHashMap();
        } else {
            JsonObject jsonobject = jsonelement.getAsJsonObject();
            HashMap hashmap = Maps.newHashMap();
            Iterator iterator = jsonobject.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                Statistic statistic = StatisticList.getStatistic((String) entry.getKey());

                if (statistic != null) {
                    StatisticWrapper statisticwrapper = new StatisticWrapper();

                    if (((JsonElement) entry.getValue()).isJsonPrimitive() && ((JsonElement) entry.getValue()).getAsJsonPrimitive().isNumber()) {
                        statisticwrapper.a(((JsonElement) entry.getValue()).getAsInt());
                    } else if (((JsonElement) entry.getValue()).isJsonObject()) {
                        JsonObject jsonobject1 = ((JsonElement) entry.getValue()).getAsJsonObject();

                        if (jsonobject1.has("value") && jsonobject1.get("value").isJsonPrimitive() && jsonobject1.get("value").getAsJsonPrimitive().isNumber()) {
                            statisticwrapper.a(jsonobject1.getAsJsonPrimitive("value").getAsInt());
                        }

                        if (jsonobject1.has("progress") && statistic.l() != null) {
                            try {
                                Constructor constructor = statistic.l().getConstructor(new Class[0]);
                                IJsonStatistic ijsonstatistic = (IJsonStatistic) constructor.newInstance(new Object[0]);

                                ijsonstatistic.a(jsonobject1.get("progress"));
                                statisticwrapper.a(ijsonstatistic);
                            } catch (Throwable throwable) {
                                b.warn("Invalid statistic progress in " + this.d, throwable);
                            }
                        }
                    }

                    hashmap.put(statistic, statisticwrapper);
                } else {
                    b.warn("Invalid statistic in " + this.d + ": Don\'t know what " + (String) entry.getKey() + " is");
                }
            }

            return hashmap;
        }
    }

    public static String a(Map map) {
        JsonObject jsonobject = new JsonObject();
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (((StatisticWrapper) entry.getValue()).b() != null) {
                JsonObject jsonobject1 = new JsonObject();

                jsonobject1.addProperty("value", Integer.valueOf(((StatisticWrapper) entry.getValue()).a()));

                try {
                    jsonobject1.add("progress", ((StatisticWrapper) entry.getValue()).b().a());
                } catch (Throwable throwable) {
                    b.warn("Couldn\'t save statistic " + ((Statistic) entry.getKey()).e() + ": error serializing progress", throwable);
                }

                jsonobject.add(((Statistic) entry.getKey()).name, jsonobject1);
            } else {
                jsonobject.addProperty(((Statistic) entry.getKey()).name, Integer.valueOf(((StatisticWrapper) entry.getValue()).a()));
            }
        }

        return jsonobject.toString();
    }

    public void d() {
        Iterator iterator = this.a.keySet().iterator();

        while (iterator.hasNext()) {
            Statistic statistic = (Statistic) iterator.next();

            this.e.add(statistic);
        }
    }

    public void a(EntityPlayer entityplayer) {
        int i = this.c.al();
        HashMap hashmap = Maps.newHashMap();

        if (this.g || i - this.f > 300) {
            this.f = i;
            Iterator iterator = this.c().iterator();

            while (iterator.hasNext()) {
                Statistic statistic = (Statistic) iterator.next();

                hashmap.put(statistic, Integer.valueOf(this.getStatisticValue(statistic)));
            }
        }

        entityplayer.playerConnection.sendPacket(new PacketPlayOutStatistic(hashmap));
    }

    public void updateStatistics(EntityPlayer entityplayer) {
        HashMap hashmap = Maps.newHashMap();
        Iterator iterator = AchievementList.e.iterator();

        while (iterator.hasNext()) {
            Achievement achievement = (Achievement) iterator.next();

            if (this.hasAchievement(achievement)) {
                hashmap.put(achievement, Integer.valueOf(this.getStatisticValue(achievement)));
                this.e.remove(achievement);
            }
        }

        entityplayer.playerConnection.sendPacket(new PacketPlayOutStatistic(hashmap));
    }

    public boolean e() {
        return this.g;
    }
}
