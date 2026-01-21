package starter

import org.scalatest.FunSuite

import lms.core.stub.Adapter

import starter._

class TestStagedConcolicEval extends FunSuite {
    import sys.process._

  def testExample() = {
    ExampleToCppCompiler.compile()
  }

  test("example") { testExample() }

}
