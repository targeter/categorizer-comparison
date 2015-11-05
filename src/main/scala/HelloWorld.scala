package com.example.categorizercomparison

import java.io.File

import scala.xml.{Node, XML}


object HelloWorld {
  val path = "/Users/sietse/repos/cwc/cwc-config/hulk/categorizers"

  def main(args: Array[String]) {
    val master = new File(s"$path/categorizers.xml")
//    val test = new File(s"$path/sbox2-test/categorizers.xml")
//    val functest = new File(s"$path/sbox2-acceptance/categorizers.xml")
//    val acc = new File(s"$path/sdujurisprudentie-acceptatie/categorizers.xml")
//    val prod = new File(s"$path/sdujurisprudentie/categorizers.xml")

//    val cats = parseCategorizers(new File("/Users/sietse/cats.xml"))
    val cats = parseCategorizers(new File(s"/Users/sietse/Downloads//categorizers (7).xml"))

    val masterResults = parseCategorizers(master).toSeq
//    val testResults = parseCategorizers(test).toSeq
//    val functestResults = parseCategorizers(functest).toSeq
//    val accResults = parseCategorizers(acc).toSeq
//    val prodResults = parseCategorizers(prod).toSeq

    println("master -> cats: ")
    masterResults.diff(cats).foreach(println)
//    masterResults.diff(testResults).foreach(println)
    println("cats -> master: ")
    cats.diff(masterResults).foreach(println)
//    testResults.diff(masterResults).foreach(println)
//
//    println("master -> functest: ")
//    masterResults.diff(functestResults).foreach(println)
//    println("functest -> master: ")
//    functestResults.diff(masterResults).foreach(println)
//
//    println("master -> acc: ")
//    masterResults.diff(accResults).foreach(println)
//    println("acc -> master: ")
//    accResults.diff(masterResults).foreach(println)

//    testResults.diff(masterResults).foreach(println)
//    println("===========")
//    masterResults.diff(testResults).foreach(println)







  }

  def duplicates(categorizers: Seq[String]) = {
    categorizers.groupBy(identity).collect { case (x, ys) if ys.size > 1 => x }
  }

  def parseCategorizers(file: File): Seq[String] = {
    val xml = XML.loadFile(file)

    val categorizers = (xml \ "categorizer").map(categorizer => categorizer \@ "name" -> categorizer \ "category").toMap

    def foldCategory(previous: String, category: Node): Seq[String] = {
      val value = category \@ "value"
      val current = s"$previous$value"
      val subCats = category \ "category"
      if (subCats.isEmpty) {
        Seq(current)
      } else {
        subCats.flatMap(foldCategory(current + "/", _))
      }
    }

    categorizers.flatMap {
      case (categorizer, categories) =>
      categories.flatMap(foldCategory(s"$categorizer::", _)).sorted
    }.toSeq


  }
}

