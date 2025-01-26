package dbms.query

internal class SelectQuery {
    private val selectedColumns = mutableListOf<String>()
    private var targetTable: String? = null
    private val whereConditions = mutableListOf<WhereClause>()

    internal fun initTargetTable(tableName: String) {
        targetTable = tableName
    }

    internal fun addColumns(columns: Array<out String>) {
        selectedColumns.addAll(columns.distinct())
    }

    internal fun addWhereConditions(condition: List<WhereClause>) {
        whereConditions.addAll(condition)
    }

    internal fun selectedRecords(records: List<Map<String, Any>>): List<Map<String, Any>> {
        return records.filter { record ->
            predicateRecord(record)
        }.map { row ->
            row.filterKeys { it in columns() }
        }
    }

    internal fun table(): String {
        val currentTargetTable = targetTable
        check(currentTargetTable != null) { "Table name for query is not initialized" }
        return currentTargetTable
    }

    private fun columns(): List<String> {
        check(selectedColumns.isNotEmpty()) { "Selected columns for query are not set" }
        return selectedColumns.toList()
    }

    private fun predicateRecord(
        record: Map<String, Any>,
    ) = whereConditions.all { condition ->
        when (condition.operator) {
            Operator.EQUALS -> record[condition.attributeName] == condition.value
            Operator.LIKE -> {
                val fieldValue = record[condition.attributeName]
                if (fieldValue !is String || condition.value !is String) false else condition.value in fieldValue
            }
        }
    }
}

