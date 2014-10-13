package cool.tree

import java.io.PrintStream


public class Printer(val out: PrintStream) : Visitor {
    val T = "    "
    val S = " "

    var currentTab = ""

    public fun print(tree: TreeNode) {
        tree.accept(this)
    }

    public fun close() {
        out.close()
    }

    public override fun visitClass(clazz: Clazz) {
        val prevTab = currentTab

        out.print("CLASS${S}")
        out.print(clazz.name)
        out.print("${S}:${S}")
        out.print(clazz.parent)
        out.print("${S}{\n")
        currentTab = "${T}"
        clazz.features.accept(this)
        currentTab = prevTab
        out.print("}\n")
    }
    public override fun visitMethod(method: Method) {
        val prevTab = currentTab

        out.print(currentTab)
        out.print("METHOD${S}")
        out.print(method.name)
        out.print("${S}:${S}")
        out.print(method.typeName)
        out.print("${S}(")
        method.formals.accept(this)
        out.print(")${S}")
        out.print("{\n")
        currentTab = "${T}${T}"
        out.print(currentTab)
        method.body.accept(this)
        currentTab = prevTab
        out.print("\n")
        out.print(currentTab)
        out.print("}\n")
    }
    public override fun visitAttr(attr: Attr) {

        out.print(currentTab)
        out.print("ATTR${S}")
        out.print(attr.name)
        out.print("${S}:${S}")
        out.print(attr.typeName)
        if (attr.init != null) {
            out.print("${S}<-${S}")
            attr.init.accept(this)
        }
        out.print("\n")
    }
    public override fun visitFormal(formal: Formal) {
        out.print("${S}(")
        out.print(formal.name)
        out.print("${S}:${S}")
        out.print(formal.typeName)
        out.print(")${S}")
    }
    public override fun visitAssign(assign: Assign) {
        out.print(assign.name)
        out.print("${S}<-${S}")
        assign.init.accept(this)
    }
    public override fun visitCall(call: Call) {
        out.print(call.name)
        out.print("${S}(")
        call.expr_list.accept(this)
        out.print(")")
    }
    public override fun visitDispatch(dispatch: Dispatch) {
        throw UnsupportedOperationException()
    }
    public override fun visitObjectId(objectId: ObjectId) {
        throw UnsupportedOperationException()
    }
    public override fun visitBinaryExpression(binaryExpr: BinaryExpr) {
        throw UnsupportedOperationException()
    }
    public override fun visitIfExpression(ifExpr: IfExpr) {
        throw UnsupportedOperationException()
    }
    public override fun visitWhileExpression(whileExpr: WhileExpr) {
        throw UnsupportedOperationException()
    }
    public override fun visitBlock(block: Block) {
        val prevTab = currentTab

        out.print("{\n")
        currentTab += "${T}"
        out.print(currentTab)
        block.expr_list.accept(this)
        currentTab = prevTab
        out.print("\n}")
    }
    public override fun visitNew(newExpr: New) {
        throw UnsupportedOperationException()
    }
    public override fun visitIsVoid(isVoid: IsVoid) {
        throw UnsupportedOperationException()
    }
    public override fun visitNeg(neg: Neg) {
        throw UnsupportedOperationException()
    }
    public override fun visitNot(not: Not) {
        throw UnsupportedOperationException()
    }
    public override fun visitComp(comp: Comp) {
        throw UnsupportedOperationException()
    }
    public override fun visitInt(intLiteral: IntLiteral) {
        out.print(intLiteral.value)
    }
    public override fun visitString(str: StringLiteral) {
        out.print(str.value)
    }
    public override fun visitBool(bool: BoolLiteral) {
        out.print(bool.value)
    }
}