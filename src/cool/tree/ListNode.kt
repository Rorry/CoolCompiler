package cool.tree



abstract class ListNode(lineNumber: Int, elements: List<TreeNode>) : TreeNode(lineNumber) {
    private val elements: List<TreeNode> = elements

    public override fun accept(visitor: Visitor) {
        for (element in elements) {
            element.accept(visitor)
        }
    }
}