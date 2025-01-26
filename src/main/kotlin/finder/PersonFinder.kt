package finder

import dbms.ui.create
import dbms.ui.select

fun main() {
    create {
        table("Person")
        attributes("name" to "string", "age" to "int")
        values("Kame" x 28, "Km" x 17, "Kate" x 25)
    }

    select {
        columns("name", "age")
        from("Person")
        where {
            "name" like "Ka"
        }
    }
}
