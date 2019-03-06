package hql.builder

import groovy.util.logging.Slf4j

@Slf4j
class Predicate extends AbstractBuilder {

    private Long paramCounter = 0
    private Long runClosureCounter = 0
    private String paramPrefix = 'p_1'

    static Predicate build(@DelegatesTo(Predicate.class) Closure body) {
        Predicate predicate = new Predicate()
        runClosure(body, predicate)
        predicate
    }

    void or(@DelegatesTo(Predicate.class) Closure body) {
        runClosureCounter++
        Predicate predicate = new Predicate(separator: ' OR ', paramPrefix: paramPrefix + '_' + runClosureCounter)
        runClosure(body, predicate)
        addExpression(enclose(predicate.getQueryString()), predicate.getParamValues())
    }

    void and(@DelegatesTo(Predicate.class) Closure body) {
        runClosureCounter++
        Predicate predicate = new Predicate(separator: ' AND ', paramPrefix: paramPrefix + '_' + runClosureCounter)
        runClosure(body, predicate)
        addExpression(enclose(predicate.getQueryString()), predicate.getParamValues())
    }

    def eq(String expression, Object value) {
        String paramName = getParamName()
        addExpression("${expression} = :${paramName}", [(paramName): value])
    }

    def gte(String expression, Object value) {
        String paramName = getParamName()
        addExpression("${expression} >= :${paramName}", [(paramName): value])
    }

    def gt(String expression, Object value) {
        String paramName = getParamName()
        addExpression("${expression} > :${paramName}", [(paramName): value])
    }

    def lte(String expression, Object value) {
        String paramName = getParamName()
        addExpression("${expression} <= :${paramName}", [(paramName): value])
    }

    def lt(String expression, Object value) {
        String paramName = getParamName()
        addExpression("${expression} < :${paramName}", [(paramName): value])
    }

    def neq(String expression, Object value) {
        String paramName = getParamName()
        addExpression("${expression} != :${paramName}", [(paramName): value])
    }

    def like(String expression, String value = "") {

        String paramName = getParamName()
        addExpression("${expression} like :${paramName}", [(paramName): value])
    }

    def ilike(String expression, String value = "") {
        String paramName = getParamName()
        addExpression("upper(${expression}) like upper(:${paramName})", [(paramName): value])
    }

    def 'in'(String expression, Collection<?> values) {
        String paramName = getParamName()
        addExpression("${expression} in (:${paramName})", [(paramName): values])
    }

    def isNull(String expression) {
        addExpression("${expression} is null")
    }

    void apply(@DelegatesTo(Predicate.class) Closure customFilter) {
        runClosureCounter++
        runClosure(customFilter, this)
    }

    String getParamName() {
        paramCounter++
        paramPrefix + '_' + paramCounter
    }

    private static String enclose(String strToEnclose) {
        if (!strToEnclose) return strToEnclose

        "($strToEnclose)"
    }
}