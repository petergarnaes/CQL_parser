fun main(args: Array<String>) {
    /*System.out.println("Hello, world")
    fun <T, R, M : Monad<M, *>> Monad<M, T>.map(f: (T) -> R) = bind { returns(f(it)) }

    val m = just(3).map { it * 2 } as Maybe
    System.out.println(m)
    val answer = when (m) {
        is Just -> "Just"
        is None -> "None!"
    }
    System.out.println(answer)
    val n = doReturning(MonadListReturn) {
        val x = bind(monadListOf(1, 2, 3))
        val y = bind(monadListOf(x, x + 1))
        monadListOf(y, x * y)
    }

    System.out.println(n)
    var test = "12345"
    System.out.println(test.substring(0))
    System.out.println(test.substring(1))
    System.out.println(test.substring(2))*/
    //val test = apply(space(),"        ")
    //System.out.println("resss: "+test)
    //val zeroAndDigit = zero<Int>() + digit()
    //val digit = digit()
    //System.out.println(apply(zeroAndDigit,"12"))
    //System.out.println(apply(digit,"12"))
    val tezt = apply(space(),"")
    System.out.println(tezt)
    val test2 = apply(sepby(digit(),space()),"12345 234 34 1   ")
    System.out.println("results are in: "+test2)
    val tezzt = apply(sepby1(digit(),space()),"1")
    System.out.println("results are in: "+tezzt)
    //val test3 = apply(many(digit()),"1234")
    //System.out.println("results are in: "+test3)
    //val trying = apply(expr(),"1 - 2 * 3 + 4 ")
    //System.out.println(trying)
}