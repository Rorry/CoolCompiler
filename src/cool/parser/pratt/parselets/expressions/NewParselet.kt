package cool.parser.pratt.parselets.expressions

import cool.parser.pratt.parselets.SimpleParselet
import cool.parser.pratt.Parser
import cool.tree.AbstractExpression
import cool.util.NameTable
import cool.tree.New


public class NewParselet : SimpleParselet {

    public override fun parse(parser: Parser): AbstractExpression {
        val lineNumber = parser.token.lineNumber
        parser.nextToken()
        val name = NameTable.addString(parser.token.obj.toString())
        return New(lineNumber, name)
    }
}