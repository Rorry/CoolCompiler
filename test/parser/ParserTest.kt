package parser

import junit.framework.Assert
import org.junit.Test as test
import java.io.File
import cool.lexer.Lexer
import cool.parser.pratt.CoolParser
import cool.tree.TreeNode
import cool.tree.Printer
import java.io.PrintStream
import java.io.BufferedInputStream
import java.io.FileInputStream


public class ParserTest {
    private fun createParserResult(fileCL: File, fileOUT: File) {
        val printStream = PrintStream(fileOUT)

        val lexer = Lexer(fileCL.reader())
        val parser = CoolParser(lexer)
        val tree: TreeNode = parser.parse()
        val printer = Printer(printStream)

        printer.print(tree)
        printer.close()
    }

    test fun parseTest() {
        val path: String = File(".").canonicalPath
        val fileCL = File(path + "/resources/hello_world.cl")
        val fileOUT =  File(path + "/test/resources/out/hello_world.out")
        val fileTest = File(path + "/test/resources/hello_world.test")

        createParserResult(fileCL, fileOUT)

        Assert.assertEquals(fileTest.readText(), fileOUT.readText())
    }
}