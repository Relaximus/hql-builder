package hql.builder

abstract class AbstractBuilder {
    protected StringBuilder expressions = new StringBuilder('')
    protected def separator = ' AND '
    def paramValues = [:]

    protected void addExpression( String expression, Map<?,?> params = null ) {
        if(!expression) return

        if( expressions ) {
            expressions << separator
        }
        expressions << expression
        if (params) {
            paramValues << params
        }
    }

    protected static runClosure(Closure runClosure, def delegate) {
        // Create clone of closure for threading access.
        Closure runClone = runClosure.clone()

        // Set delegate of closure to this builder.
        runClone.delegate = delegate

        // And only use this builder as the closure delegate.
        runClone.resolveStrategy = Closure.DELEGATE_FIRST

        // Run closure code.
        runClone()
    }

    @Override
    String toString() {
        expressions
    }

    String getQueryString() {
        expressions.toString()
    }
}
