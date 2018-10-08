import java.util.*

// Basic combinators

fun <T> zero() : Parser<T> = Parser { _ -> emptyList<Pair<T,String>>()}

fun parseSingleChar() : Parser<Char> = Parser {
    when {
        it.isEmpty() -> emptyList()
        // We have non-empty string
        else -> listOf(Pair(it.first(),it.substring(1)))
    }
}

// Choice combinators

fun sat(func: (Char) -> Boolean) : Parser<Char> = doReturning(ParserReturn) {
    val c = bind(parseSingleChar())
    if(func(c)) returns(c) else zero()
} as Parser

fun char(char: Char) : Parser<Char> = sat { char == it }

// Recursion combinators

fun string(string: String) : Parser<String> = doReturning(ParserReturn) {
    when {
        string.isEmpty() -> returns("")
        else -> {
            val c = bind(char(string.first()))
            val cs = bind(string(string.substring(1)))
            returns(c + cs)
        }
    }
} as Parser

fun<A> choice(parser1: Parser<A>, parser2: Parser<A>) : Parser<A> = Parser { cs ->
    val res = (parser1 + parser2).parse(cs)
    when {
        res.isEmpty() -> emptyList()
        else -> listOf(res.first())
    }
}

fun<A> many(parser: Parser<A>) : Parser<List<A>> = doReturning(ParserReturn) {
    choice(many1(parser),returns(listOf<A>()) as Parser)
} as Parser

fun<A> many1(parser: Parser<A>) : Parser<List<A>> = doReturning(ParserReturn) {
    val a = bind(parser)
    val ass = bind(many(parser))
    // TODO prettier way???
    var m = mutableListOf(a)
    m.addAll(ass)
    returns(m.toList())
} as Parser

// TODO this still does not work
fun<A,B> sepby(parser1: Parser<A>, parser2: Parser<B>) : Parser<List<A>> = doReturning(ParserReturn) {
    val c = choice(sepby1(parser1, parser2), returns(emptyList<A>()) as Parser)
    c
} as Parser

fun<A,B> sepbyTake2(parser1: Parser<A>, parser2: Parser<B>) : Parser<List<A>> = choice(sepby1(parser1, parser2), zero())

fun<A,B> sepby1(parser: Parser<A>, separator: Parser<B>) : Parser<List<A>> = doReturning(ParserReturn) {
    val a = bind(parser)
    val ass = bind(many(doReturning(ParserReturn) {
        bind(separator)
        val b = bind(parser)
        returns(b)
    } as Parser))
    // TODO prettier way???
    var m = mutableListOf(a)
    m.addAll(ass)
    returns(m.toList())
} as Parser

fun<A> chainl(parser: Parser<A>, parserFun: Parser<(A, A) -> A>, element: A) : Parser<A> = doReturning(ParserReturn) {
    choice(chainl1(parser,parserFun), returns(element) as Parser)
} as Parser

fun<A> chainl1(parser: Parser<A>, parserFun: Parser<(A, A) -> A>) : Parser<A> = doReturning(ParserReturn) {
    val a = bind(parser)
    fun rest(a: A) : Parser<A> = choice(doReturning(ParserReturn) {
        val f = bind(parserFun)
        val b = bind(parser)
        val res = bind(rest(f(a,b)))
        returns(res)
    } as Parser, returns(a) as Parser)
    val res = bind(rest(a))
    returns(res)
} as Parser

// Lexical combinators

fun space() : Parser<String> = doReturning(ParserReturn){
    val l = bind(many(sat { it.isWhitespace() }))
    // TODO kinda ugly, can it be better? Should we drop String type and only work on char array?
    returns(Arrays.toString(l.toCharArray()))
} as Parser

fun<A> token(parser: Parser<A>) : Parser<A> = doReturning(ParserReturn) {
    val a = bind(parser)
    bind(space())
    returns(a)
} as Parser

fun symb(s: String) : Parser<String> = token(string(s))

fun<A> apply(parser: Parser<A>, s: String) : List<Pair<A,String>> = parser.parse(s)