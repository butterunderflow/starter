package starter

import scala.collection.mutable.{ArrayBuffer, HashMap}

import lms.core.stub.Adapter
import lms.core.virtualize
import lms.macros.SourceContext
import lms.core.stub.{Base, ScalaGenBase, CGenBase}
import lms.core.Backend._
import lms.core.Backend.{Block => LMSBlock, Const => LMSConst}
import lms.core.Graph

import gensym.lmsx.{SAIDriver, StringOps, SAIOps, SAICodeGenBase, CppSAIDriver, CppSAICodeGenBase}

@virtualize
trait StagedExample extends SAIOps {
  def power(x: Rep[Int], n: Int): Rep[Int] = {
    if (n == 0) 1
    else x * power(x, n - 1)
  }
}

trait StagedExampleCppGen extends CGenBase with CppSAICodeGenBase {
  headers.clear()
  // Add any necessary C++ headers
  registerHeader("<iostream>")


  override def traverse(n: Node): Unit = n match {
    case _ => super.traverse(n)
  }

  override def shallow(n: Node): Unit = n match {
    case _ => super.shallow(n)
  }

  override def emitAll(g: Graph, name: String)(m1: Manifest[_], m2: Manifest[_]): Unit = {
    val ng = init(g)
    emitHeaders(stream)
    emitln("""
    |/*****************************************
    |Emitting Generated Code
    |*******************************************/
    """.stripMargin)
    val src = run(name, ng)
    emitFunctionDecls(stream)
    emitDatastructures(stream)
    emitFunctions(stream)
    emit(src)
    emitln(s"""
    |/*****************************************
    |End of Generated Code
    |*******************************************/
    |int main(int argc, char *argv[]) {
    |  Snippet(atoi(argv[1]));
    |  return 0;
    |}""".stripMargin)
  }

}

trait ExampleToCppCompilerDriver[A, B] extends CppSAIDriver[A, B] with StagedExample { q =>
  override val codegen = new StagedExampleCppGen {
    val IR: q.type = q
    import IR._
  }
}

object ExampleToCppCompiler {

  def compile() = {
    println(s"Now compiling example to C++...")

    val driver = new ExampleToCppCompilerDriver[Int, Int] {
      def snippet(x: Rep[Int]): Rep[Int] = {
        power(x, 3)
      }
    }

    println(driver.code)
  }

}

