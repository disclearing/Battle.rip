package rip.battle.entity.utils;

import lombok.experimental.UtilityClass;
import net.minecraft.server.v1_8_R3.DataWatcher;
import org.apache.commons.lang.ObjectUtils;
import rip.battle.entity.npc.NPCEntity;

@UtilityClass
public class DataWatcherUtil {

    public DataWatcher createDataWatcher() {
        return new DataWatcher(null);
    }

    public DataWatcher createNPCDataWatcher(NPCEntity npc){
        DataWatcher dataWatcher = createDataWatcher();

        dataWatcher.a(0, (byte) 0);
        dataWatcher.a(2, npc.getDisplayName()); // Name

        dataWatcher.a(6, 20.0f); // health
        dataWatcher.a(7, 0);
        dataWatcher.a(8, (byte) 0);
        dataWatcher.a(9, (byte) 0);

        byte overlays = 0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40;
        dataWatcher.a(10, overlays);

        dataWatcher.a(17, 0.0F);
        dataWatcher.a(18, 0);

        return dataWatcher;
    }

    public void setTypeFlag(DataWatcher dataWatcher, int index, int flag, boolean value) {
        byte b0 = (byte) dataWatcher.j(index).a();

        if (value) {
            set(dataWatcher,index, (byte) (b0 | flag));
        } else {
            set(dataWatcher,index, (byte) (b0 & ~flag));
        }
    }

    public void setFlag(DataWatcher dataWatcher, int index, boolean value){
        byte b0 = (byte) dataWatcher.j(0).a();

        if (value) {
            set(dataWatcher, 0, (byte) (b0 | 1 << index));
        } else {
            set(dataWatcher, 0, (byte) (b0 & ~(1 << index)));
        }
    }

    public void set(DataWatcher dataWatcher, int index, Object value){
        DataWatcher.WatchableObject watchableObject = dataWatcher.j(index);
        int b = watchableObject.a();

        if (ObjectUtils.notEqual(b, value)){
            watchableObject.a(value);
            watchableObject.a(true);

            dataWatcher.setE(true);
        }
    }

}
