package dbms.query

class WhereClauseBuilder {
    private val clauses = mutableMapOf<String, WhereClause>()

    infix fun String.equalsTo(other: Any) {
        clauses[this] = WhereClause(this, Operator.EQUALS, other)
    }

    infix fun String.like(other: Any) {
        clauses[this] = WhereClause(this, Operator.LIKE, other)
    }

    internal fun build(): List<WhereClause> = clauses.map { it.value }
}
