package dbms.query

class WhereClauseBuilder {
    private val clauses = mutableMapOf<String, WhereClause>()

    // TODO 1. equalsTo 함수 정의 - 문자열 혹은 숫자 속성을 가진 특정 필드가 특정 문자열 혹은 숫자와 같은 값을 가지는지 판단

    // TODO 2. like 함수 정의 - 문자열 속성을 가진 특정 필드가 특정 문자열을 포함하는지 판단

    internal fun build(): List<WhereClause> = clauses.map { it.value }
}
