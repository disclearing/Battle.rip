package rip.battle.crates.utils;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import rip.battle.crates.reward.Reward;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class RandomUtils {

    @Getter
    private static final Random random = ThreadLocalRandom.current();

    public double getDouble(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    public Reward getRandomReward(List<Reward> list) {

        list.removeIf(reward -> reward.getChance() <= 0D);

        if (list.isEmpty()) return null;

        Reward reward = null;

        double total = list.stream().mapToDouble(Reward::getChance).sum();

        double index = getDouble(0D, total);
        double countWeight = 0D;

        for (Reward reward1 : list) {
            countWeight += reward1.getChance();
            if (countWeight >= index) {
                reward = reward1;
                break;
            }
        }

        return reward;
    }

}
