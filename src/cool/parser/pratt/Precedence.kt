package cool.parser.pratt

import cool.lexer.TokenConstants



object Precedence {
    public val ASSIGN: Int = 1
    public val EQ: Int = 2
    public val LE: Int = 2
    public val LT: Int = 2
    public val PLUS: Int = 3
    public val MINUS: Int = 3
    public val MULT: Int = 4
    public val DIV: Int = 4
    public val CALL: Int = 5
    public val AT: Int = 6
    public val DOT: Int = 6

    public fun map(key: Int): Int {
        return when (key) {
            TokenConstants.ASSIGN -> ASSIGN
            TokenConstants.EQ,
            TokenConstants.LE,
            TokenConstants.LT -> EQ
            TokenConstants.PLUS,
            TokenConstants.MINUS -> PLUS
            TokenConstants.MULT,
            TokenConstants.DIV -> MULT
            TokenConstants.LPAREN -> CALL
            TokenConstants.AT -> AT
            TokenConstants.DOT -> DOT
            else -> -1
        }
    }
}