package cool.parser.pratt

import cool.tree.TreeNode
import cool.lexer.Token
import cool.lexer.Lexer
import cool.util.Errors
import cool.util.Name
import cool.util.NameTable
import cool.tree.AbstractClass
import cool.tree.AbstractFeature
import cool.tree.AbstractExpression
import cool.lexer.TokenConstants



trait Parser {
    public var token: Token

    public fun nextToken(): Token {
        token = lexer().nextToken()
        return token
    }

    public fun accept(key: Int): Token {
        if (token.key == key) {
            nextToken()
        } else {
            Errors.printError(token.lineNumber, key)
        }
        return token
    }

    /* ?????????????? */
    public fun check(key: Int): Token {
        if (token.key != key) {
            Errors.printError(token.lineNumber, key)
        }
        return token
    }

    public fun getName(key: Int): Name {
        return if (token.key == key) {
            val obj = token.obj
            nextToken()
            NameTable.addString(obj.toString())
        } else
            NameTable.emptyName()
    }

    public fun skip() {
        while (token.key != TokenConstants.SEMI
            && token.key != TokenConstants.RBRACE
            && token.key != TokenConstants.EOF) {
            nextToken()
        }
    }

    public fun error(key: Int) {
        Errors.printError(token.lineNumber, key)
    }

    public fun lexer(): Lexer

    public fun parse(): TreeNode
    public fun parseClazz(): AbstractClass
    public fun parseFeature(): AbstractFeature
    public fun parseExpression(): AbstractExpression
    public fun parseExpression(precedence: Int): AbstractExpression
}