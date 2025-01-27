# Kotlin DSL Basics

## Explanation

### Try using Kotlin DSL

From now on, we will create a simple DBMS tool with `Kotlin`.
In the process, I will introduce various grammars that allow you to write code concisely and intuitively in `Kotlin`.

In this explanation, we will proceed with the practice using the example code.
The explanation will proceed step by step, and it would be good to move to the branch corresponding to each step and
write the code.

It consists of a total of **4 steps**, and this article will explain only up to step2, and you can implement the rest
yourself.
Also, since the code is provided mainly for DSL practice, the detailed logic implementation details have been
implemented in advance and will not be covered here.

### Functional requirements

- [ ] You can manipulate the database using queries.
- [ ] Creating a table
    - [ ] You can set and change the table name.
    - [ ] There are two data types available for table fields
        - [ ] string
        - [ ] int
    - [ ] When table creation is complete, the created table name is printed.
    - [ ] You can initially insert records into the table.
    - [ ] In the case of creation, the methods should be in the order
      of `setting name of the table` -> `setting column names` -> `initializing records(optional)`.
- [ ] You can search for data using `select` query.
    - [ ] You can set the conditions for the data to be searched.
    - [ ] Numbers support an option to determine if they are equal (equals).
    - [ ] The string supports two options to determine whether it is equal to or contains different strings (equals,
      like).
    - [ ] You cannot set conditions that do not match the type of data to be searched.
        - Example) Using the like operation on numbers
    - [ ] Get the query results in list form
    - [ ] When the query is complete, print all the retrieved data to the console.
    - [ ] In the case of `select` query, the desired results should be obtained even if the method order is used
      differently.
        - Example) Calling by the order of `from -> select -> where` should also be possible.

### Final goal

You should be able to properly run the `PersonFinder.kt` code that the user uses and output the execution results to the
console.

```kotlin
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
```

> Table Person created successfully.
>
>Table attributes : {name=STRING, age=INTEGER}
>
>Table records : [{name=Kame, age=28}, {name=Km, age=17}, {name=Kate, age=25}]
>
>Selection result : [{name=Kame, age=28}, {name=Kate, age=25}]

## Project Structure

In the branch `step1`, you can see a project with some features pre-implemented.

The project structure is as follows:

* dbms module: dbms program
    * Developers utilizing this module use the functions `create` and `select` in `QueryFunctions.kt`.
* Main module
    * PersonFinder.kt: Program written using the dbms module

## Step 1 - Implementing the User Interface

dbmsModules > uiPackages >QueryFunctions.kt

```kotlin
fun create(block: TableBuilder.() -> Unit) {
    // // TODO 1. implement create method using the methods of DatabaseManager.kt
}

fun select(block: SelectQueryBuilder.() -> Unit): List<Map<String, Any>> {
    // TODO 2. implement select method using the methods of DatabaseManager.kt 
    return emptyList()
}
```

### If the last parameter of a function is a lambda function, it can be removed from the parentheses.

In Kotlin, if the last parameter of a function is a lambda,
you can pass the lambda outside the parentheses.
The two codes below do the same thing, but the code that moves the last lambda function outside the parentheses is
definitely more readable.

```kotlin
fun doubleNumber(n: Int, operation: (Int) -> Int): Int {
    return operation(n)
}

fun main() {
    val number = 5
    // 내부에서 람다 정의
    // 10
    val result1 = doubleNumber(number, { it * 2 })

    // 외부에서 람다 정의
    // 10
    val result2 = doubleNumber(number) { it * 2 }
}
```

This feature is useful for improving readability when writing code in DSL style. In particular, it is an excellent way
to express dependency relationships between two pieces of codecreate . In the function below, you can see that it is
also useful for expressing inclusion relationships between actions .

Before

```kotlin
create({
    table("Person")
    attributes("name" to "string", "age" to "int")
    values("Kame" x 28, "Km" x 17, "Kate" x 25)
})
```

After

```kotlin
create {
    table("Person")
    attributes("name" to "string", "age" to "int")
    values("Kame" x 28, "Km" x 17, "Kate" x 25)
}
```

### Lambda specifying the receiving object

**`dbms > ui > QueryFunctions.kt`**

```kotlin
fun create(block: TableBuilder.() -> Unit) {
    // TODO 1.
}

fun select(block: SelectQueryBuilder.() -> Unit): List<Map<String, Any>> {
    // TODO 2.
    return emptyList()
}
```

**`dbms > database >DatabaseManager.kt`**

```kotlin
internal object DatabaseManager {
    private val tables = mutableMapOf<String, Table>()

    internal fun createTable(block: TableBuilder.() -> Unit) {
        val queryBuilder = TableBuilder()
        queryBuilder.block()

        val createdTable = queryBuilder.table()
        addTable(createdTable)
    }

    // ...
}
```

Let's implement a user interface by utilizing objects that have already been implemented in `DatabaseManager`.

`DatabaseManager` is the core object for creating and managing tables in the database, providing a central management
point for all operations such as adding tables and retrieving data.
This class internally executes external APIs such as create and select, and is responsible for processing data.

Here we see heterogeneous method types in various places in the parameters, like this:

```kotlin
// block: TableBuilder.() -> Unit
// block: SelectQueryBuilder.() -> Unit
```

It is a form in which an object name is attached in front of a general function type.
To understand this form, you need to understand the `lambda specifying the receiving object`.

By using the `lambda functions specifying the receiving object`, you can specify a specific type as the receiver object
of the lambda function. By specifying a specific type as the receiver object, you can directly use the methods or
properties of the object declared as that type within the lambda block.

> If there is an instance of the receiving type, the lambda function that specifies the receiving object is a function
> that can be used as an instance of that type. This is because by utilizing the lambda function that specifies the
> receiving object, you can ultimately use it in the form of calling an externally defined lambda from within the
> function.

#### When not using a lambda specifying the receiver object

When defining a `configureFile` method, you must define all the necessary parameters. If the number of parameters
increases, readability may decrease.

```kotlin
class FileConfig {
    var filePath: String = ""
    var fileExtension: String = ""
}

fun configureFile(fileConfig: FileConfig, path: String, extension: String) {
    fileConfig.filePath = path
    fileConfig.fileExtension = extension
}

fun main() {
    val config = FileConfig()

    configureFile(config, "C:/Documents", ".txt")

    println("File Path: ${config.filePath}")
    println("File Extension: ${config.fileExtension}")
}
```

#### When using lambda to specify a receiver object

You can set objects directly within a lambda block. By implicitly passing the object as this, you can easily set methods
or properties of the object within the lambda block, which improves readability.

```kotlin
class FileConfig {
    var filePath: String = ""
    var fileExtension: String = ""
}

fun configureFile(configure: FileConfig.() -> Unit): FileConfig {
    val config = FileConfig()
    config.configure()
    return config
}

fun main() {
    val config = configureFile { // this: FileConfig
        filePath = "C:/Documents" // this.filePath = ...
        fileExtension = ".txt" // this.fileExtension = ...
    }

    println("File Path: ${config.filePath}")
    println("File Extension: ${config.fileExtension}")
}
```

This form of lambda allows you to refer to the object through the this keyword when accessing the object's methods or
properties. This lambda function is useful for improving the readability and maintainability of code, and is useful when
implementing DSLs.

Now, let's complete the method in `QueryFunctions.kt`.

Internally, the `block()` function of `DatabaseManager` is used in the form of a lambda specifying the receiving object.

By leveraging this feature, we can easily initialize the settings by creating an instance inside the method and calling
the block() function passed to that instance. (See PersonFinder.kt)

It is now possible to define the table name, properties, and records required to initialize a table without using
explicit parameters.

```kotlin
internal object DatabaseManager {
    private val tables = mutableMapOf<String, Table>()

    internal fun createTable(block: TableBuilder.() -> Unit) {
        val tableBuilder = TableBuilder()
        queryBuilder.block()

        // ...
    }

    internal fun selectRecords(block: SelectQueryBuilder.() -> Unit): List<Map<String, Any>> {
        val queryBuilder = SelectQueryBuilder()
        queryBuilder.block()

        // ...
    }
}
```

Now you can implement `create` and `select` functions in `QueryFunctions.kt` as follows.

```kotlin
fun create(block: TableBuilder.() -> Unit) {
    DatabaseManager.createTable {
        block()
        println("Table ${table().name} created successfully.")
        println("Table attributes : ${table().currentAttributes()}")
        println("Table records : ${table().currentRecords()}")
    }
}

fun select(block: SelectQueryBuilder.() -> Unit): List<Map<String, Any>> {
    return DatabaseManager.selectRecords(block).also { println(it) }
}
```

Using the method in DatabaseManager, we pass the lambda function that specifies the receiver object received from
PersonFinder back to DatabaseManager.

Additionally, according to the requirements, I even wrote code to output the results when the task is completed
successfully.

## Step 2

```kotlin
create { // this: **TableBuilder**
    table("Person")
    attributes("name" to "string", "age" to "int")
    values("Kame" x 28, "Km" x 17, "Kate" x 25)
}
class TableBuilder {
    private val table = Table()

    // TODO 1. table 함수 정의 - 테이블 명 설정

    // TODO 2. attributes 함수 정의 - 테이블 속성 추가

    // TODO 3. values 함수 정의 - 테이블에 레코드 추가

    // TODO 4. x 함수 정의 - 레코드 하나에 들어가는 값들을 하나의 리스트 형태로 변환

    // build()
}
```

By utilizing functions inside the object, you can easily define the table name, attributes, and record values of the Table object.
```kotlin
class TableBuilder {
    private val table = Table()

    // TODO 1. table 
    fun table(tableName: String) {
        table.changeName(tableName)
    }

    // TODO 2. attributes 
    fun attributes(vararg attributes: Pair<String, String>) {
        table.initializeAttributes(attributes.map { it.first to SupportedType.from(it.second) })
    }

    // TODO 3. values
    fun values(vararg records: List<Any>) {
        table.insertRecords(records)
    }

    // ...
}
```

### Extension Function

Next, let's implement a function that creates a list of multiple field values. The return value of this function will be
used as a record in the table. Here, we will try to use other ways to define functions for better readability.

```kotlin
class TableBuilder {
    private val table = Table()

    // ...

    // TODO 4. define `x` function

    internal fun table(): Table {
        check(table.name.isNotEmpty()) { "Table name should not be empty." }
        check(table.currentAttributes().isNotEmpty()) { "Table fields should be set." }
        return table
    }
}
```

In Kotlin, you can define `Extension Functions`. Extension functions provide the flexibility to add new behavior without
modifying the code of an existing class. Extension functions are implemented by defining a function externally that can
be called on instances of a specific class.

```kotlin
fun List<Int>.sumUp(): Int {
    return sum() // this.sum()
}
```

This function is bound to the receiving object and can access the object being called. The `this` keyword can be
omitted, so you can access the public properties or methods of the receiving object without specifying the object. In
the example above, List<Int>the function is used directly inside the function block without declaring a separate
variable.

If you use the extension function form appropriately, you can benefit in many aspects, such as the readability of the
code.

```kotlin
fun List<Int>.sumUp(): Int {
    return this.sum()
}

fun sumUp(nums: List<Int>): Int {
    return this.sum()
}

fun main() {
    val numbers = listOf(1, 2, 3, 4)
    // 1. 
    sumUp(numbers)

    // 2. 
    numbers.sumUp()
}
```

> Note) It should be noted that although extension functions can be called as member methods of a particular class, they
> are not actually added as member methods of the receiving object .

Let's implement `x` function using an extension function.
The function is a function that combines various field values into a single list and returns a list, and the returned
list is used as a record in the table.

```kotlin
// TODO 4. x function
fun Any.x(other: Any): List<Any> {
    return when (this) {
        is List<*> -> {
            require(!this.contains(null)) { "Record should not contain null value." }
            val currList = this.filterNotNull()
            currList + other
        }
        else -> listOf(this, other)
    }
}
```

The function implemented this way can be used as follows:

```kotlin
create {
    table("Person")
    attributes("name" to "string", "age" to "int")
    values("Kame".x(28), "Km".x(17), "Kate".x(25))
}
```

### infix functions
We have created a list of field values of a record using the extension function.
If you only use the extension function, you can use it as follows when forming a record.

```kotlin
values("Kame".x(28), "Km".x(17), "Kate.x(25))
```
But the user wants to use it like this:

```kotlin
values("Kame" x 28, "Km" x 17, "Kate" x 25)
```
Compared to the above method, this method definitely looks more intuitive because the field values are linked with the symbol `x`.
In Kotlin, by utilizing infix functions, you can insert the function name between two variables (receiver object and parameter).

In fact, in the previous implementation, we were already using infix functions, when creating a Pair object while defining its properties.

```kotlin
infix fun <A, B> A.to(that: B): Pair<A, B> = Pair(this, that)
```

Kotlin provides an infix function called `to`, which allows us to easily bundle two values into a Pair object.

```kotlin
attributes("name" to "string", "age" to "int")
```
Defining an infix function is very simple. Just add a keyword `infix` in front of the extension function you have already defined. 
The unnecessary parentheses and `.` for function call are gone, resulting in a more readable code.

```kotlin
infix fun Any.x(other: Any): List<Any> {
    // ...
}
```
```kotlin
values("Kame" x 28, "Km" x 17, "Kate" x 25)
```

>Note) Two things to keep in mind when using the infix function
>1. The infix keyword can only be used with `extension functions`.
>2. Only one parameter can be defined.

Now we have completed the query to create the table.
PersonFinder If we uncomment the createfunction, we can see the following result in the console.

>Table Person created successfully.
> 
>Table attributes : {name=STRING, age=INTEGER}
> 
>Table records : [{name=Kame, age=28}, {name=Km, age=17}, {name=Kate, age=25}]

## Step 3 ~ 4

Select query can also be created in the same way as DSL. Since it can be implemented in a similar way, I will not cover
it in this article. The example answer is in example-answerthe branch of the repository . I recommend that you use it as
an opportunity to get familiar with DSL by implementing it yourself.

### Step-by-step implementation requirements

* Step 3
  Implement WhereClauseBuilder.
* Step 4
  Implement SelectQueryBuilder.
  PersonFinderWhen you uncomment this, all your code should work normally.

## Reference

[Velog Link](https://velog.io/@kmkim2689/kotlin-dsl-basics)

