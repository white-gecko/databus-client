package org.dbpedia.databus.filehandling.convert.format

import better.files.File
import org.apache.jena.graph.Triple
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.dbpedia.databus.filehandling.FileUtil
import org.dbpedia.databus.filehandling.convert.format.csv.CSVHandler
import org.dbpedia.databus.filehandling.convert.format.rdf.RDFHandler
import org.slf4j.LoggerFactory

object Converter {

  def convertFormat(inputFile: File, inputFormat: String, outputFormat: String): File = {

    val spark = SparkSession.builder()
      .appName(s"Triple reader  ${inputFile.name}")
      .master("local[*]")
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .getOrCreate()

    val sparkContext = spark.sparkContext
    sparkContext.setLogLevel("WARN")

    val tempDir = File("./target/databus.tmp/temp/")
    if (tempDir.exists) tempDir.delete()

    val targetFile: File = tempDir / inputFile.nameWithoutExtension.concat(s".$outputFormat")
    var mappingFile = File("")

    if (EquivalenceClasses.RDFTypes.contains(outputFormat)){

      val triples = {
        if (EquivalenceClasses.RDFTypes.contains(inputFormat)) {
          RDFHandler.readRDF(inputFile, inputFormat, spark: SparkSession)

        }
        else{ // if (EquivalenceClasses.CSVTypes.contains(inputFormat)){
          RDFHandler.readRDF(inputFile, inputFormat, spark: SparkSession)
        }
      }

      RDFHandler.writeRDF(tempDir, triples, outputFormat, spark)
    }

    else if (EquivalenceClasses.CSVTypes.contains(outputFormat)) {
      if (EquivalenceClasses.CSVTypes.contains(inputFormat)) {
        val data: DataFrame = CSVHandler.read(inputFile, inputFormat, spark: SparkSession)
        CSVHandler.write(tempDir, data, outputFormat, spark)
      }
      else {
        val triples = RDFHandler.readRDF(inputFile, inputFormat, spark: SparkSession)
        mappingFile = CSVHandler.writeTriples(tempDir, triples, outputFormat, spark)
      }
    }

    try {
      FileUtil.unionFiles(tempDir, targetFile)
      if (mappingFile.exists) {
        val mapDir = File("./mappings/")
          mapDir.createDirectoryIfNotExists()
        mappingFile.moveTo(mapDir / FileUtil.getSha256(targetFile), overwrite = true)
      }
    }
    catch {
      case _: RuntimeException => LoggerFactory.getLogger("UnionFilesLogger").error(s"File $targetFile already exists") //deleteAndRestart(inputFile, inputFormat, outputFormat, targetFile: File)
    }

    targetFile
  }

}
