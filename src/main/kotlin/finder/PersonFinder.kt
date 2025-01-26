package finder

import dbms.ui.create

fun main() {
    create {
        table("Person")
        attributes("name" to "string", "age" to "int")
        values("Kame" x 28, "Km" x 17, "Kate" x 25)
    }

//    TODO : 아래 코드 주석을 해제했을 때, 올바르게 동작하도록 구현
//    select {
//        columns("name", "age")
//        from("Person")
//        where {
//            "name" like "Ka"
//        }
//    }
}
