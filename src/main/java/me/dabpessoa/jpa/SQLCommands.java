package me.dabpessoa.jpa;

/**
 * Created by diego.pessoa on 10/03/2017.
 */

/*
Implementar sintaxe dos comandos SQL
 */
public enum SQLCommands {

    SELECT("select"),
    FROM("from"),
    WHERE("where"),
    JOIN("join"),
    COUNT("count"),
    PARANTESES_LEFT("("),
    PARANTESES_RIGHT(")"),
    GROUP_BY("group by"),
    HAVING("having"),
    OBJECTS_SEPARATOR("."),
    PARAMS_SEPARATOR(","),
    WHITESPACE(" "),
    IN("in");

    private String sintaxe;

    SQLCommands(String sintaxe) {
        this.sintaxe = sintaxe;
    }

    public String sintaxe() {
        return sintaxe;
    }

}
