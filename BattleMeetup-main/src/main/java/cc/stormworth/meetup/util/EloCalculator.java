package cc.stormworth.meetup.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;

import java.util.Arrays;

public final class EloCalculator {

    private final double kPower;
    private final int minEloGain;
    private final int maxEloGain;
    private final int minEloLoss;
    private final int maxEloLoss;

    public EloCalculator(double kPower, int minEloGain, int maxEloGain, int minEloLoss, int maxEloLoss) {
        this.kPower = kPower;
        this.minEloGain = minEloGain;
        this.maxEloGain = maxEloGain;
        this.minEloLoss = minEloLoss;
        this.maxEloLoss = maxEloLoss;
    }

    public Result calculate(int winnerElo, int loserElo) {
        double winnerQ = Math.pow(10, ((double) winnerElo) / 300D);
        double loserQ = Math.pow(10, ((double) loserElo) / 300D);

        double winnerE = winnerQ / (winnerQ + loserQ);
        double loserE = loserQ / (winnerQ + loserQ);

        int winnerGain = (int) (kPower * (1 - winnerE));
        int loserGain = (int) (kPower * (0 - loserE));

        winnerGain = Math.min(winnerGain, maxEloGain);
        winnerGain = Math.max(winnerGain, minEloGain);

        // loserGain will be negative so pay close attention here
        loserGain = Math.min(loserGain, -minEloLoss);
        loserGain = Math.max(loserGain, -maxEloLoss);

        return new Result(winnerElo, winnerGain, loserElo, loserGain);
    }

    public Division getDivision(int elo) {
        int finalElo = Math.max(0, elo);

        return Arrays.stream(Division.values())
                .filter(division -> finalElo >= division.getMinElo() && finalElo < division.getMaxElo())
                .findFirst()
                .orElse(null);
    }

    @Getter
    @RequiredArgsConstructor
    public enum Division {
        BRONZE("Bronze", 0, 1400, ChatColor.RED),
        SILVER("Silver", 1400, 1500, ChatColor.GRAY),
        GOLD("Gold", 1500, 1600, ChatColor.GOLD),
        PLATINUM("Platinum", 1600, 1700, ChatColor.WHITE),
        EMERALD("Emerald", 1700, 1800, ChatColor.GREEN),
        SAPPHIRE("Sapphire", 1800, 1900, ChatColor.DARK_BLUE),
        DIAMOND("Diamond", 1900, 2000, ChatColor.AQUA),
        CHAMPION("Champion", 2000, 2200, ChatColor.DARK_RED),
        GRAND_CHAMPION("Grand Champion", 2200, Integer.MAX_VALUE, ChatColor.BLACK);

        private final String name;
        private final int minElo, maxElo;
        private final ChatColor color;
    }

    @Getter
    public static class Result {

        private final int winnerOld, winnerGain, winnerNew;
        private final int loserOld, loserGain, loserNew;

        Result(int winnerOld, int winnerGain, int loserOld, int loserGain) {
            this.winnerOld = winnerOld;
            this.winnerGain = winnerGain;
            winnerNew = winnerOld + winnerGain;

            this.loserOld = loserOld;
            this.loserGain = loserGain;
            loserNew = loserOld + loserGain;
        }
    }
}