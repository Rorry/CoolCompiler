package cool.parser.pratt.parselets.expressions

import cool.parser.pratt.parselets.SimpleParselet
import cool.parser.pratt.Parser
import cool.tree.AbstractExpression
import cool.lexer.TokenConstants
import cool.tree.WhileExpr


public class WhileParslet : SimpleParselet {
    public override fun parse(parser: Parser): AbstractExpression {
        val lineNumber = parser.token.lineNumber
        parser.nextToken()
        val cond = parser.parseExpression()
        parser.accept(TokenConstants.LOOP)
        val body = parser.parseExpression()
        if (parser.token.key != TokenConstants.POOL) {
            parser.error(TokenConstants.POOL)
        }
        return WhileExpr(lineNumber, cond, body)
    }
}