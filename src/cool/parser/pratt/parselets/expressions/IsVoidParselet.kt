package cool.parser.pratt.parselets.expressions

import cool.parser.pratt.parselets.SimpleParselet
import cool.parser.pratt.Parser
import cool.tree.AbstractExpression
import cool.tree.IsVoid


public class IsVoidParselet : SimpleParselet {
    public override fun parse(parser: Parser): AbstractExpression {
        val lineNumber = parser.token.lineNumber
        parser.nextToken()
        val expr = parser.parseExpression()
        return IsVoid(lineNumber, expr)
    }
}