package dbms.query

internal data class WhereClause(
    val attributeName: String,
    val operator: Operator,
    val value: Any,
) {
    init {
        if (operator == Operator.LIKE && value !is String) {
            throw IllegalArgumentException("Operator \'like\' should be utilized with String values.")
        }
    }
}

