package dev.nulledcode.spigot.knockback;

public interface KnockbackProfile {

    String getName();

    double getFriction();

    void setFriction(double friction);

    double getHorizontal();

    void setHorizontal(double horizontal);

    double getVertical();

    void setVertical(double vertical);

    double getVerticalLimit();

    void setVerticalLimit(double verticalLimit);

    double getExtraHorizontal();

    void setExtraHorizontal(double extraHorizontal);

    double getExtraVertical();

    void setExtraVertical(double extraVertical);

    double getRodVertical();

    void setRodVertical(double extraVertical);

    double getRodHorizontal();

    void setRodHorizontal(double extraVertical);

    void setOnePointSeven(boolean onePointSevent);

    boolean isOnePointSevent();
}