package cool.parser.pratt.parselets

import cool.parser.pratt.Parser
import cool.tree.AbstractExpression


public trait SimpleParselet : Parselet {
    public fun parse(parser: Parser): AbstractExpression
}