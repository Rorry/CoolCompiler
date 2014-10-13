package cool.parser.pratt.parselets.expressions

import cool.parser.pratt.parselets.SimpleParselet
import cool.parser.pratt.Parser
import cool.tree.AbstractExpression
import cool.tree.Expressions
import java.util.ArrayList
import cool.lexer.TokenConstants
import cool.tree.Block


public class BlockParselet : SimpleParselet {

    public override fun parse(parser: Parser): AbstractExpression {
        val lineNumber = parser.token.lineNumber
        parser.nextToken()
        val expr_list = expr_list(parser)
        return Block(lineNumber, expr_list)
    }

    /**
     * expr_list = expr ";" { expr ";" }
     */
    private fun expr_list(parser: Parser): Expressions {
        val lineNumber = parser.token.lineNumber
        val expressions = ArrayList<AbstractExpression>()
        expressions.add(parser.parseExpression())
        parser.accept(TokenConstants.SEMI)
        while (parser.token.key != TokenConstants.RBRACE && parser.token.key != TokenConstants.EOF) {
            expressions.add(parser.parseExpression())
            parser.accept(TokenConstants.SEMI)
        }
        return Expressions(lineNumber, expressions)
    }
}