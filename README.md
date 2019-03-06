# Hql Builder [![Build Status](https://travis-ci.com/Relaximus/hql-builder.svg?branch=master)](https://travis-ci.com/Relaximus/hql-builder)

## Intro
This is a simple tool to create dynamic HQL queries in more fancy way, using groovy DSL. It looks like this:
```groovy
def q = HqlQuery.build(Book) {
    where {
        or {
            eq("id", 2)
            'in'("status", [BookStatuses.PUBLISHED, BookStatuses.NEW])
        }
        
    }
}

println q.queryString // from Book where id = :p1 OR  status in (:p2)
println q.paramValues // [p1: 2, p2: [PUBLISHED, NEW]]
```

 