package rip.battle.crates.reward;

public enum RewardType {

  COMMAND,
  ITEMS;

  public static RewardType getByName(String name) {
    for (RewardType type : RewardType.values()) {
      if (type.name().equalsIgnoreCase(name)) {
        return type;
      }
    }
    return null;
  }

}