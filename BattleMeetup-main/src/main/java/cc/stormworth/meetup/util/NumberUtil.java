package cc.stormworth.meetup.util;

import cc.stormworth.core.util.chat.CC;
import cc.stormworth.meetup.managers.GameManager;
import cc.stormworth.meetup.user.UserData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtil {

    public static void handleEloChange(UserData killerUser, UserData victimUser) {
        EloCalculator calculator = GameManager.getInstance().getEloCalculator();

        int priorKillerElo = killerUser.getStatistics().getElo();
        int priorVictimElo = victimUser.getStatistics().getElo();

        EloCalculator.Division oldKillerDivision = calculator.getDivision(priorKillerElo);

        EloCalculator.Result result = calculator.calculate(priorKillerElo, priorVictimElo);

        killerUser.getStatistics().setElo(result.getWinnerNew());
        victimUser.getStatistics().setElo(result.getLoserNew());

        EloCalculator.Division newKillerDivision = calculator.getDivision(killerUser.getStatistics().getElo());

        Player killer = Bukkit.getPlayer(killerUser.getUniqueId());

        if (killer != null) {
            if (oldKillerDivision != newKillerDivision) {
                killer.sendMessage(CC.translate("&aYou have been promoted to &b" + newKillerDivision.getColor() + newKillerDivision.getName()));

                Bukkit.broadcastMessage(CC.translate("&d" + killer.getDisplayName() + " &ehas been upranked to " +
                        newKillerDivision.getColor() + newKillerDivision.getName() + "&e."));
            }
        }

        if (Bukkit.getPlayer(killerUser.getUniqueId()) != null)
            Bukkit.getPlayer(killerUser.getUniqueId()).sendMessage(CC.SECONDARY + "You now have an elo rating of " + CC.PRIMARY + killerUser.getStatistics().getElo() + CC.SECONDARY + " since you've obtained " + CC.PRIMARY + (result.getWinnerGain()) + " elo" + CC.SECONDARY + ".");
        if (Bukkit.getPlayer(victimUser.getUniqueId()) != null)
            Bukkit.getPlayer(victimUser.getUniqueId()).sendMessage(CC.SECONDARY + "You now have an elo rating of " + CC.PRIMARY + victimUser.getStatistics().getElo() + CC.SECONDARY + " since you've lost " + CC.PRIMARY + result.getLoserGain() + " elo" + CC.SECONDARY + ".");
    }

    public static void handleEloChange(int killerElo, UserData victimUser) {
        int priorVictimElo = victimUser.getStatistics().getElo();
        EloCalculator calculator = GameManager.getInstance().getEloCalculator();

        EloCalculator.Result result = calculator.calculate(killerElo, priorVictimElo);
        victimUser.getStatistics().setElo(result.getLoserNew());

        if (Bukkit.getPlayer(victimUser.getUniqueId()) != null)
            Bukkit.getPlayer(victimUser.getUniqueId()).sendMessage(CC.SECONDARY + "You now have an elo rating of " + CC.PRIMARY + victimUser.getStatistics().getElo() + CC.SECONDARY + " since you've lost " + CC.PRIMARY + result.getLoserGain() + " elo" + CC.SECONDARY + ".");
    }

    public static void handleEloChange(int killerElo, UserData victimUser, Player death) {
        if(death.hasMetadata("leftWhileScatter")) return;

        int priorVictimElo = victimUser.getStatistics().getElo();
        EloCalculator calculator = GameManager.getInstance().getEloCalculator();

        EloCalculator.Result result = calculator.calculate(killerElo, priorVictimElo);
        victimUser.getStatistics().setElo(result.getLoserNew());

        if (Bukkit.getPlayer(victimUser.getUniqueId()) != null)
            Bukkit.getPlayer(victimUser.getUniqueId()).sendMessage(CC.SECONDARY + "You now have an elo rating of " + CC.PRIMARY + victimUser.getStatistics().getElo() + CC.SECONDARY + " since you've lost " + CC.PRIMARY + result.getLoserGain() + " elo" + CC.SECONDARY + ".");
    }

    public static void handleEloChange(UserData killerUser, int victimElo) {
        EloCalculator calculator = GameManager.getInstance().getEloCalculator();

        int killerElo = killerUser.getStatistics().getElo();

        EloCalculator.Division oldKillerDivision = calculator.getDivision(killerElo);

        EloCalculator.Result result = calculator.calculate(killerElo, victimElo);
        killerUser.getStatistics().setElo(result.getWinnerNew());

        EloCalculator.Division newKillerDivision = calculator.getDivision(killerUser.getStatistics().getElo());

        Player killer = Bukkit.getPlayer(killerUser.getUniqueId());

        if (killer != null) {
            if (oldKillerDivision != newKillerDivision) {
                killer.sendMessage(CC.translate("&aYou have been promoted to &b" + newKillerDivision.getColor() + newKillerDivision.getName()));

                Bukkit.broadcastMessage(CC.translate("&d" + killer.getDisplayName() + " &ehas been upranked to " +
                        newKillerDivision.getColor() + newKillerDivision.getName() + "&e."));
            }
        }

        if (Bukkit.getPlayer(killerUser.getUniqueId()) != null)
            Bukkit.getPlayer(killerUser.getUniqueId()).sendMessage(CC.SECONDARY + "You now have an elo rating of " + CC.PRIMARY + killerUser.getStatistics().getElo() + CC.SECONDARY + " since you've obtained " + CC.PRIMARY + result.getWinnerGain() + " elo" + CC.SECONDARY + ".");
    }

    public static String calculate(long seconds) {
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - TimeUnit.DAYS.toHours(day);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.DAYS.toMinutes(day) - TimeUnit.HOURS.toMinutes(hours);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.DAYS.toSeconds(day) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minute);

        String hour_text = String.valueOf(hours), minute_text = String.valueOf(minute), second_text = String.valueOf(second);

        if (hours < 10) hour_text = "0" + hour_text;
        if (minute < 10) minute_text = "0" + minute_text;
        if (second < 10) second_text = "0" + second_text;
        return (hours == 0 ? minute_text + ":" + second_text : hour_text + ":" + minute_text + ":" + second_text);
    }

    public static long getDuration(String source) {
        return TimeUnit.MILLISECONDS.convert(fromString(source), TimeUnit.MILLISECONDS);
    }

    public static String millisToSeconds(long millis) {
        return (new DecimalFormat("#0.0")).format(((float) millis / 1000.0F));
    }

    public static String millisToRoundedTime(long millis) {
        millis++;
        long seconds = millis / 1000L;
        long minutes = seconds / 60L;
        long hours = minutes / 60L;
        long days = hours / 24L;
        long weeks = days / 7L;
        long months = weeks / 4L;
        long years = months / 12L;
        minutes++;
        if (years > 0L)
            return years + " year" + ((years == 1L) ? "" : "s");
        if (months > 0L)
            return months + " month" + ((months == 1L) ? "" : "s");
        if (weeks > 0L)
            return weeks + " week" + ((weeks == 1L) ? "" : "s");
        if (days > 0L)
            return days + " day" + ((days == 1L) ? "" : "s");
        if (hours > 0L)
            return hours + " hour" + ((hours == 1L) ? "" : "s");
        if (minutes > 0L)
            return minutes + " minute" + ((minutes == 1L) ? "" : "s");
        return seconds + " second" + ((seconds == 1L) ? "" : "s");
    }

    public static long fromString(String source) {
        if (source.isEmpty()) {
            return 0L;
        }

        long totalTime = 0L;
        boolean found = false;
        Matcher matcher = Pattern.compile("\\d+\\D+").matcher(source);

        while (matcher.find()) {
            String s = matcher.group();
            Long value = Long.parseLong(s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0]);
            String type = s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[1];

            switch (type) {
                case "s":
                    totalTime += value;
                    found = true;
                    break;
                case "m":
                    totalTime += value * 60;
                    found = true;
                    break;
                case "h":
                    totalTime += value * 60 * 60;
                    found = true;
                    break;
                case "d":
                    totalTime += value * 60 * 60 * 24;
                    found = true;
                    break;
                case "w":
                    totalTime += value * 60 * 60 * 24 * 7;
                    found = true;
                    break;
                case "M":
                    totalTime += value * 60 * 60 * 24 * 30;
                    found = true;
                    break;
                case "y":
                    totalTime += value * 60 * 60 * 24 * 365;
                    found = true;
                    break;
            }
        }

        return !found ? 0L : totalTime * 1000;
    }

    public static double roundToHalves(double d) {
        return Math.round(d * 2.0D) / 2.0D;
    }

    public static String convertTicksToMinutes(int ticks) {
        long minute = ticks / 1200L;
        long second = ticks / 20L - minute * 60L;
        String secondString = Math.round((float) second) + "";
        if (second < 10L)
            secondString = Character.MIN_VALUE + secondString;
        String minuteString = Math.round((float) minute) + "";
        if (minute == 0L)
            minuteString = "0";
        return minuteString + ":" + secondString;
    }

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
        }
        return null;
    }

    public static int convertEditorSlot(int slot) {
        return (slot < 9 ? slot + 27 : slot < 18 ? slot + 9 : slot < 27 ? slot - 9 : slot - 27);
    }
}
