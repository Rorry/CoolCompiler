package cool.parser.pratt.parselets.expressions

import cool.parser.pratt.parselets.SimpleParselet
import cool.parser.pratt.Parser
import cool.tree.AbstractExpression
import cool.tree.BoolLiteral


public class BoolParselet : SimpleParselet {
    public override fun parse(parser: Parser): AbstractExpression {
        val lineNumber = parser.token.lineNumber
        return BoolLiteral(lineNumber, parser.token.obj as Boolean)
    }
}