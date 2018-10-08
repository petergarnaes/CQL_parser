val foo by lazy { "abc" }

var <T> List<T>.bar get() = foo; set(value) {
    println(foo)
}