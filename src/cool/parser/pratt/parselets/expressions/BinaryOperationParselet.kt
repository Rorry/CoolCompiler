package cool.parser.pratt.parselets.expressions

import cool.parser.pratt.parselets.BinaryParselet
import cool.parser.pratt.Parser
import cool.tree.AbstractExpression
import cool.parser.pratt.Precedence
import cool.tree.BinaryExpr


public class BinaryOperationParselet: BinaryParselet {
    public override fun parse(parser: Parser, left: AbstractExpression): AbstractExpression {
        val lineNumber = parser.token.lineNumber
        val op = parser.token.key
        parser.nextToken()
        val right = parser.parseExpression(Precedence.map(op))
        return BinaryExpr(lineNumber, op, left, right)
    }
}