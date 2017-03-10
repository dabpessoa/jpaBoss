package me.dabpessoa.jpa;

public enum ComparableType {

        EQUALS("="),
        IN("in"),
        NOT_EQUALS("<>"),
        LIKE("like"),
        ILIKE("ilike"),
        IS_NULL("is null"),
        IS_NOT_NULL("is not null");

        private String symbol;
        private ComparableType(String symbol) {
                this.symbol = symbol;
        }

        public String getSymbol() {
                return symbol;
        }

}