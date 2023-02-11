package net.minecraft.server;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ScoreboardTeam extends ScoreboardTeamBase
{
    private final Scoreboard a;
    private final String b;
    private final Set c;
    private String d;
    private String e;
    private String f;
    private boolean g;
    private boolean h;
    private boolean tagVisible;

    public ScoreboardTeam(final Scoreboard scoreboard, final String s) {
        this.c = new HashSet();
        this.e = "";
        this.f = "";
        this.g = true;
        this.h = true;
        this.tagVisible = true;
        this.a = scoreboard;
        this.b = s;
        this.d = s;
    }

    public ScoreboardTeam(final Scoreboard scoreboard, final String s, final boolean tagVisible) {
        this.c = new HashSet();
        this.e = "";
        this.f = "";
        this.g = true;
        this.h = true;
        this.tagVisible = tagVisible;
        this.a = scoreboard;
        this.b = s;
        this.d = s;
        //this.tagVisible = tagVisible;
    }

    @Override
    public String getName() {
        return this.b;
    }

    public String getDisplayName() {
        return this.d;
    }

    public void setDisplayName(final String s) {
        if (s == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        this.d = s;
        this.a.handleTeamChanged(this);
    }

    public Collection getPlayerNameSet() {
        return this.c;
    }

    public String getPrefix() {
        return this.e;
    }

    public void setPrefix(final String s) {
        if (s == null) {
            throw new IllegalArgumentException("Prefix cannot be null");
        }
        this.e = s;
        this.a.handleTeamChanged(this);
    }

    public String getSuffix() {
        return this.f;
    }

    public void setSuffix(final String s) {
        if (s == null) {
            throw new IllegalArgumentException("Suffix cannot be null");
        }
        this.f = s;
        this.a.handleTeamChanged(this);
    }

    @Override
    public String getFormattedName(final String s) {
        return this.getPrefix() + s + this.getSuffix();
    }

    public static String getPlayerDisplayName(final ScoreboardTeamBase scoreboardteambase, final String s) {
        return (scoreboardteambase == null) ? s : scoreboardteambase.getFormattedName(s);
    }

    @Override
    public boolean allowFriendlyFire() {
        return this.g;
    }

    public void setAllowFriendlyFire(final boolean flag) {
        this.g = flag;
        this.a.handleTeamChanged(this);
    }

    public boolean canSeeFriendlyInvisibles() {
        return this.h;
    }

    public void setCanSeeFriendlyInvisibles(final boolean flag) {
        this.h = flag;
        this.a.handleTeamChanged(this);
    }

    public int packOptionData() {
        int i = 0;
        if (this.allowFriendlyFire()) {
            i |= 0x1;
        }
        if (this.canSeeFriendlyInvisibles()) {
            i |= 0x2;
        }
        return i;
    }

    public boolean isTagVisible() {
        return this.tagVisible;
    }

    public void setTagVisible(final boolean tagVisible) {
        this.tagVisible = tagVisible;
    }
}