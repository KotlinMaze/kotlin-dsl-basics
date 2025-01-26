package dbms.query

class SelectQueryBuilder {
    private val query = SelectQuery()

    fun columns(vararg values: String) {
        query.addColumns(values)
    }

    fun from(tableName: String) {
        query.initTargetTable(tableName)
    }

    fun where(block: WhereClauseBuilder.() -> Unit) {
        val whereClauseBuilder = WhereClauseBuilder().apply(block)
        val whereClauses = whereClauseBuilder.build()
        query.addWhereConditions(whereClauses)
    }

    internal fun build(): SelectQuery = query
}
