package cool.parser.pratt

import cool.tree.TreeNode
import cool.lexer.Lexer
import cool.lexer.Token
import cool.tree.AbstractClass
import cool.tree.AbstractFeature
import cool.parser.pratt.parselets.ClazzParselet
import cool.tree.AbstractExpression
import cool.parser.pratt.parselets.FeatureParselet
import cool.parser.pratt.parselets.SimpleParselet
import java.util.HashMap
import cool.parser.pratt.parselets.BinaryParselet
import cool.util.Errors
import cool.tree.ErrorExpr
import cool.lexer.TokenConstants
import cool.parser.pratt.parselets.ObjectIdParselet
import cool.parser.pratt.parselets.expressions.BlockParselet
import cool.parser.pratt.parselets.expressions.GroupParselet
import cool.parser.pratt.parselets.expressions.IfParselet
import cool.parser.pratt.parselets.expressions.IsVoidParselet
import cool.parser.pratt.parselets.expressions.MinusParselet
import cool.parser.pratt.parselets.expressions.NegParselet
import cool.parser.pratt.parselets.expressions.NewParselet
import cool.parser.pratt.parselets.expressions.NotParselet
import cool.parser.pratt.parselets.expressions.IntParselet
import cool.parser.pratt.parselets.expressions.BoolParselet
import cool.parser.pratt.parselets.expressions.StringParselet
import cool.parser.pratt.parselets.expressions.WhileParslet
import cool.parser.pratt.parselets.AssignParselet
import cool.parser.pratt.parselets.expressions.BinaryOperationParselet
import cool.parser.pratt.parselets.expressions.CallParselet
import cool.parser.pratt.parselets.expressions.DispatchParselet



class CoolParser(val lexer: Lexer) : Parser {
    public override var token: Token = lexer.nextToken()

    private val clazzParselet: ClazzParselet = ClazzParselet()
    private val featureParselet: FeatureParselet = FeatureParselet()

    private val simpleParselets: MutableMap<Int, SimpleParselet> = HashMap<Int, SimpleParselet>()
    private val binaryParselets: MutableMap<Int, BinaryParselet> = HashMap<Int, BinaryParselet>()

    ;{
        //Simple parselets
        registerSimple(TokenConstants.LBRACE, BlockParselet())
        registerSimple(TokenConstants.LPAREN, GroupParselet())
        registerSimple(TokenConstants.IF, IfParselet())
        registerSimple(TokenConstants.ISVOID, IsVoidParselet())
        registerSimple(TokenConstants.MINUS, MinusParselet())
        registerSimple(TokenConstants.NEG, NegParselet())
        registerSimple(TokenConstants.NEW, NewParselet())
        registerSimple(TokenConstants.NOT, NotParselet())
        registerSimple(TokenConstants.OBJECTID, ObjectIdParselet())
        registerSimple(TokenConstants.WHILE, WhileParslet())
        registerSimple(TokenConstants.INT_CONST, IntParselet())
        registerSimple(TokenConstants.BOOL_CONST, BoolParselet())
        registerSimple(TokenConstants.STR_CONST, StringParselet())

        //Binary parselets
        registerBinary(TokenConstants.ASSIGN, AssignParselet())
        registerBinary(TokenConstants.PLUS, BinaryOperationParselet())
        registerBinary(TokenConstants.MINUS, BinaryOperationParselet())
        registerBinary(TokenConstants.MULT, BinaryOperationParselet())
        registerBinary(TokenConstants.DIV, BinaryOperationParselet())
        registerBinary(TokenConstants.LT, BinaryOperationParselet())
        registerBinary(TokenConstants.LE, BinaryOperationParselet())
        registerBinary(TokenConstants.EQ, BinaryOperationParselet())
        registerBinary(TokenConstants.LPAREN, CallParselet())
        registerBinary(TokenConstants.AT, DispatchParselet())
        registerBinary(TokenConstants.DOT, DispatchParselet())
    }

    private fun registerSimple(key: Int, parselet: SimpleParselet) {
        simpleParselets.put(key, parselet)
    }

    private fun registerBinary(key: Int, parselet: BinaryParselet) {
        binaryParselets.put(key, parselet)
    }

    public override fun lexer(): Lexer = lexer

    public override fun parse(): TreeNode {
        return parseClazz()
    }

    public override fun parseClazz(): AbstractClass {
        return clazzParselet.parse(this)
    }

    public override fun parseFeature(): AbstractFeature {
        return featureParselet.parse(this)
    }

    public override fun parseExpression(): AbstractExpression {
        return parseExpression(0)
    }

    public override fun parseExpression(precedence: Int): AbstractExpression {
        val simpleParselet: SimpleParselet? = simpleParselets.get(token.key)

        if (simpleParselet == null) {
            return illegal()
        }

        var left: AbstractExpression = simpleParselet.parse(this)

        while (precedence <= Precedence.map(nextToken().key)) {
            val binaryParselet: BinaryParselet? = binaryParselets.get(token.key)

            if (binaryParselet != null) {
                left = binaryParselet.parse(this, left)
            }
        }

        return left
    }

    private fun illegal(): AbstractExpression {
        Errors.printError(token.lineNumber, "No expression")
        return ErrorExpr(token.lineNumber)
    }
}
