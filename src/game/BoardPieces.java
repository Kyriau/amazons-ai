package game;

public enum BoardPieces {
    BLANK {
        @Override
        public String toString() {
            return ".";
        }
    },
    BLACK{
        @Override
        public String toString() {
            return "B";
        }
    },
    WHITE{
        @Override
        public String toString() {
            return "W";
        }
    },
    ARROW{
        @Override
        public String toString() {
            return "@";
        }
    };

}
