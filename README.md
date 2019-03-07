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
```
```q.queryString``` produces:
```sql
from Book 
where id = :p1 OR  status in (:p2)
```
and ```q.paramValues``` will give you a Map of params:
```groovy
[p1: 2, p2: [PUBLISHED, NEW]]
```

## Installation
TBD

## Usage

Build can accept the string instead of the model class, this string will be the beginning of the 
constructed HQL query.

Main section also accepts: ```count()```, ```delete()``` and ```limit()``` methods which change the whole 
behaviour of the query. 

### Predicates
All predicates are connected with the implicit ```AND``` operation, ```OR``` should be used explicitly instead.

|Predicate name|Comment|
|--------------|-----------|
| *eq()* / *neq()* | Simple equality/not equality. | 
| *in()* | Value in the list |
| *like()* / *iLike()* | Text search (with case insensitive option) |
| *gt()* / *gte()* /  *lt()* / *lte()* | &gt; / 	&ge; / &lt; / &le; |
| *isNull()* ||

 