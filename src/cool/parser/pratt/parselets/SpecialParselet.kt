package cool.parser.pratt.parselets

import cool.tree.TreeNode
import cool.lexer.Token
import cool.parser.pratt.Parser


public trait SpecialParselet : Parselet {
    public fun parse(parser: Parser): TreeNode
}