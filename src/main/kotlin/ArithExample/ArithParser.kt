fun expr() : Parser<Int> = chainl1(term(), addop())

fun term() : Parser<Int> = chainl1(factor(), mulop())

fun factor() : Parser<Int> = choice(digit(), doReturning(ParserReturn) {
    bind(symb("("))
    val n = bind(expr())
    bind(symb(")"))
    returns(n)
} as Parser)

// TODO maybe conversion from char to number is wrong?
fun digit() : Parser<Int> = doReturning(ParserReturn) {
    val x = bind(token(sat { it.isDigit() }))
    returns(x.toInt())
} as Parser

fun addop() : Parser<(Int,Int) -> Int> = choice(
        doReturning(ParserReturn) {
            bind(symb("+"))
            returns({ a: Int, b: Int -> a + b })
        } as Parser,
        doReturning(ParserReturn) {
            bind(symb("-"))
            returns({ a: Int, b: Int -> a - b })
        } as Parser
)

fun mulop() : Parser<(Int,Int) -> Int> = choice(
        doReturning(ParserReturn) {
            bind(symb("*"))
            returns({ a: Int, b: Int -> a * b })
        } as Parser,
        doReturning(ParserReturn) {
            bind(symb("/"))
            returns({ a: Int, b: Int -> a / b })
        } as Parser
)