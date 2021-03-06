package models

import scala.util.Random

case class Network private (
    size: Int,
    truthTable: TruthTable,
    nodes: Seq[Node]
) {
  def nextNode(node: Node): Node = {
    val (x, y, z) = node.inputs
    val nextVal =
      truthTable.getValueFor(nodes(x).value, nodes(y).value, nodes(z).value)
    Node(nextVal, node.inputs)
  }

  def nextIteration: Network = new Network(size, truthTable, nodes map nextNode)

  def configToString: String =
    s"""Truth table:
       |${truthTable.toString}
       |Node connections:
       |$nodesToString""".stripMargin

  def nodesToString: String = ((nodes map {
    _.inputs
  }).zipWithIndex map { case (t, i) =>
    String.format("%2d | %2d, %2d, %2d\n", i, t._1, t._2, t._3)
  }).mkString

  override def toString: String =
    nodes.foldLeft(new StringBuilder)(_ append _).append('\n').toString
}

object Network {
  val defaultSize = 20

  def apply(size: Int = defaultSize, seed: Long): Network = {
    Random.setSeed(seed)

    val nodeConnections: Seq[(Int, Int, Int)] = Seq.fill(size) {
      (Random.nextInt(size), Random.nextInt(size), Random.nextInt(size))
    }

    val nodes: Seq[Node] =
      nodeConnections map (x => Node(Random.nextBoolean(), (x._1, x._2, x._3)))

    new Network(size, TruthTable(), nodes)
  }

  val iterations: Long => LazyList[Network] =
    seed =>
      Network(defaultSize, seed) #:: iterations(seed) map (_.nextIteration)
}
