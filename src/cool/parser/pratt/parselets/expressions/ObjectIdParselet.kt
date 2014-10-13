package cool.parser.pratt.parselets

import cool.parser.pratt.Parser
import cool.tree.AbstractExpression
import cool.tree.ObjectId
import cool.lexer.TokenConstants
import cool.util.NameTable


public class ObjectIdParselet : SimpleParselet {

    public override fun parse(parser: Parser): AbstractExpression {
        val lineNumber = parser.token.lineNumber
        val name = NameTable.addString(parser.token.obj.toString())
        return ObjectId(lineNumber, name)
    }

}