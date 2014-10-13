package cool.util

import java.util.ArrayList



object NameTable {
    val table: List<Name> = ArrayList<Name>()

    public fun emptyName(): Name = EmptyName()

    public fun addString(str: String): Name {
        for (item in table) {
            if (item.str == str) {
                return item
            }
        }
        val name = Name(str, table.size)
        return name
    }
}