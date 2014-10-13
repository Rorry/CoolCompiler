package cool.util



data open class Name(val str: String, val index: Int) {
    public override fun toString(): String = str

    public override fun equals(other: Any?) : Boolean {
        return other is Name && other.index == this.index
    }
}

class EmptyName : Name("", -1)