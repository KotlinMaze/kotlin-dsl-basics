package dbms.table

class TableBuilder {
    private val table = Table()

    // TODO 1. table 함수 정의 - 테이블 명 설정
    fun table(tableName: String) {
        table.changeName(tableName)
    }

    // TODO 2. attributes 함수 정의 - 테이블 속성 추가
    fun attributes(vararg attributes: Pair<String, String>) {
        table.initializeAttributes(attributes.map { it.first to SupportedType.from(it.second) })
    }

    // TODO 3. values 함수 정의 - 테이블에 레코드 추가
    fun values(vararg records: List<Any>) {
        table.insertRecords(records)
    }

    // TODO 4. x 함수 정의 - 레코드 하나에 들어가는 값들을 하나의 리스트 형태로 변환
    infix fun Any.x(other: Any): List<Any> {
        return when (this) {
            is List<*> -> {
                require(!this.contains(null)) { "Record should not contain null value." }
                val currList = this.filterNotNull()
                currList + other
            }
            else -> listOf(this, other)
        }
    }

    internal fun table(): Table {
        check(table.name.isNotEmpty()) { "Table name should not be empty." }
        check(table.currentAttributes().isNotEmpty()) { "Table fields should be set." }
        return table
    }
}
