package hql.builder

class HqlQuery extends AbstractBuilder {
    String footer = ""
    String select
    String distinct
    String grouping
    int limit
    int offset
    boolean count = false
    boolean delete = false
    Predicate predicate

    static HqlQuery build(Class entityClass, @DelegatesTo(HqlQuery.class) Closure body) {
        build("from ${entityClass.getName()} ", body)
    }

    /**
     * The incipit can start with "from  " or
     * with the less common "select  from  ..."
     */
    static HqlQuery build(String incipit, @DelegatesTo(HqlQuery.class) Closure body) {
        HqlQuery query = new HqlQuery(separator: '\n')
        query.addExpression(incipit)
        runClosure(body, query)
        query
    }

    /**
     * Inner Join
     * @param expression what to join with
     * @param alias Aliased name for this join
     * @return
     */
    void join(String expression, String alias) {
        addExpression(" join ${expression} as ${alias}")
    }

    void leftJoin(String expression, String alias) {
        addExpression(" left join ${expression} as ${alias}")
    }

    void leftJoin(String expression, String alias, String onClosure) {
        addExpression(" left join ${expression} as ${alias} on ${onClosure}")
    }

    void rightJoin(String expression, String alias) {
        addExpression(" right join ${expression} as ${alias}")
    }

    void where (@DelegatesTo(Predicate.class) Closure whereBody) {
        predicate = Predicate.build whereBody
        addExpression("WHERE")
        addExpression(predicate.getQueryString())
        paramValues = predicate.paramValues
    }

    void group(String groupBy) {
        grouping = groupBy
    }

    void limit (int limit, int offset = 0) {
        this.limit = limit
        this.offset = offset
    }

    void count() {
        count = true
    }

    void delete () {
        delete = true
    }

    void distinct(String alias) {
        distinct = alias
    }

    void select(String selectStatement){
        select = selectStatement
    }

    private String getSelectStatement() {
        if(select) {
            return "SELECT ${select}\n"
        }

        if (delete) {
            return "delete "
        }

        if(!distinct && !count) {
            return ""
        }
        return "SELECT ${count ? 'count(' : ''} ${!distinct ? "*" : 'distinct ' + distinct} ${count ? ')' : ''}\n"
    }

    void orderBy(String orderByExpression) {
        footer = footer + (!footer ? "\nORDER BY " : ",") + orderByExpression
    }

    void order(String sortProperty, SortOrder sortOrder = SortOrder.ASC) {
        orderBy(sortProperty + " " + sortOrder.name())
    }

    void order(String sortProperty, String sortOrder) {
        orderBy(sortProperty + " " + sortOrder)
    }

    void buildPredicate() {
        if (predicate) {
            paramValues += predicate.paramValues
            addExpression(predicate.getQueryString())
        }
    }

    @Override
    String getQueryString() {
        return (selectStatement + super.getQueryString() +
                (grouping ? "\nGROUP BY ${grouping}" : "") +
                (!count ? footer : ""))

    }

    @Override
    String toString() {
        getQueryString()
    }
}