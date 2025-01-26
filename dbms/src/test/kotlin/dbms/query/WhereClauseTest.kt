package dbms.query

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class WhereClauseTest {
    @Test
    fun `LIKE 연산자를 숫자 피연산자와 함께 사용할 수 없다`() {
        assertThrows<IllegalArgumentException> {
            WhereClause("name", Operator.LIKE, 1)
        }
    }
}
