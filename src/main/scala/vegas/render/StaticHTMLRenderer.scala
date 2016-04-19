package vegas.render

import vegas.DSL.SpecBuilder
import vegas.spec.Spec

/**
  * @author Aish Fenton.
  */
case class StaticHTMLRenderer(spec: Spec) extends BaseHTMLRenderer {

  def HTMLImports(additionalImports: String*) = (JSImports ++ additionalImports).map { s => "<script src=\"" + s + "\" charset=\"utf-8\"></script>" }

  def HTMLHeader(additionalImports: String*) =
    s"""
       |<html>
       |  <head>
       |    ${ HTMLImports(additionalImports:_*) }
       |  </head>
       |  <body>
    """.stripMargin

  def HTMLChart(name: String, spec: Spec, pretty: Boolean = false) =
    s"""
       | <script>
       |   var embedSpec = {
       |     mode: "vega-lite",
       |     spec: ${spec.toJson(pretty)}
       |   }
       |   vg.embed("#$name", embedSpec, function(error, result) {});
       | </script>
       | <div id='$name'></div>
    """.stripMargin

  val HTMLFooter =
    """
      |  </body>
      |</html>
    """.stripMargin

  def HTMLPage(pretty: Boolean = false) = {
    HTMLHeader().trim + HTMLChart("vis", spec, pretty) + HTMLFooter.trim
  }

}

object StaticHTMLRenderer {

  implicit def toStaticHTMLRenderer(sb: SpecBuilder): StaticHTMLRenderer = { new StaticHTMLRenderer(sb.spec) }

}