package dbms.table

internal enum class SupportedType {
    STRING, INTEGER;

    companion object {
        fun from(typeInput: String): SupportedType {
            return when (typeInput) {
                "string" -> STRING
                "int" -> INTEGER
                else -> throw IllegalArgumentException("Unsupported Type")
            }
        }
    }
}
