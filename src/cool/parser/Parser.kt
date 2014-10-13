package cool.parser

import cool.lexer.Token
import cool.lexer.Lexer
import cool.tree.TreeNode
import cool.lexer.TokenConstants
import cool.tree.AbstractClass
import cool.tree.Clazz
import cool.tree.Features
import cool.tree.AbstractFeature
import cool.util.Name
import cool.util.NameTable
import cool.tree.AbstractExpression
import java.util.ArrayList
import cool.util.Errors
import cool.tree.ErrorFeature
import cool.tree.Formals
import cool.tree.AbstractFormal
import cool.tree.Method
import cool.tree.Attr
import cool.tree.Formal
import cool.tree.Assign
import cool.tree.Expressions
import cool.tree.Call
import cool.tree.ObjectId
import cool.tree.Dispatch
import cool.tree.BinaryExpr
import cool.tree.IfExpr
import cool.tree.WhileExpr
import cool.tree.Block
import cool.tree.New
import cool.tree.IsVoid
import cool.tree.Neg
import cool.tree.Not
import cool.tree.Comp
import cool.tree.ErrorExpr
import cool.tree.IntLiteral
import cool.tree.StringLiteral
import cool.tree.BoolLiteral



/* @deprecated */
class Parser(val lexer: Lexer) {
    private var token: Token = lexer.nextToken()

    private fun error(key: Int) {
        Errors.printError(token.lineNumber, key)
    }

    private fun accept(key: Int) {
        if (token.key == key) {
            nextToken()
        } else {
            Errors.printError(token.lineNumber, key)
        }
    }

    private fun illegal(): AbstractExpression {
        Errors.printError(token.lineNumber, "No expression")
        return ErrorExpr(token.lineNumber)
    }

    private fun skip() {

    }

    private fun prec(key: Int): Int {
        return when (token.key) {
            TokenConstants.EQ -> 1
            TokenConstants.LE,
            TokenConstants.LT -> 2
            TokenConstants.PLUS,
            TokenConstants.MINUS -> 3
            TokenConstants.MULT,
            TokenConstants.DIV -> 4
            else -> -1
        }
    }

    private fun nextToken() {
        token = lexer.nextToken()
    }

    public fun parse(): TreeNode {
        return clazz()
    }

    private fun name(key: Int): Name {
        return if (token.key == key) {
            val obj = token.obj
            nextToken()
            NameTable.addString(obj.toString())
        } else
            NameTable.emptyName()
    }

    /**
     * class = CLASS TYPEID [INHERITS TYPEID] "{" features "}"
     */
    private fun clazz(): AbstractClass {
        if (token.key == TokenConstants.CLASS) {
            nextToken()
            val name: Name = name(TokenConstants.TYPEID)
            var parent: Name? = null
            if (token.key == TokenConstants.INHERITS) {
                nextToken()
                parent = name(TokenConstants.TYPEID)
            }
            accept(TokenConstants.LBRACE)
            val features = features()
            accept(TokenConstants.RBRACE)
            return Clazz(token.lineNumber, name, parent, features)
        } else {
            error(TokenConstants.CLASS)
            skip()
            return Clazz(token.lineNumber, NameTable.emptyName(), NameTable.emptyName(), Features(token.lineNumber))
        }
    }

    /**
     * features = { feature }
     */
    private fun features(): Features {
        val elements = ArrayList<AbstractFeature>()
        while (token.key != TokenConstants.RBRACE) {
            if (token.key == TokenConstants.OBJECTID) {
                elements.add(feature())
            }
            accept(TokenConstants.SEMI)
        }
        return Features(token.lineNumber, elements)
    }

    /**
     * feature = OBJID feature_rest
     */
    private fun feature(): AbstractFeature {
        val lineNumber = token.lineNumber
        if (token.key == TokenConstants.OBJECTID) {
            val name = NameTable.addString(token.obj.toString())
            nextToken()
            return featureRest(lineNumber, name)
        } else {
            skip()
            return ErrorFeature(lineNumber)
        }
    }

    /**
     * feature_rest = "(" formals ")" ":" TYPEID "{" expr "}"
     *                | ":" TYPEID ["<-" expr ";"]
     */
    private fun featureRest(lineNumber: Int,
                            name: Name): AbstractFeature {
        val feature: AbstractFeature = when (token.key) {
            TokenConstants.LPAREN -> {
                nextToken()
                val formals = formals()
                nextToken()
                accept(TokenConstants.RPAREN)
                accept(TokenConstants.COLON)
                val typeName: Name = name(TokenConstants.TYPEID)
                accept(TokenConstants.LBRACE)
                val body: AbstractExpression = expr(0)
                accept(TokenConstants.RBRACE)
                Method(lineNumber, name, typeName, formals, body)
            }
            TokenConstants.COLON -> {
                nextToken()
                val typeName: Name = name(TokenConstants.TYPEID)
                val init: AbstractExpression? = if (token.key == TokenConstants.ASSIGN) {
                    nextToken()
                    expr(0)
                } else null
                accept(TokenConstants.SEMI)
                Attr(lineNumber, name, typeName, init)
            }
            else -> {
                error(TokenConstants.COLON)
                skip()
                ErrorFeature(lineNumber)
            }
        }
        return feature
    }

    /**
     * formals = { formal }
     */
    private fun formals(): Formals {
        val lineNumber = token.lineNumber
        val elements = ArrayList<AbstractFormal>()
        while (token.key != TokenConstants.RPAREN) {
            elements.add(formal())
            if (token.key != TokenConstants.RPAREN) {
                accept(TokenConstants.COMMA)
            }
        }
        return Formals(lineNumber, elements)
    }

    /**
     * formal = OBJID ":" TYPEID
     */
    private fun formal(): AbstractFormal {
        val lineNumber = token.lineNumber
        val name: Name = name(TokenConstants.OBJECTID)
        accept(TokenConstants.COLON)
        val typeName: Name = name(TokenConstants.TYPEID)
        return Formal(lineNumber, name, typeName)
    }

    /**
     * expr_list = { expr [","] }
     */
    private fun expr_list(): Expressions {
        val lineNumber = token.lineNumber
        val expressions = ArrayList<AbstractExpression>()
        while (token.key != TokenConstants.RPAREN) {
            expressions.add(expr(0))
            if (token.key != TokenConstants.RPAREN) {
                accept(TokenConstants.COMMA)
            }
        }
        return Expressions(lineNumber, expressions)
    }

    /**
     * expr = OBJID obj_rest | expr1
     */

    private fun expr(minPrec: Int): AbstractExpression {
        val expr1 = expr1()
        return expr_rest(expr1, minPrec)
    }

    /**
     * expr_rest = [ "@" TYPEID ] "." OBJID "(" expr_list ")"
     *              | { operator expr }
     * operator =     "||"
     *              | "&&"
     *              | "=="
     *              | "<" | ">" | "<=" | ">="
     *              | "+" | "-"
     *              | "*" | "/"
     */
    private fun expr_rest(expr1: AbstractExpression,
                          minPrec: Int): AbstractExpression {
        val typeName: Name? = if (token.key == TokenConstants.AT) {
            nextToken()
            name(TokenConstants.TYPEID)
        } else null
        val lineNumber = token.lineNumber
        return when (token.key) {
            TokenConstants.DOT -> {
                nextToken()
                val name: Name = name(TokenConstants.OBJECTID)
                accept(TokenConstants.LPAREN)
                val expr_list = expr_list()
                accept(TokenConstants.RPAREN)
                Dispatch(lineNumber, expr1, typeName, name, expr_list)
            }
            TokenConstants.PLUS,
            TokenConstants.MINUS,
            TokenConstants.MULT,
            TokenConstants.DIV,
            TokenConstants.LT,
            TokenConstants.LE,
            TokenConstants.EQ -> {
                val lineNumber = token.lineNumber
                var expr = expr1
                while (minPrec <= prec(token.key)) {
                    val op = token.key
                    val q = minPrec
                    nextToken()
                    val expr2 = expr(q)
                    expr = BinaryExpr(lineNumber, op, expr, expr2)
                }
                expr
            }
            else -> expr1
        }
    }

    private fun expr1(): AbstractExpression {
        return if (token.key == TokenConstants.OBJECTID) {
            val lineNumber = token.lineNumber
            val name = NameTable.addString(token.obj.toString())
            nextToken()
            obj_rest(lineNumber, name)
        } else {
            expr2()
        }
    }

    /**
     * obj_rest = "<-" expr expr_rest |  "(" expr_list ")" expr_rest | expr_rest
     */
    private fun obj_rest(lineNumber: Int, name: Name): AbstractExpression {
        return when (token.key) {
            TokenConstants.ASSIGN -> {
                nextToken()
                val init = expr(0)
                Assign(lineNumber, name, init)
            }
            TokenConstants.LPAREN -> {
                nextToken()
                val expr_list = expr_list()
                accept(TokenConstants.RPAREN)
                Call(lineNumber, name, expr_list)
            }
            else -> ObjectId(lineNumber, name)
        }
    }

    /**
     * expr1 = IF expr THEN expr ELSE expr FI
     *          | WHILE expr LOOP expr POOL
     *          | "{" block_exprs "}"
     *          | LET OBJID ":" TYPEID ["<-" expr] {"," OBJID ":" TYPEID ["<-" expr] } vars IN expr //TODO:
     *          | CASE expr OF OBJID ":" TYPEID => expr; { OBJID ":" TYPEID => expr; } ESAC //TODO:
     *          | NEW TYPEID
     *          | ISVOID
     *          | "~" expr
     *          | NOT expr
     *          | "(" expr ")"
     *          | literal
     */
    private fun expr2(): AbstractExpression {
        val lineNumber = token.lineNumber
        return when (token.key) {
            TokenConstants.IF -> {
                nextToken()
                val cond = expr(0)
                accept(TokenConstants.THEN)
                val thenExpr = expr(0)
                accept(TokenConstants.ELSE)
                val elseExpr = expr(0)
                accept(TokenConstants.FI)
                IfExpr(lineNumber, cond, thenExpr, elseExpr)
            }
            TokenConstants.WHILE -> {
                nextToken()
                val cond = expr(0)
                accept(TokenConstants.LOOP)
                val body = expr(0)
                accept(TokenConstants.POOL)
                WhileExpr(lineNumber, cond, body)
            }
            TokenConstants.LBRACE -> {
                nextToken()
                val expr_list = block_exprs()
                accept(TokenConstants.RBRACE)
                Block(lineNumber, expr_list)
            }
            TokenConstants.NEW -> {
                nextToken()
                val typeName = name(TokenConstants.TYPEID)
                New(lineNumber, typeName)
            }
            TokenConstants.ISVOID -> {
                nextToken()
                val expr = expr(0)
                IsVoid(lineNumber, expr)
            }
            TokenConstants.MINUS -> {
                nextToken()
                val expr = expr(0)
                Neg(lineNumber, expr)
            }
            TokenConstants.NOT -> {
                nextToken()
                val expr = expr(0)
                Not(lineNumber, expr)
            }
            TokenConstants.NEG -> {
                nextToken()
                val expr = expr(0)
                Comp(lineNumber, expr)
            }
            TokenConstants.LPAREN -> {
                nextToken()
                val expr = expr(0)
                accept(TokenConstants.RPAREN)
                expr
            }
            TokenConstants.INT_CONST,
            TokenConstants.STR_CONST,
            TokenConstants.BOOL_CONST -> literal()
            else -> illegal()
        }
    }

    /**
     * block_exprs = expr ";" { expr ";" }
     */
    private fun block_exprs(): Expressions {
        val lineNumber = token.lineNumber
        val expressions = ArrayList<AbstractExpression>()
        expressions.add(expr(0))
        accept(TokenConstants.SEMI)
        while (token.key != TokenConstants.RPAREN) {
            expressions.add(expr(0))
            accept(TokenConstants.SEMI)
        }
        return Expressions(lineNumber, expressions)
    }

    /**
     * literal = INT | STRING | TRUE | FALSE
     */
    private fun literal(): AbstractExpression {
        val lineNumber = token.lineNumber
        return when (token.key) {
            TokenConstants.INT_CONST -> IntLiteral(lineNumber, token.obj as Int)
            TokenConstants.STR_CONST -> StringLiteral(lineNumber, token.obj as String)
            TokenConstants.BOOL_CONST -> BoolLiteral(lineNumber, token.obj as Boolean)
            else -> illegal()
        }
    }

}