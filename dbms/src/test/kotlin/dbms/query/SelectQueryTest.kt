package dbms.query

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class SelectQueryTest {
    private lateinit var query: SelectQuery

    @BeforeEach
    fun setUp() {
        query = SelectQuery()
    }

    @Test
    fun `테이블 이름을 초기화할 수 있다`() {
        // when
        query.initTargetTable("users")

        // then
        assertEquals("users", query.table())
    }

    @Test
    fun `테이블 이름을 변경할 수 있다`() {
        // when
        query.initTargetTable("users")
        query.initTargetTable("person")

        // then
        assertEquals("person", query.table())
    }

    @Test
    fun `테이블 명이 초기화되지 않은 상태에서 특정 레코드를 가져오는 쿼리의 테이블 명을 조회할 수 없다`() {
        // when & then
        val exception = assertThrows<IllegalStateException> {
            query.table()
        }
        assertEquals("Table name for query is not initialized", exception.message)
    }

    @Test
    fun `EQUALS 연산을 사용하여 정확히 같은 필드값을 가진 레코드 목록을 조회할 수 있다`() {
        // given
        val records = listOf(
            mapOf("id" to 1, "name" to "Alice", "age" to 25),
            mapOf("id" to 2, "name" to "Bob", "age" to 30),
            mapOf("id" to 3, "name" to "Alice", "age" to 28)
        )

        // when
        query.initTargetTable("users")
        query.addColumns(arrayOf("id", "name"))
        query.addWhereConditions(
            listOf(
                WhereClause("name", Operator.EQUALS, "Alice"),
                WhereClause("age", Operator.EQUALS, 25)
            )
        )
        val filteredRecords = query.selectedRecords(records)

        // then
        assertEquals(
            listOf(
                mapOf("id" to 1, "name" to "Alice")
            ),
            filteredRecords
        )
    }

    @Test
    fun `조회 시 대상 칼럼을 적어도 하나 이상 명시하여야 한다`() {
        // given
        val records = listOf(
            mapOf("id" to 1, "name" to "Alice", "age" to 25)
        )

        // when & then
        query.initTargetTable("users")
        query.addWhereConditions(
            listOf(
                WhereClause("name", Operator.EQUALS, "Alice")
            )
        )

        val exception = assertThrows<IllegalStateException> {
            query.selectedRecords(records)
        }
        assertEquals("Selected columns for query are not set", exception.message)
    }

    @Test
    fun `LIKE 연산자를 활용하여 특정 문자열이 포함된 필드를 가진 레코드들의 목록을 조회할 수 있다`() {
        query.addColumns(arrayOf("id", "name"))
        query.initTargetTable("users")
        query.addWhereConditions(
            listOf(
                WhereClause("name", Operator.LIKE, "Ali")
            )
        )

        val records = listOf(
            mapOf("id" to 1, "name" to "Alice", "age" to 25),
            mapOf("id" to 2, "name" to "Bob", "age" to 30),
            mapOf("id" to 3, "name" to "Ali", "age" to 28)
        )

        val filteredRecords = query.selectedRecords(records)

        assertEquals(
            listOf(
                mapOf("id" to 1, "name" to "Alice"),
                mapOf("id" to 3, "name" to "Ali")
            ),
            filteredRecords
        )
    }

    @Test
    fun `LIKE 연산자는 적용 가능한 피연산자에만 사용되어야 한다`() {
        val exception = assertThrows<IllegalArgumentException> {
            WhereClause("name", Operator.LIKE, 123)
        }
        assertEquals("Operator 'like' should be utilized with String values.", exception.message)
    }
}
