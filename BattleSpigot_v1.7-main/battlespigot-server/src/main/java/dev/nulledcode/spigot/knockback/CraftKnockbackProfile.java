package dev.nulledcode.spigot.knockback;

public class CraftKnockbackProfile implements KnockbackProfile {

    private String name;
    private double friction = 2.0D;
    private double horizontal = 0.35D;
    private double vertical = 0.35D;
    private double verticalLimit = 0.4000000059604645D;
    private double extraHorizontal = 0.425D;
    private double extraVertical = 0.085D;
    private double rodHorizontal = 1.2;
    private double rodVertical = 1.0;
    private boolean onePointSeven = true;

    public CraftKnockbackProfile(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getFriction() {
        return friction;
    }

    public void setFriction(double friction) {
        this.friction = friction;
    }

    public double getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(double horizontal) {
        this.horizontal = horizontal;
    }

    public double getVertical() {
        return vertical;
    }

    public void setVertical(double vertical) {
        this.vertical = vertical;
    }

    public double getVerticalLimit() {
        return verticalLimit;
    }

    public void setVerticalLimit(double verticalLimit) {
        this.verticalLimit = verticalLimit;
    }

    public double getExtraHorizontal() {
        return extraHorizontal;
    }

    public void setExtraHorizontal(double extraHorizontal) {
        this.extraHorizontal = extraHorizontal;
    }

    public double getExtraVertical() {
        return extraVertical;
    }

    public void setExtraVertical(double extraVertical) {
        this.extraVertical = extraVertical;
    }

    @Override
    public double getRodVertical() {
        return rodVertical;
    }

    @Override
    public void setRodVertical(double rodvertical) {
        this.rodVertical = rodvertical;
    }

    @Override
    public double getRodHorizontal() {
        return rodHorizontal;
    }

    @Override
    public void setRodHorizontal(double rodhorizontal) {
        this.rodHorizontal = rodhorizontal;
    }

    @Override
    public void setOnePointSeven(boolean b) {
        this.onePointSeven = b;
    }

    @Override
    public boolean isOnePointSevent() {
        return this.onePointSeven;
    }
}