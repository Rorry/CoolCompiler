package cool.parser.pratt.parselets

import cool.parser.pratt.Parser
import cool.tree.AbstractExpression


public trait BinaryParselet : Parselet {
    public fun parse(parser: Parser, left: AbstractExpression): AbstractExpression
}