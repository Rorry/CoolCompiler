package cool.tree

import java.util.Collections
import cool.util.Name



abstract class AbstractClass(lineNumber: Int) : TreeNode(lineNumber) {

}

abstract class AbstractFeature(lineNumber: Int) : TreeNode(lineNumber) {

}

class Features(val lineNumber: Int, val elements: List<AbstractFeature> = Collections.emptyList()) : ListNode(lineNumber, elements) {

}

abstract class AbstractFormal(lineNumber: Int) : TreeNode(lineNumber) {

}

class Formals(lineNumber: Int, elements: List<AbstractFormal> = Collections.emptyList()) : ListNode(lineNumber, elements) {

}

abstract class AbstractExpression(lineNumber: Int) : TreeNode(lineNumber) {

}

class Expressions(lineNumber: Int, elements: List<AbstractExpression> = Collections.emptyList()) : ListNode(lineNumber, elements) {

}

class Clazz(val lineNumber: Int,
            val name: Name,
            val parent: Name?,
            val features: Features) : AbstractClass(lineNumber) {

    public override fun accept(visitor: Visitor) {
        visitor.visitClass(this)
    }
}

class Method(val lineNumber: Int,
             val name: Name,
             val typeName: Name,
             val formals: Formals = Formals(lineNumber),
             val body: AbstractExpression) : AbstractFeature(lineNumber) {

    public override fun accept(visitor: Visitor) {
        visitor.visitMethod(this)
    }
}

class Attr(val lineNumber: Int,
           val name: Name,
           val typeName: Name,
           val init: AbstractExpression?) : AbstractFeature(lineNumber) {

    public override fun accept(visitor: Visitor) {
        visitor.visitAttr(this)
    }
}

class ErrorFeature(val lineNumber: Int) : AbstractFeature(lineNumber) {

    public override fun accept(visitor: Visitor) {
        throw UnsupportedOperationException()
    }
}

class Formal(val lineNumber: Int,
             val name: Name,
             val typeName: Name) : AbstractFormal(lineNumber) {

    public override fun accept(visitor: Visitor) {
        visitor.visitFormal(this)
    }
}

/* expressions */

class Assign(val lineNumber: Int,
             val name: Name,
             val init: AbstractExpression) : AbstractExpression(lineNumber) {

    public override fun accept(visitor: Visitor) {
        visitor.visitAssign(this)
    }
}

class Call(val lineNumber: Int,
           val name: Name,
           val expr_list: Expressions) : AbstractExpression(lineNumber) {

    public override fun accept(visitor: Visitor) {
        visitor.visitCall(this)
    }
}

class Dispatch(val lineNumber: Int,
               val expr: AbstractExpression,
               val typeName: Name?,
               val name: Name,
               val expr_list: Expressions) : AbstractExpression(lineNumber) {

    public override fun accept(visitor: Visitor) {
        visitor.visitDispatch(this)
    }
}

class ObjectId(val lineNumber: Int,
               val name: Name) : AbstractExpression(lineNumber) {

    public override fun accept(visitor: Visitor) {
        visitor.visitObjectId(this)
    }
}

class BinaryExpr(val lineNumber: Int,
                val op: Int,
                val left: AbstractExpression,
                val right: AbstractExpression) : AbstractExpression(lineNumber) {

    public override fun accept(visitor: Visitor) {
        visitor.visitBinaryExpression(this)
    }
}

class IfExpr(val lineNumber: Int,
             val cond: AbstractExpression,
             val thenExpr: AbstractExpression,
             val elseExpr: AbstractExpression) : AbstractExpression(lineNumber) {

    public override fun accept(visitor: Visitor) {
        visitor.visitIfExpression(this)
    }
}

class WhileExpr(val lineNumber: Int,
                val cond: AbstractExpression,
                val body: AbstractExpression) : AbstractExpression(lineNumber) {

    public override fun accept(visitor: Visitor) {
        visitor.visitWhileExpression(this)
    }
}

class Block(val lineNumber: Int,
            val expr_list: Expressions) : AbstractExpression(lineNumber) {
    public override fun accept(visitor: Visitor) {
        visitor.visitBlock(this)
    }
}

class New(val lineNumber: Int,
          val typeName: Name) : AbstractExpression(lineNumber) {

    public override fun accept(visitor: Visitor) {
        visitor.visitNew(this)
    }
}

class IsVoid(val lineNumber: Int,
             val expr: AbstractExpression) : AbstractExpression(lineNumber) {

    public override fun accept(visitor: Visitor) {
        visitor.visitIsVoid(this)
    }
}

class Neg(val lineNumber: Int,
          val expr: AbstractExpression) : AbstractExpression(lineNumber) {

    public override fun accept(visitor: Visitor) {
        visitor.visitNeg(this)
    }
}

class Not(val lineNumber: Int,
          val expr: AbstractExpression) : AbstractExpression(lineNumber) {

    public override fun accept(visitor: Visitor) {
        visitor.visitNot(this)
    }
}

class Comp(val lineNumber: Int,
           val expr: AbstractExpression) : AbstractExpression(lineNumber) {

    public override fun accept(visitor: Visitor) {
        visitor.visitComp(this)
    }
}

class IntLiteral(val lineNumber: Int, val value: Int) : AbstractExpression(lineNumber) {

    public override fun accept(visitor: Visitor) {
        visitor.visitInt(this)
    }
}

class StringLiteral(val lineNumber: Int, val value: String) : AbstractExpression(lineNumber) {

    public override fun accept(visitor: Visitor) {
        visitor.visitString(this)
    }
}

class BoolLiteral(val lineNumber: Int, val value: Boolean) : AbstractExpression(lineNumber) {

    public override fun accept(visitor: Visitor) {
        visitor.visitBool(this)
    }
}


class ErrorExpr(val lineNumber: Int) : AbstractExpression(lineNumber) {

    public override fun accept(visitor: Visitor) {
        throw UnsupportedOperationException()
    }
}

