package cc.stormworth.meetup.user.statistics;

public enum LeaderboardType {
    ELO {
        @Override
        public String toString() {
            return "Elo";
        }
    },
    WINS {
        @Override
        public String toString() {
            return "Wins";
        }
    },
    KILLS {
        @Override
        public String toString() {
            return "Kills";
        }
    }
}
