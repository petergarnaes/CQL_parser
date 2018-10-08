typealias Ident = String
typealias FunName = String

sealed class Program
data class Prog(val statements: List<Stm>) : Program()

sealed class Stm
data class VarDecl(val identifier: Ident, val expr: Maybe<Expr>) : Stm()

sealed class Expr
data class SubsNumber(val number: Int) : Expr()
data class SubsString(val string: String) : Expr()
data class SubsArray(val array: List<Expr>) : Expr()
object Undefined : Expr()
object TrueConst : Expr()
object FalseConst : Expr()
data class Var(val identifier: Ident) : Expr()
data class Compr(val expr: Expr) : Expr()
data class Assign(val identifier: Ident, val expr: Expr) : Expr()
data class Comma(val lexpr: Expr, val rexpr: Expr) : Expr()

data class ArrayFor(val identifier: Ident, val expr: Expr, val arrayCompr: Maybe<ArrayCompr>)

sealed class ArrayCompr
data class ArrayForCompr(val arrayFor: ArrayFor) : ArrayCompr()
data class ArrayIf(val expr: Expr, val arrayCompr: ArrayCompr) : ArrayCompr()