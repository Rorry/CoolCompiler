package cool.tree



abstract class TreeNode(lineNumber: Int) {
    public abstract fun accept(visitor: Visitor);
}
