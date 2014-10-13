package cool.util

import cool.lexer.TokenConstants


object Errors {
    public fun printError(lineNumber: Int, message: String) {
        val message = "ERROR: line: " + lineNumber + " " + message
        println(message)
    }

    public fun printError(lineNumber: Int, key: Int) {
        val message = "ERROR: line: " + lineNumber + "  Expected:" + keyToString(key)
        println(message)
    }

    private fun keyToString(key: Int): String {
        return when (key) {
            TokenConstants.ASSIGN -> " \"<-\" "
            TokenConstants.AT -> " \"@\" "
            TokenConstants.BOOL_CONST -> " BOOL_CONST "
            TokenConstants.CASE -> " \"case\" "
            TokenConstants.CLASS -> " \"class\" "
            TokenConstants.COLON -> " \":\" "
            TokenConstants.COMMA -> " \",\" "
            TokenConstants.DARROW -> " \"->\" "
            TokenConstants.DIV -> " \"/\" "
            TokenConstants.DOT -> " \".\" "
            TokenConstants.ELSE -> " \"else\" "
            TokenConstants.EQ -> " \"=\" "
            TokenConstants.ESAC -> " \"esac\" "
            TokenConstants.FI -> " \"fi\" "
            TokenConstants.IN -> " \"in\" "
            TokenConstants.INT_CONST -> " INT_CONST "
            TokenConstants.LBRACE -> " \"{\" "
            TokenConstants.LPAREN -> " \"(\" "
            TokenConstants.LT -> " \"<=\" "
            TokenConstants.MINUS -> " \"-\" "
            TokenConstants.OBJECTID -> " OBJECTID "
            TokenConstants.OF -> " \"of\" "
            TokenConstants.RBRACE -> " \"}\" "
            TokenConstants.RPAREN -> " \")\" "
            TokenConstants.SEMI -> " \";\" "
            TokenConstants.TYPEID -> " TYPEID "
            else -> " Unenxpected symbol "
        }
    }
}