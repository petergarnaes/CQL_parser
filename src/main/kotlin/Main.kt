import com.github.h0tk3y.kotlinMonads.Maybe
import com.github.h0tk3y.kotlinMonads.Monad
import com.github.h0tk3y.kotlinMonads.just

fun main(args: Array<String>) {
    System.out.println("Hello, world")
    fun <T, R, M : Monad<M, *>> Monad<M, T>.map(f: (T) -> R) = bind { returns(f(it)) }

    val m = just(3).map { it * 2 } as Maybe
    System.out.println(m)
}