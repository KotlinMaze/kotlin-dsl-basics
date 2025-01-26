package dbms.database

import dbms.query.SelectQueryBuilder
import dbms.table.Table
import dbms.table.TableBuilder

internal object DatabaseManager {
    private val tables = mutableMapOf<String, Table>()

    internal fun createTable(block: TableBuilder.() -> Unit) {
        val queryBuilder = TableBuilder()
        queryBuilder.block()

        val createdTable = queryBuilder.table()
        addTable(createdTable)
    }

    internal fun selectRecords(block: SelectQueryBuilder.() -> Unit): List<Map<String, Any>> {
        val queryBuilder = SelectQueryBuilder()
        queryBuilder.block()

        val query = queryBuilder.build()
        val targetTable = findTable(query.table())
        checkNotNull(targetTable) { "Table not found" }

        val result = query.selectedRecords(targetTable.currentRecords())
        return result
    }

    private fun addTable(table: Table) {
        tables[table.name] = table
    }

    private fun findTable(tableName: String): Table? = tables[tableName]

    private fun dropTable(tableName: String) {
        tables.remove(tableName)
    }
}

