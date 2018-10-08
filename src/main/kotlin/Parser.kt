class Parser<T>(val parse: (String) -> List<Pair<T,String>>) : Monad<Parser<*>, T> {
    override fun <R> bind(f: Binder<Parser<*>, T, R>): Monad<Parser<*>, R> {
        return Parser { cs ->
            parse(cs).map {
                (f(ParserReturn, it.first) as Parser).parse(it.second)
            }.flatMap { it }
        }
    }

    operator fun plus(parser: Parser<T>): Parser<T> = Parser { cs ->
        listOf(parse(cs), parser.parse(cs)).flatMap { it }
    }
}

object ParserReturn : Return<Parser<*>> {
    override fun <T> returns(t: T) = Parser { cs ->
        listOf(Pair(t,cs))
    }
}
