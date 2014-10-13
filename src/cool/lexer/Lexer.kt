package cool.lexer

import java.io.Reader


class Lexer(reader: Reader) {
    val input = reader.buffered()
    val EOF = -1

    var pos = -1
    var lineNumber = 1

    private fun lookAhead(): Char {
        input.mark(1)
        var ch = input.read()
        input.reset()
        return ch.toChar()
    }

    private fun readNext(): Char {
        return input.read().toChar()
    }

    public fun nextToken(): Token {
        var ch: Char = readNext().toChar()

        if (ch == EOF.toChar()) return Token(TokenConstants.EOF, lineNumber = lineNumber)

        while (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n') {
            if (ch == '\n') {
                lineNumber += 1
            }
            ch = readNext()
        }

        when (ch) {
            ';' -> return Token(TokenConstants.SEMI, "<;>", lineNumber)
            ',' -> return Token(TokenConstants.COMMA, "<,>", lineNumber)
            ':' -> return Token(TokenConstants.COLON, "<:>", lineNumber)
            '~' -> return Token(TokenConstants.NEG, "<`>", lineNumber)
            '.' -> return Token(TokenConstants.DOT, "<.>", lineNumber)
            '@' -> return Token(TokenConstants.AT, "<@>", lineNumber)

            '(' -> return Token(TokenConstants.LPAREN, "<(>", lineNumber)
            ')' -> return Token(TokenConstants.RPAREN, "<)>", lineNumber)
            '{' -> return Token(TokenConstants.LBRACE, "<{>", lineNumber)
            '}' -> return Token(TokenConstants.RBRACE, "<}>", lineNumber)

            '+' -> return Token(TokenConstants.PLUS, "<+>", lineNumber)
            '-' -> return Token(TokenConstants.MINUS, "<->", lineNumber)
            '*' -> return Token(TokenConstants.MULT, "<*>", lineNumber)
            '/' -> return Token(TokenConstants.DIV, "</>", lineNumber)

            '=' ->  {
                ch = lookAhead()
                when (ch) {
                    '>' -> {
                        readNext()
                        return Token(TokenConstants.DARROW, "<=>>", lineNumber)
                    }
                    else -> return Token(TokenConstants.EQ, "<=>", lineNumber)
                }
            }
            '<' -> {
                ch = lookAhead()
                when (ch) {
                    '-' -> {
                        readNext()
                        return Token(TokenConstants.ASSIGN, "<<->", lineNumber)
                    }
                    '=' -> {
                        readNext()
                        return Token(TokenConstants.LE, "<<=>", lineNumber)
                    }
                    else -> return Token(TokenConstants.LT, "<<>", lineNumber)
                }
            }

            '"' -> return matchString()

            in 'a' .. 'z' -> return matchIdent(ch, TokenConstants.OBJECTID)

            in 'A' .. 'Z' -> return matchIdent(ch, TokenConstants.TYPEID)

            in '0' .. '9' -> return matchNumber(ch)

            else -> return Token(TokenConstants.ERROR, "<!ERROR!>", lineNumber)
        }
    }

    private fun matchString(): Token {
        var str = ""
        var next = lookAhead()
        while ((next != EOF.toChar()) && (next != '"')) {
            str += readNext()
            next = lookAhead()
        }
        if (next == '"') {
            readNext()
            return Token(TokenConstants.STR_CONST, str, lineNumber)
        } else {
            return Token(TokenConstants.ERROR, "error in string", lineNumber)
        }
    }

    private fun matchNumber(ch: Char): Token {
        var number = "" + ch
        var next = lookAhead()
        while ((next != EOF.toChar()) && (next in '0' .. '9')) {
            number += readNext()
            next = lookAhead()
        }
        return if (next == EOF.toChar()) Token(TokenConstants.ERROR, "error in number", lineNumber) else Token(TokenConstants.INT_CONST, number.toInt(), lineNumber)
    }

    private fun matchIdent(ch: Char, key: Int): Token {
        var ident = "" + ch
        var next = lookAhead()
        while ((next != EOF.toChar()) && (next in 'a' .. 'z') || (next in 'A' .. 'Z') || (next in '0' .. '9') || (next == '_')) {
            ident += readNext()
            next = lookAhead()
        }
        val token = if (next == EOF.toChar())
                Token(TokenConstants.ERROR, "error in ident", lineNumber)
            else
                if (key == TokenConstants.OBJECTID)
                    matchKeyWord(ident)
                else
                    Token(key, ident, lineNumber)
        return token
    }

    private fun matchKeyWord(word: String): Token {
        when (word) {
            Keywords.CLASS -> return Token(TokenConstants.CLASS, "<class>", lineNumber)
            Keywords.ELSE -> return Token(TokenConstants.ELSE, "<else>", lineNumber)
            Keywords.IF -> return Token(TokenConstants.IF, "<if>", lineNumber)
            Keywords.FI -> return Token(TokenConstants.FI, "<fi>", lineNumber)
            Keywords.IN -> return Token(TokenConstants.IN, "<in>", lineNumber)
            Keywords.INHERITS -> return Token(TokenConstants.INHERITS, "<inherits>", lineNumber)
            Keywords.IS_VOID -> return Token(TokenConstants.ISVOID, "<isVoid>", lineNumber)
            Keywords.LET -> return Token(TokenConstants.LET, "<let>", lineNumber)
            Keywords.LOOP -> return Token(TokenConstants.LOOP, "<loop>", lineNumber)
            Keywords.POOL -> return Token(TokenConstants.POOL, "<pool>", lineNumber)
            Keywords.THEN -> return Token(TokenConstants.THEN, "<then>", lineNumber)
            Keywords.WHILE -> return Token(TokenConstants.WHILE, "<while>", lineNumber)
            Keywords.CASE -> return Token(TokenConstants.CASE, "<case>", lineNumber)
            Keywords.ESAC -> return Token(TokenConstants.ESAC, "<esac>", lineNumber)
            Keywords.NEW -> return Token(TokenConstants.NEW, "<new>", lineNumber)
            Keywords.OF -> return Token(TokenConstants.OF, "<of>", lineNumber)
            Keywords.NOT -> return Token(TokenConstants.NOT, "<not>", lineNumber)

            Keywords.TRUE -> return Token(TokenConstants.BOOL_CONST, true, lineNumber)
            Keywords.FALSE -> return Token(TokenConstants.BOOL_CONST, false, lineNumber)

            else -> return Token(TokenConstants.OBJECTID, word, lineNumber)
        }
    }

}