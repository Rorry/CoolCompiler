package main

import cool.lexer.TokenConstants
import cool.lexer.Lexer
import java.io.File
import cool.parser.pratt.CoolParser
import cool.tree.TreeNode
import cool.tree.Printer



fun main (args: Array<String>) {
    val path: String = File(".").canonicalPath
    val file = File(path + "/resources/hello_world.cl")
    val lexer = Lexer(file.reader())

//    var token = lexer.nextToken()
//    while (token.key != TokenConstants.EOF) {
//        println(token)
//        token = lexer.nextToken()
//    }
//    println((-1).toChar())

    val parser = CoolParser(lexer)
    val tree: TreeNode = parser.parse()
    val printer = Printer(System.out)

    printer.print(tree)
}