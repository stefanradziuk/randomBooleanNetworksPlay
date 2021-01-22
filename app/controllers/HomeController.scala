package controllers

import javax.inject._
import play.api.mvc._
import play.twirl.api.Html
import models._

import scala.util.Random

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  private val initialIterations = 100
  private val onLoadIterations = 50

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(seed: Option[Long] = None): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index(genNetworkTable(seed.getOrElse(Random.nextLong()))))
  }

  private def genNetworkTable(seed: Long): Html = {
    println(s"Generating an rbn from seed: $seed")
    val hiddenInput = s"""<input type="hidden" id="seed" value="$seed">"""
    val tableContent = networksToHtml(Network.iterations(seed))
    Html(s"""$hiddenInput<table id="rbn-table"><tbody id = "rbn-tbody">$tableContent</tbody></table>""")
  }

  private def nodeToCell(node: Node): String =
    s"""<td class="${if (node.value) "full" else "empty"}"></td>"""

  private def networkToRow(network: Network): String =
    s"<tr>${(network.nodes map nodeToCell).mkString}</tr>"

  def networksToHtml(networks: LazyList[Network], iterations: Int = initialIterations, drop: Int = 0): String = {
    (networks.slice(drop, drop + iterations).toList map networkToRow).mkString
  }


  private val sampleNetworkTable: Html = Html(
    """
      |    <tr>
      |      <td></td>
      |      <td></td>
      |      <td></td>
      |    </tr>
      |    <tr>
      |      <td></td>
      |      <td class="full"></td>
      |      <td></td>
      |    </tr>
      |""".stripMargin)

  def loadMore(seed: Long, count: Int): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    println(s"loadMore hit with seed: $seed, count: $count")
    Ok(networksToHtml(Network.iterations(seed), iterations = onLoadIterations, drop = count))
  }
}
