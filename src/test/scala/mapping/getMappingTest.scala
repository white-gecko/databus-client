package mapping

import org.apache.jena.query.{Query, QueryExecution, QueryExecutionFactory, QueryFactory, QuerySolution, ResultSet}
import org.apache.jena.rdf.model.{Model, ModelFactory}
import org.scalatest.FlatSpec

class getMappingTest extends FlatSpec{
 "mappingInfo" should "be interpreted right to convert related csv" in {

   val mapInfo = "/home/eisenbahnplatte/git/format-mappings/tarql/1.ttl"

   val mapInfoModel =ModelFactory.createDefaultModel().read(mapInfo)

   val sparqlVar = "?mappingFile"
   val queryString =

   s"""
     |PREFIX tmp: <http://tmp-namespace.org/>
     |
     |SELECT DISTINCT $sparqlVar
     |WHERE {
     |<$mapInfo#this> tmp:hasMappingFile $sparqlVar .
     |}
     |""".stripMargin

   val result = executeMapQuery(queryString, mapInfoModel, sparqlVar)

   result.foreach(println(_))
 }

  def executeMapQuery(queryString: String, model:Model, sparqlVar:String): Seq[String] = {

    val query: Query = QueryFactory.create(queryString)
    //    println("\n--------------------------------------------------------\n")
    //    println(s"""Query:\n\n${query.toString()} """)

    val qexec: QueryExecution = QueryExecutionFactory.create(query,model)

    var filesSeq: Seq[String] = Seq[String]()

    try {
      val results: ResultSet = qexec.execSelect
      while (results.hasNext) {
        val resource = results.next().getResource(sparqlVar)
        filesSeq = filesSeq :+ resource.toString
      }
    } finally qexec.close()

    filesSeq
  }


  "executeQuery" should "be generic" in {

    val mapInfo = "/home/eisenbahnplatte/git/format-mappings/tarql/1.ttl"

    val mapInfoModel =ModelFactory.createDefaultModel().read(mapInfo)

    val sparqlVar = "?mappingFile"
    val mapComment = "?comment"
    val label = "?label"

    val varSeq = Seq(sparqlVar, label, mapComment)

    val queryString =
      s"""
         |PREFIX tmp: <http://tmp-namespace.org/>
         |PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
         |
         |SELECT DISTINCT *
         |WHERE {
         |<$mapInfo#this> tmp:hasMappingFile $sparqlVar ;
         |rdfs:label $label;
         |rdfs:comment $mapComment .
         |}
         |""".stripMargin

    val result = executeQuery(queryString, mapInfoModel, varSeq)

    result.foreach(println(_))
  }

  def executeQuery(queryString: String, model:Model = ModelFactory.createDefaultModel(), sparqlVars:Seq[String]): Seq[QuerySolution] = {

    val query: Query = QueryFactory.create(queryString)
    val qexec: QueryExecution = QueryExecutionFactory.create(query,model)

    var resultSeq: Seq[QuerySolution] = Seq.empty

    try {
      val results: ResultSet = qexec.execSelect
      while (results.hasNext) {
        val result = results.next()
        resultSeq = resultSeq :+ result
      }
    } finally qexec.close()

    resultSeq
  }

  "model" should "be empty" in {
    val model:Model = ModelFactory.createDefaultModel()

    println(model.isEmpty)
    assert(model.isEmpty)
  }
}
