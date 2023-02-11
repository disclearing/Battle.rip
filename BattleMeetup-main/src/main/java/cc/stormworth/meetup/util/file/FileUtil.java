package cc.stormworth.meetup.util.file;

import java.io.File;

public class FileUtil {

    public static boolean deleteDirectory(File path) {

        if (path.exists()) {
            File[] files = path.listFiles();

            if (files != null) {
                File[] var2 = files;
                int var3 = files.length;

                for (int var4 = 0; var4 < var3; ++var4) {
                    File file = var2[var4];

                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }

        return path.delete();
    }
}
