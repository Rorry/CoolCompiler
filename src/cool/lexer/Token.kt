package cool.lexer



data class Token(val key: Int, val obj: Any? = null, val lineNumber: Int, val pos: Int = -1) {
    public override fun toString(): String {
        return "[" + key + ", " + obj + "]"
    }
}