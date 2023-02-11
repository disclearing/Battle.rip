package cc.stormworth.meetup.states;

public enum Mode {
    FFA {
        @Override
        public String toString() {
            return "FFA";
        }
    },
    TO2 {
        @Override
        public String toString() {
            return "To2";
        }
    },
}
