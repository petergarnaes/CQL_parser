class Parser<T>(val parse: (String) -> List<Pair<T,String>>) : Monad<Parser<*>, T> {
    override fun <R> bind(f: Binder<Parser<*>, T, R>): Monad<Parser<*>, R> {
        return Parser { cs ->
            val tmp = parse(cs)
            val tmp2 = tmp.map { ps ->
                val parser = f(ParserReturn, ps.first) as Parser
                parser.parse(ps.second)
            }
            tmp2.flatMap { it }
            // TODO more concise???
        }
    }

    operator fun plus(parser: Parser<T>): Parser<T> = Parser { cs ->
        System.out.println("left: "+parse(cs))
        System.out.println("right: "+parser.parse(cs))
        listOf(parse(cs), parser.parse(cs)).flatMap { it }
    }
}

object ParserReturn : Return<Parser<*>> {
    override fun <T> returns(t: T) = Parser { cs ->
        listOf(Pair(t,cs))
    }
}
