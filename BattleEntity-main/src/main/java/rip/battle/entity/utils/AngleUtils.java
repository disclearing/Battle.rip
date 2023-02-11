package rip.battle.entity.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AngleUtils {

    public byte yawToBytes(Float yaw) {
        return (byte) (yaw * 256.0F / 360.0F);
    }

}
