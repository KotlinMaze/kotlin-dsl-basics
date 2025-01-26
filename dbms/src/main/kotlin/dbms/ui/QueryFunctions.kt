package dbms.ui

import dbms.database.DatabaseManager
import dbms.query.SelectQueryBuilder
import dbms.table.TableBuilder

fun create(block: TableBuilder.() -> Unit) {
    DatabaseManager.createTable {
        block()
        println("Table ${table().name} created successfully.")
        println("Table attributes : ${table().currentAttributes()}")
        println("Table records : ${table().currentRecords()}")
    }
}

fun select(block: SelectQueryBuilder.() -> Unit): List<Map<String, Any>> {
    return DatabaseManager.selectRecords(block).also { println("Selection result : $it") }
}
