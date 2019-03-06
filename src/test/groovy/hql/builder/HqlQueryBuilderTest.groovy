package hql.builder

import spock.lang.Specification

class HqlQueryBuilderTest extends Specification {
    def "Simple Predicate test"() {
        when:
        Predicate p = Predicate.build {
                or {
                    and {
                        eq('prop', new Object())
                        eq('prop4',new Object())
                    }
                    eq('prop2',new Object())
                }
                eq('prop3','testValue')
            }

        then:
//        ((prop = :rckGCJUc AND prop4 = :FeMEpILV) OR prop2 = :CLNMDcnc) AND prop3 = :zLrdTwzX
        p.getQueryString() ==~ /\(\(prop = :p_1_1_1_1+ AND prop4 = :p_1_1_1_2+\) OR prop2 = :p_1_1_1\) AND prop3 = :p_1_1+/

    }

    def "More complicated predicate test"() {
        when:
        Predicate p = Predicate.build {
            neq("t.param1", new Object())
            'in'('t.param2', [new Object()])
        }

        then:
        p.getQueryString() ==~ /t.param1 != :p_1_1 AND t.param2 in \(:p_1_2\)/
    }

    def "Query with predicate test"() {
        when:
        def query = HqlQuery.build("from SomeDomain t ") {
            leftJoin('t.reference', 'r')

            where {
                    eq('t.prop1', new Object())
                    'in'('t.prop2', [new Object()])
                    or {
                        and {
                            gte('r.dateProp1', new Date())
                            ilike('r.prop2', '%some_value%')
                        }
                        gte('t.dateProp', new Date())
                    }
                }
        }

        then:
        query.getQueryString() ==~ /from SomeDomain t \n left join t.reference as r\nWHERE\nt.prop1 = :p_1_1+ AND t.prop2 in \(:p_1_2\) AND \(\(r.dateProp1 >= :p_1_1_1_1 AND upper\(r.prop2\) like upper\(:p_1_1_1_2\)\) OR t.dateProp >= :p_1_1_1\)/
    }

    def "Query with select statements test"() {
        when:
        def query = HqlQuery.build("from SomeDomain t ") {
            leftJoin('t.reference', 'r')

            select("t.prop1")
        }

        then:
        query.getQueryString() ==~ /SELECT t.prop1\nfrom SomeDomain t \n left join t.reference as r/
    }

    def "Query with distinct count statement test"() {
        when:
        def query = HqlQuery.build("from SomeDomain t ") {
            count()
            distinct("t.prop2")
        }

        then:
        query.getQueryString() ==~ /SELECT count\( distinct t.prop2 \)\nfrom SomeDomain t /
    }

    def "Query with order test"() {
        when:
        def query = HqlQuery.build("from SomeDomain t ") {
            where {
                eq('t.prop',new Object())
            }
            order("t.prop2", SortOrder.DESC)
        }

        then:
        query.getQueryString() ==~ /from SomeDomain t \nWHERE\nt.prop = :p_1_1\nORDER BY t.prop2 DESC/
        query.getParamValues().size() == 1
    }

    def "Delete query" () {
        when:
        def query = HqlQuery.build("from SomeDomain") {
            delete()
            where {
                eq("id", 2)
            }
        }
        then:
        query.queryString ==~ /delete from SomeDomain\nWHERE\nid = :p_1_1/
    }

    def "Special cases: empty and Predicate test"() {
        when:
        Predicate p = Predicate.build {
            or {
                eq('prop2',new Object())
                and {}
            }
        }

        then:
//        (prop2 = :oqCGLlfe)
        p.getQueryString() ==~ /\(prop2 = :p_1_1_1\)/

    }

    def "Group by support" () {
        when:
        def query = HqlQuery.build("from SomeDomain") {
            count()
            group("field")
        }
        then:
        query.queryString == "SELECT count( * )\nfrom SomeDomain\nGROUP BY field"
    }
}
