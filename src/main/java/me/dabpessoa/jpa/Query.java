package me.dabpessoa.jpa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diego.pessoa on 09/03/2017.
 */
public class Query {

    private List<String> selects;
    private List<String> froms;
    private List<String> joins;
    private List<String> wheres;
    private List<String> orders;
    private List<String> havings;
    private List<String> groupbyes;

    public Query() {}

    public static QueryBuilder create() {
        return new QueryBuilder(new Query());
    }

    public void addSelect(String select) {
        if (selects == null) selects = new ArrayList<>();
        selects.add(select);
    }

    public void addFrom(String from) {
        if (froms == null) froms = new ArrayList<>();
        froms.add(from);
    }

    public void addJoin(String join) {
        if (joins == null) joins = new ArrayList<>();
        joins.add(join);
    }

    public void addWhere(String where) {
        if (wheres == null) wheres = new ArrayList<>();
        wheres.add(where);
    }

    public void addOrder(String order) {
        if (orders == null) orders = new ArrayList<>();
        orders.add(order);
    }

    public void addHaving(String having) {
        if (havings == null) havings = new ArrayList<>();
        havings.add(having);
    }

    public void addGroupBy(String groupBy) {
        if (groupbyes == null) groupbyes = new ArrayList<>();
        groupbyes.add(groupBy);
    }

    public List<String> getSelects() {
        return selects;
    }

    public void setSelects(List<String> selects) {
        this.selects = selects;
    }

    public List<String> getFroms() {
        return froms;
    }

    public void setFroms(List<String> froms) {
        this.froms = froms;
    }

    public List<String> getJoins() {
        return joins;
    }

    public void setJoins(List<String> joins) {
        this.joins = joins;
    }

    public List<String> getWheres() {
        return wheres;
    }

    public void setWheres(List<String> wheres) {
        this.wheres = wheres;
    }

    public List<String> getOrders() {
        return orders;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }

    public List<String> getHavings() {
        return havings;
    }

    public void setHavings(List<String> havings) {
        this.havings = havings;
    }

    public List<String> getGroupbyes() {
        return groupbyes;
    }

    public void setGroupbyes(List<String> groupbyes) {
        this.groupbyes = groupbyes;
    }

}
