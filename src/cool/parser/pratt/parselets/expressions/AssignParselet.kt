package cool.parser.pratt.parselets

import cool.parser.pratt.Parser
import cool.parser.pratt.Precedence
import cool.tree.AbstractExpression
import cool.tree.Assign
import cool.tree.ObjectId
import cool.util.NameTable
import cool.util.Name
import cool.lexer.TokenConstants


public class AssignParselet : BinaryParselet {

    public override fun parse(parser: Parser, left: AbstractExpression): AbstractExpression {
        val lineNumber = parser.token.lineNumber
        val name: Name = when (left) {
            is ObjectId -> left.name
            else -> {
                parser.error(TokenConstants.OBJECTID)
                NameTable.emptyName() }
        }
        parser.nextToken()
        val init = parser.parseExpression(Precedence.ASSIGN - 1)
        return Assign(lineNumber, name, init)
    }

}