package dbms.table

internal class Table {
    var name: String = ""
        private set
    private var attributes: MutableMap<String, SupportedType> = mutableMapOf()
    private val records: MutableList<Map<String, Any>> = mutableListOf()

    internal fun currentRecords(): List<Map<String, Any>> = records.toList()

    internal fun currentAttributes(): Map<String, SupportedType> = attributes.toMap()

    internal fun changeName(newName: String) {
        name = newName
    }

    internal fun initializeAttributes(attributeItems: List<Pair<String, SupportedType>>) {
        check(attributes.isEmpty()) { "Attribute list cannot be reinitialized." }
        require(attributeItems.isNotEmpty()) { "Attribute list cannot be empty." }
        attributes = attributeItems.toMap().toMutableMap()
    }

    // 레코드 삽입
    internal fun insertRecords(records: Array<out List<Any>>) {
        if (attributes.isEmpty()) {
            initializeAttributesFromRecords(records)
        }

        val newRecords = records.map { record ->
            validateRecord(record)
            createRecordMap(record)
        }

        this.records.addAll(newRecords)
    }

    // 필드 자동 초기화 (속성이 비어 있을 때)
    private fun initializeAttributesFromRecords(records: Array<out List<Any>>) {
        require(records.isNotEmpty()) { "Cannot infer attributes from empty records." }
        val inferredAttributes = mutableMapOf<String, SupportedType>()
        val sampleRecord = records.first()

        sampleRecord.forEachIndexed { index, fieldValue ->
            val fieldType = when (fieldValue) {
                is String -> SupportedType.STRING
                is Int -> SupportedType.INTEGER
                else -> throw IllegalArgumentException("Unsupported type for field value.")
            }
            inferredAttributes["attribute_$index"] = fieldType
        }

        attributes = inferredAttributes
    }

    private fun validateRecord(record: List<Any>) {
        require(record.size == attributes.size) { "Record size (${record.size}) does not match attributes size (${attributes.size})." }

        record.forEachIndexed { index, fieldValue ->
            val fieldType = attributes.values.elementAt(index)
            when (fieldType) {
                SupportedType.STRING -> require(fieldValue is String) { "Field at index $index must be a String." }
                SupportedType.INTEGER -> require(fieldValue is Int) { "Field at index $index must be an Integer." }
            }
        }
    }

    private fun createRecordMap(record: List<Any>): Map<String, Any> {
        return attributes.keys.zip(record).toMap()
    }
}
