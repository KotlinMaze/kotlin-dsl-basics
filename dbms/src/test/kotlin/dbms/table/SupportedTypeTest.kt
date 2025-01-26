package dbms.table

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows

class SupportedTypeTest {
    @Test
    fun `지원하는 자료형을 문자열 형태로 올바르게 입력하면 알맞은 자료형 타입을 반환한다`() {
        // when
        val type1 = SupportedType.from("string")
        val type2 = SupportedType.from("int")

        // then
        assertAll(
            { assertEquals(SupportedType.STRING, type1) },
            { assertEquals(SupportedType.INTEGER, type2) },
        )
    }

    @Test
    fun `지원하지 않는 자료형을 입력하면 자료형 타입을 반환할 수 없다`() {
        // when
        val exception = assertThrows<IllegalArgumentException> {
            SupportedType.from("byte")
        }

        // then
        assertEquals("Unsupported Type", exception.message)
    }
}
