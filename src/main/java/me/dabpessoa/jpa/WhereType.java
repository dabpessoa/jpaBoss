package me.dabpessoa.jpa;

/**
 * Created by diego.pessoa on 09/03/2017.
 */
public enum WhereType {
    AND("and"),
    OR("or");

    private String symbol;

    private WhereType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
