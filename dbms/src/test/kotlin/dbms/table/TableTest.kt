package dbms.table

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class TableTest {
    @Test
    fun `테이블 명을 설정하면 테이블 명을 알아낼 수 있다`() {
        val table = Table()
        table.changeName("Users")
        assertEquals("Users", table.name)
    }

    @Test
    fun `테이블의 속성들을 정의할 수 있다`() {
        val table = Table()
        val fields = listOf(
            "name" to SupportedType.STRING,
            "age" to SupportedType.INTEGER
        )

        table.initializeAttributes(fields)

        assertEquals(mapOf(
            "name" to SupportedType.STRING,
            "age" to SupportedType.INTEGER
        ), table.currentAttributes())
    }

    @Test
    fun `테이블의 속성명과 자료형은 한 번 설정하면 변경할 수 없다`() {
        val table = Table()
        val fields = listOf("name" to SupportedType.STRING)

        table.initializeAttributes(fields)

        val exception = assertThrows<IllegalStateException> {
            table.initializeAttributes(fields)
        }
        assertEquals("Attribute list cannot be reinitialized.", exception.message)
    }

    @Test
    fun `테이블의 속성은 적어도 하나 이상이어야 한다`() {
        val table = Table()

        val exception = assertThrows<IllegalArgumentException> {
            table.initializeAttributes(emptyList())
        }
        assertEquals("Attribute list cannot be empty.", exception.message)
    }

    @Test
    fun `속성을 초기화하면 테이블에 속성의 순서에 맞는 필드들로 이뤄진 레코드를 삽입할 수 있다`() {
        val table = Table()
        val attributes = listOf(
            "name" to SupportedType.STRING,
            "age" to SupportedType.INTEGER
        )
        table.initializeAttributes(attributes)

        val records = arrayOf(
            listOf("Alice", 25),
            listOf("Bob", 30)
        )
        table.insertRecords(records)

        val expectedRecords = listOf(
            mapOf("name" to "Alice", "age" to 25),
            mapOf("name" to "Bob", "age" to 30)
        )
        assertEquals(expectedRecords, table.currentRecords())
    }

    @Test
    fun `속성명을 제공하지 않고 레코드만 삽입하면 자동으로 속성명이 지정된다`() {
        val table = Table()

        val records = arrayOf(
            listOf("Alice", 25),
            listOf("Bob", 30)
        )
        table.insertRecords(records)

        val expectedAttributes = mapOf(
            "attribute_0" to SupportedType.STRING,
            "attribute_1" to SupportedType.INTEGER
        )
        assertEquals(expectedAttributes, table.currentAttributes())

        val expectedRecords = listOf(
            mapOf("attribute_0" to "Alice", "attribute_1" to 25),
            mapOf("attribute_0" to "Bob", "attribute_1" to 30)
        )
        assertEquals(expectedRecords, table.currentRecords())
    }

    @Test
    fun `레코드의 크기가 속성의 개수와 맞지 않으면 레코드를 삽입할 수 없다`() {
        val table = Table()
        val attributes = listOf(
            "name" to SupportedType.STRING,
            "age" to SupportedType.INTEGER
        )
        table.initializeAttributes(attributes)

        val records = arrayOf(
            listOf("Alice")
        )

        val exception = assertThrows<IllegalArgumentException> {
            table.insertRecords(records)
        }
        assertEquals("Record size (1) does not match attributes size (2).", exception.message)
    }

    @Test
    fun `레코드에 속성에 부합하지 않는 타입을 가진 필드가 있다면 테이블에 삽입할 수 없다`() {
        val table = Table()
        val attributes = listOf(
            "name" to SupportedType.STRING,
            "age" to SupportedType.INTEGER
        )
        table.initializeAttributes(attributes)

        val records = arrayOf(
            listOf("Alice", "invalidAge")
        )

        val exception = assertThrows<IllegalArgumentException> {
            table.insertRecords(records)
        }
        assertEquals("Field at index 1 must be an Integer.", exception.message)
    }
}
