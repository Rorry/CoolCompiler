package cool.parser.pratt.parselets

import cool.parser.pratt.Parser
import cool.lexer.TokenConstants
import cool.util.Name
import cool.util.NameTable
import cool.tree.TreeNode
import cool.tree.AbstractFeature
import cool.tree.ErrorFeature
import cool.tree.Formals
import cool.tree.AbstractFormal
import cool.tree.Attr
import cool.tree.Method
import cool.tree.AbstractExpression
import cool.tree.Formal

import java.util.ArrayList


public class FeatureParselet : SpecialParselet {

    /**
     * feature = OBJID feature_rest
     */
    public override fun parse(parser: Parser): AbstractFeature {
        val lineNumber = parser.token.lineNumber
        if (parser.token.key == TokenConstants.OBJECTID) {
            val name = NameTable.addString(parser.token.obj.toString())
            parser.nextToken()
            return featureRest(parser, lineNumber, name)
        } else {
            parser.skip()
            return ErrorFeature(lineNumber)
        }
    }

    /**
     * feature_rest = "(" formals ")" ":" TYPEID "{" expr "}"
     *                | ":" TYPEID ["<-" expr ";"]
     */
    private fun featureRest(parser: Parser,
                            lineNumber: Int,
                            name: Name): AbstractFeature {
        val feature: AbstractFeature = when (parser.token.key) {
            TokenConstants.LPAREN -> {
                parser.nextToken()
                val formals = formals(parser)
                parser.accept(TokenConstants.RPAREN)
                parser.accept(TokenConstants.COLON)
                val typeName: Name = parser.getName(TokenConstants.TYPEID)
                parser.accept(TokenConstants.LBRACE)
                val body: AbstractExpression = parser.parseExpression()
                parser.accept(TokenConstants.SEMI)
                parser.accept(TokenConstants.RBRACE)
                Method(lineNumber, name, typeName, formals, body)
            }
            TokenConstants.COLON -> {
                parser.nextToken()
                val typeName: Name = parser.getName(TokenConstants.TYPEID)
                val init: AbstractExpression? = if (parser.token.key == TokenConstants.ASSIGN) {
                    parser.nextToken()
                    val expr = parser.parseExpression()
                    expr
                } else null
                parser.check(TokenConstants.SEMI)
                Attr(lineNumber, name, typeName, init)
            }
            else -> {
                parser.error(TokenConstants.COLON)
                parser.skip()
                ErrorFeature(lineNumber)
            }
        }
        return feature
    }

    /**
     * formals = { formal }
     */
    private fun formals(parser: Parser): Formals {
        val lineNumber = parser.token.lineNumber
        val elements = ArrayList<AbstractFormal>()
        while (parser.token.key != TokenConstants.RPAREN && parser.token.key != TokenConstants.EOF) {
            elements.add(formal(parser))
            if (parser.token.key != TokenConstants.RPAREN) {
                parser.accept(TokenConstants.COMMA)
            }
        }
        return Formals(lineNumber, elements)
    }

    /**
     * formal = OBJID ":" TYPEID
     */
    private fun formal(parser: Parser): AbstractFormal {
        val lineNumber = parser.token.lineNumber
        val name: Name = parser.getName(TokenConstants.OBJECTID)
        parser.accept(TokenConstants.COLON)
        val typeName: Name = parser.getName(TokenConstants.TYPEID)
        return Formal(lineNumber, name, typeName)
    }

}