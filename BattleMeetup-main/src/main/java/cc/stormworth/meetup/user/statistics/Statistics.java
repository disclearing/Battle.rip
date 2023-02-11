package cc.stormworth.meetup.user.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.RoundingMode;
import java.text.NumberFormat;

@Getter
@Setter
@AllArgsConstructor
public class Statistics {

    private int ranking;
    private int gamesPlayed;
    private int elo;
    private int wins;
    private int kills;
    private int deaths;
    private int rerolls;
    private String inventory;

    public String getKillDeathRatio() {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);

        float output;

        if (this.deaths <= 0) output = (float) this.kills;
        else output = (float) this.kills / this.deaths;

        return nf.format(output);
    }
}