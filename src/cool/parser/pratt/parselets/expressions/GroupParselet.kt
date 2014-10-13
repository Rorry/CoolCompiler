package cool.parser.pratt.parselets.expressions

import cool.parser.pratt.parselets.SimpleParselet
import cool.parser.pratt.Parser
import cool.tree.AbstractExpression
import cool.lexer.TokenConstants


public class GroupParselet : SimpleParselet {

    public override fun parse(parser: Parser): AbstractExpression {
        parser.nextToken()
        val expr = parser.parseExpression()
        if (parser.token.key != TokenConstants.RPAREN) {
            parser.error(TokenConstants.RPAREN)
        }
        return expr
    }
}