package cool.parser.pratt.parselets.expressions

import cool.parser.pratt.parselets.BinaryParselet
import cool.parser.pratt.Parser
import cool.parser.pratt.Precedence
import cool.util.Name
import cool.tree.AbstractExpression
import cool.tree.ObjectId
import cool.tree.Expressions
import java.util.ArrayList
import cool.lexer.TokenConstants
import cool.util.NameTable
import cool.tree.Call


//TODO: join with DispatchParselet
public class CallParselet : BinaryParselet {

    public override fun parse(parser: Parser, left: AbstractExpression): AbstractExpression {
        val lineNumber = parser.token.lineNumber
        val name: Name = when (left) {
            is ObjectId -> left.name
            else -> {
                parser.error(TokenConstants.OBJECTID)
                NameTable.emptyName()
            }
        }
        parser.nextToken()
        val exprs: Expressions = expr_list(parser)
        return Call(lineNumber, name, exprs)
    }

    /**
     * expr_list = [] | expr { "," expr }
     */
    private fun expr_list(parser: Parser): Expressions {
        val lineNumber = parser.token.lineNumber
        val expressions = ArrayList<AbstractExpression>()
        while (parser.token.key != TokenConstants.RPAREN && parser.token.key != TokenConstants.EOF) {
            expressions.add(parser.parseExpression())
            if (parser.token.key != TokenConstants.RPAREN) {
                parser.accept(TokenConstants.COMMA)
            }
        }
        return Expressions(lineNumber, expressions)
    }
}