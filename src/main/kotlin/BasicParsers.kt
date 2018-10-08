import java.util.*

// Synonyms

fun<A> doParse(c: suspend DoController<Parser<*>>.() -> Monad<Parser<*>,A>): Parser<A> =
        doReturning(ParserReturn,c) as Parser

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

fun sat(func: (Char) -> Boolean) : Parser<Char> = doParse {
    val c = bind(parseSingleChar())
    if(func(c)) returns(c) else zero()
}

fun char(char: Char) : Parser<Char> = sat { char == it }

// Recursion combinators

fun string(string: String) : Parser<String> = doParse {
    when {
        string.isEmpty() -> returns("")
        else -> {
            val c = bind(char(string.first()))
            val cs = bind(string(string.substring(1)))
            returns(c + cs)
        }
    }
}

fun<A> choice(parser1: Parser<A>, parser2: Parser<A>) : Parser<A> = Parser { cs ->
    val res = (parser1 + parser2).parse(cs)
    when {
        res.isEmpty() -> emptyList()
        else -> listOf(res.first())
    }
}

fun<A> many(parser: Parser<A>) : Parser<List<A>> = doParse {
    choice(many1(parser),returns(listOf<A>()) as Parser)
}

fun<A> many1(parser: Parser<A>) : Parser<List<A>> = doParse {
    val a = bind(parser)
    val ass = bind(many(parser))
    // TODO prettier way???
    val m = mutableListOf(a)
    m.addAll(ass)
    returns(m.toList())
}

// TODO this still does not work
fun<A,B> sepby(parser1: Parser<A>, parser2: Parser<B>) : Parser<List<A>> = doParse {
    choice(sepby1(parser1, parser2), returns(emptyList<A>()) as Parser)
}

fun<A,B> sepby1(parser: Parser<A>, separator: Parser<B>) : Parser<List<A>> = doParse {
    val a = bind(parser)
    val ass = bind(many(doParse {
        bind(separator)
        returns(bind(parser))
    }))
    // TODO prettier way???
    val m = mutableListOf(a)
    m.addAll(ass)
    returns(m.toList())
}

fun<A> chainl(parser: Parser<A>, parserFun: Parser<(A, A) -> A>, element: A) : Parser<A> = doParse {
    choice(chainl1(parser,parserFun), returns(element) as Parser)
}

fun<A> chainl1(parser: Parser<A>, parserFun: Parser<(A, A) -> A>) : Parser<A> = doParse {
    val a = bind(parser)
    fun rest(a: A) : Parser<A> = choice(doParse {
        val f = bind(parserFun)
        val b = bind(parser)
        val res = bind(rest(f(a, b)))
        returns(res)
    }, returns(a) as Parser)
    returns(bind(rest(a)))
}

// Lexical combinators

fun space() : Parser<String> = doParse {
    val l = bind(many(sat { it.isWhitespace() }))
    // TODO kinda ugly, can it be better? Should we drop String type and only work on char array?
    returns(Arrays.toString(l.toCharArray()))
}

fun<A> token(parser: Parser<A>) : Parser<A> = doParse {
    val a = bind(parser)
    bind(space())
    returns(a)
}

fun symb(s: String) : Parser<String> = token(string(s))

fun<A> apply(parser: Parser<A>, s: String) : List<Pair<A,String>> = parser.parse(s)