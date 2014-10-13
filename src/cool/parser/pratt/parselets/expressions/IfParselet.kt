package cool.parser.pratt.parselets.expressions

import cool.parser.pratt.parselets.SimpleParselet
import cool.parser.pratt.Parser
import cool.tree.AbstractExpression
import cool.lexer.TokenConstants
import cool.tree.IfExpr


public class IfParselet : SimpleParselet {

    public override fun parse(parser: Parser): AbstractExpression {
        val lineNumber = parser.token.lineNumber
        parser.nextToken()
        val cond = parser.parseExpression()
        parser.accept(TokenConstants.THEN)
        val thenExpr = parser.parseExpression()
        parser.accept(TokenConstants.ELSE)
        val elseExpr = parser.parseExpression()
        if (parser.token.key != TokenConstants.FI) {
            parser.error(TokenConstants.FI)
        }
        return IfExpr(lineNumber, cond, thenExpr, elseExpr)
    }

}