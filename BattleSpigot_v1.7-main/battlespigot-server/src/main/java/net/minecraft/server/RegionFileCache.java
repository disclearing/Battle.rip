package net.minecraft.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegionFileCache {

    public static final Map<File, RegionFile> a = new HashMap<>(); // CraftBukkit - private -> public

    // PaperSpigot start
    public static synchronized RegionFile a(File file1, int i, int j) {
        return a(file1, i, j, true);
    }

    public static synchronized RegionFile a(File file1, int i, int j, boolean create) {
    // PaperSpigot end
        File file2 = new File(file1, "region");
        File file3 = new File(file2, "r." + (i >> 5) + "." + (j >> 5) + ".mca");
        RegionFile regionfile = a.get(file3);

        if (regionfile != null) {
            return regionfile;
        } else {
            if (!create && !file3.exists()) { return null; } // PaperSpigot
            if (!file2.exists()) {
                file2.mkdirs();
            }

            if (a.size() >= 256) {
                a();
            }

            RegionFile regionfile1 = new RegionFile(file3);

            a.put(file3, regionfile1);
            return regionfile1;
        }
    }

    public static synchronized void a() {

        for (RegionFile regionfile : a.values()) {
            try {
                if (regionfile != null) {
                    regionfile.c();
                }
            } catch (IOException ioexception) {
                ioexception.printStackTrace();
            }
        }

        a.clear();
    }

    public static DataInputStream c(File file1, int i, int j) {
        RegionFile regionfile = a(file1, i, j);

        return regionfile.a(i & 31, j & 31);
    }

    public static DataOutputStream d(File file1, int i, int j) {
        RegionFile regionfile = a(file1, i, j);

        return regionfile.b(i & 31, j & 31);
    }
}
