package cool.tree


public trait Visitor {
    public fun visitClass(clazz: Clazz);

    public fun visitMethod(method: Method)

    public fun visitAttr(attr: Attr)

    public fun visitFormal(formal: Formal)

    public fun visitAssign(assign: Assign)

    public fun visitCall(call: Call)

    public fun visitDispatch(dispatch: Dispatch)

    public fun visitObjectId(objectId: ObjectId)

    public fun visitBinaryExpression(binaryExpr: BinaryExpr)

    public fun visitIfExpression(ifExpr: IfExpr)

    public fun visitWhileExpression(whileExpr: WhileExpr)

    public fun visitBlock(block: Block)

    public fun visitNew(newExpr: New)

    public fun visitIsVoid(isVoid: IsVoid)

    public fun visitNeg(neg: Neg)

    public fun visitNot(not: Not)

    public fun visitComp(comp: Comp)

    public fun visitInt(intLiteral: IntLiteral)

    public fun visitString(str: StringLiteral)

    public fun visitBool(bool: BoolLiteral)
}