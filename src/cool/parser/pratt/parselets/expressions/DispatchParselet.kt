package cool.parser.pratt.parselets.expressions

import cool.parser.pratt.parselets.BinaryParselet
import cool.parser.pratt.Parser
import cool.tree.AbstractExpression
import cool.parser.pratt.Precedence
import cool.lexer.TokenConstants
import cool.util.Name
import cool.tree.Expressions
import java.util.ArrayList
import cool.tree.Dispatch


public class DispatchParselet : BinaryParselet {

    public override fun parse(parser: Parser, left: AbstractExpression): AbstractExpression {
        val lineNumber = parser.token.lineNumber
        val typeName: Name? = if (parser.token.key == TokenConstants.AT) {
            parser.nextToken()
            parser.getName(TokenConstants.TYPEID)
        } else null
        parser.accept(TokenConstants.DOT)
        val name = parser.getName(TokenConstants.OBJECTID)
        parser.accept(TokenConstants.LPAREN)
        val exprs: Expressions = expr_list(parser)
        return Dispatch(lineNumber, left, typeName, name, exprs)
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