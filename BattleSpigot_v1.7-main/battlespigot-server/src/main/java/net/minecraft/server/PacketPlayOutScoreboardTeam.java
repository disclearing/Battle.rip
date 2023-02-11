package net.minecraft.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class PacketPlayOutScoreboardTeam extends Packet
{
    public String a;
    public String b;
    public String c;
    public String d;
    public Collection e;
    public int f;
    public int g;
    public boolean tagVisible;

    public PacketPlayOutScoreboardTeam() {
        this.a = "";
        this.b = "";
        this.c = "";
        this.d = "";
        this.e = new ArrayList();
        this.tagVisible = true;
    }

    public PacketPlayOutScoreboardTeam(final ScoreboardTeam scoreboardteam, final int i) {
        this.a = "";
        this.b = "";
        this.c = "";
        this.d = "";
        this.e = new ArrayList();
        this.tagVisible = scoreboardteam.isTagVisible();
        this.a = scoreboardteam.getName();
        this.f = i;
        if (i == 0 || i == 2) {
            this.b = scoreboardteam.getDisplayName();
            this.c = scoreboardteam.getPrefix();
            this.d = scoreboardteam.getSuffix();
            this.g = scoreboardteam.packOptionData();
        }
        if (i == 0) {
            this.e.addAll(scoreboardteam.getPlayerNameSet());
        }
        //this.tagVisible = true;
    }

    public PacketPlayOutScoreboardTeam(final ScoreboardTeam scoreboardteam, final Collection collection, final int i) {
        this.a = "";
        this.b = "";
        this.c = "";
        this.d = "";
        this.e = new ArrayList();
        this.tagVisible = scoreboardteam.isTagVisible();
        if (i != 3 && i != 4) {
            throw new IllegalArgumentException("Method must be join or leave for player constructor");
        }
        if (collection != null && !collection.isEmpty()) {
            this.f = i;
            this.a = scoreboardteam.getName();
            this.e.addAll(collection);
            return;
        }
        throw new IllegalArgumentException("Players cannot be null/empty");
    }

    @Override
    public void a(final PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.c(16);
        this.f = packetdataserializer.readByte();
        if (this.f == 0 || this.f == 2) {
            this.b = packetdataserializer.c(32);
            this.c = packetdataserializer.c(16);
            this.d = packetdataserializer.c(16);
            this.g = packetdataserializer.readByte();
        }
        if (this.f == 0 || this.f == 3 || this.f == 4) {
            final short short1 = packetdataserializer.readShort();
            for (int i = 0; i < short1; ++i) {
                this.e.add(packetdataserializer.c(40));
            }
        }
    }

    @Override
    public void b(final PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.a(this.a);
        packetdataserializer.writeByte(this.f);
        if (this.f == 0 || this.f == 2) {
            packetdataserializer.a(this.b);
            packetdataserializer.a(this.c);
            packetdataserializer.a(this.d);
            packetdataserializer.writeByte(this.g);
            if (packetdataserializer.version >= 16) {
                packetdataserializer.a(this.tagVisible ? "always" : "never");
                packetdataserializer.writeByte(-1);
            }
        }
        if (this.f == 0 || this.f == 3 || this.f == 4) {
            if (packetdataserializer.version < 16) {
                packetdataserializer.writeShort(this.e.size());
            }
            else {
                packetdataserializer.b(this.e.size());
            }

            Iterator iterator = this.e.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                packetdataserializer.a(s);
            }
        }
    }

    public void a(final PacketPlayOutListener packetplayoutlistener) {
        packetplayoutlistener.a(this);
    }

    @Override
    public void handle(final PacketListener packetlistener) {
        this.a((PacketPlayOutListener)packetlistener);
    }
}