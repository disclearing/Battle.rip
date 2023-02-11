package cc.stormworth.meetup.states;

public enum GameState {
    OFFLINE {
        @Override
        public String toString() {
            return "Offline";
        }
    },
    WAITING {
        @Override
        public String toString() {
            return "Waiting";
        }
    },
    STARTING {
        @Override
        public String toString() {
            return "Starting";
        }
    },
    SCATTER {
        @Override
        public String toString() {
            return "Scatter";
        }
    },
    STARTED {
        @Override
        public String toString() {
            return "Started";
        }
    },
    ENDING {
        @Override
        public String toString() {
            return "Ending";
        }
    },
}
