package dbms.ui

import dbms.database.DatabaseManager
import dbms.query.SelectQueryBuilder
import dbms.table.TableBuilder

fun create(block: TableBuilder.() -> Unit) {
    // TODO 1. DatabaseManager의 메서드를 활용하여 create 메서드 완성
}

fun select(block: SelectQueryBuilder.() -> Unit): List<Map<String, Any>> {
    // TODO 2. DatabaseManager의 메서드를 활용하여 select 메서드 완성
    return emptyList()
}
