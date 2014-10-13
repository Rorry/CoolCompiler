package cool.parser.pratt.parselets

import cool.parser.pratt.Parser
import cool.lexer.Token
import cool.lexer.TokenConstants
import cool.util.NameTable
import cool.util.Name
import cool.tree.TreeNode
import cool.tree.Clazz
import cool.tree.Features
import java.util.ArrayList
import cool.tree.AbstractFeature
import cool.tree.AbstractClass


public class ClazzParselet : SpecialParselet {

    /**
     * class = CLASS TYPEID [INHERITS TYPEID] "{" features "}"
     */
    public override fun parse(parser: Parser): AbstractClass {
        if (parser.token.key == TokenConstants.CLASS) {
            val lineNumber = parser.token.lineNumber
            parser.nextToken()
            val name: Name = parser.getName(TokenConstants.TYPEID)
            var parent: Name? = null
            if (parser.token.key == TokenConstants.INHERITS) {
                parser.nextToken()
                parent = parser.getName(TokenConstants.TYPEID)
            }
            parser.accept(TokenConstants.LBRACE)
            val features = features(parser)
            parser.accept(TokenConstants.RBRACE)
            return Clazz(lineNumber, name, parent, features)
        } else {
            parser.error(TokenConstants.CLASS)
            parser.skip()
            return Clazz(parser.token.lineNumber, NameTable.emptyName(), NameTable.emptyName(), Features(parser.token.lineNumber))
        }
    }

    /**
     * features = { feature }
     */
    private fun features(parser: Parser): Features {
        val elements = ArrayList<AbstractFeature>()
        val lineNumber = parser.token.lineNumber
        while (parser.token.key != TokenConstants.RBRACE && parser.token.key != TokenConstants.EOF) {
            if (parser.token.key == TokenConstants.OBJECTID) {
                elements.add(parser.parseFeature())
            }
            parser.accept(TokenConstants.SEMI)
        }
        return Features(lineNumber, elements)
    }
}