package cool.parser.pratt.parselets.expressions

import cool.parser.pratt.parselets.SimpleParselet
import cool.parser.pratt.Parser
import cool.tree.AbstractExpression
import cool.tree.StringLiteral


public class StringParselet : SimpleParselet {

    public override fun parse(parser: Parser): AbstractExpression {
        val lineNumber = parser.token.lineNumber
        return StringLiteral(lineNumber, parser.token.obj.toString())
    }
}