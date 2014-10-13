package cool.parser.pratt.parselets.expressions

import cool.parser.pratt.parselets.SimpleParselet
import cool.parser.pratt.Parser
import cool.tree.AbstractExpression
import cool.tree.IntLiteral


public class IntParselet : SimpleParselet {

    public override fun parse(parser: Parser): AbstractExpression {
        val lineNumber = parser.token.lineNumber
        return IntLiteral(lineNumber, parser.token.obj as Int)
    }
}