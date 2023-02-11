package cc.stormworth.meetup.util;

public class MathUtil {

    public static String convertToRomanNumeral(int number) {
        switch (number) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
            case 7:
                return "VII";
            case 8:
                return "VIII";
            case 9:
                return "IX";
            case 10:
                return "XI";
            case 11:
                return "XII";
            case 12:
                return "XIII";
            case 13:
                return "XIIV";
            case 14:
                return "XIV";
            case 15:
                return "XV";
            default:
                return null;
        }
    }

    public static int convertTicksToMinutes(int ticks) {
        return ticks / 20;
    }

    public static double roundToHalves(double d) {
        return Math.round(d * 2.0D) / 2.0D;
    }
}
