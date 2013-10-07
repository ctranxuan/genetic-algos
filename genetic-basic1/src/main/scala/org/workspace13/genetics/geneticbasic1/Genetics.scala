package org.workspace13.genetics.geneticbasic1

import scala.util.Random
import com.twitter.util.Eval
import scala.math._
import org.workspace13.genetics.{Population, Chromosome}
import scala.annotation.tailrec

class Genetics(val value: Int) {


  def makeChromosome(): Chromosome = {
    val chromosome = (1 to 48 map {
      i => Random.nextInt(2).toString
    })

    (None, chromosome.mkString)
  }

  def makePopulation(): Population = {
    for (i <- List.range(1, 101)) yield {
      makeChromosome()
    }
  }

  def chromosomeToGene(chromosome: Chromosome): Seq[String] = {
    (chromosome._2 grouped 4).toSeq
  }

  def geneToOperand(gene: String): String = {
    require(gene.size == 4, "gene must have a size of 4 and not " + gene.size)

    gene match {
      case "0000" => "0"
      case "0001" => "1"
      case "0010" => "2"
      case "0011" => "3"
      case "0100" => "4"
      case "0101" => "5"
      case "0110" => "6"
      case "0111" => "7"
      case "1000" => "8"
      case "1001" => "9"
      case "1010" => "+"
      case "1011" => "-"
      case "1100" => "/"
      case "1101" => "%"
      case _ => ""
    }
  }

  def geneToFormula(genes: Seq[String]): String = {
    genes map (gene => geneToOperand(gene)) mkString ""
  }

  def evaluation(genes: Seq[String]): Int = {
    val formula = geneToFormula(genes)

    try {
      val eval: Int = (new Eval).apply[Int](formula)
      abs(value - eval)

    } catch {
      case e: Exception => Int.MaxValue
    }
  }

  def scorePopulation(population: Population): Population = {
    population.map(individual => (Some(evaluation(chromosomeToGene(individual))), individual._2))
  }

  def selection(population: Population): Population = {
    Random.shuffle(population.take(50))
  }

  def crossover(parent1: Chromosome, parent2: Chromosome): Population = {
    val random = Random.nextInt(48)

    val child1 = (None, parent1._2.take(random) ++ parent2._2.drop(random))
    val child2 = (None, parent2._2.take(random) ++ parent1._2.drop(random))

    child1 :: child2 :: Nil
  }

  def nextGeneration(population: Population): Population = {
    require(population.size % 2 == 0, f"population [#${population.size}] must have a pair number of individuals")

    @tailrec
    def buildNewGen(pop: Population, acc: Population): Population = {
      pop match {
        case Nil => acc
        case List(_) => acc
        case x :: y :: tail => {
          val children1 = crossover(x, y)
          val children2 = crossover(x, y)
          buildNewGen(tail, children1 ++ children2 ++ acc)
        }
      }
    }

    buildNewGen(selection(population), Nil)
  }

  def search(value: Int) = {
    val population = makePopulation
    val limit = 50

    def searchNext(pop: Population, generation: Int): Option[Int] = {
      val scoredPop = scorePopulation(pop) sortWith ((c1, c2) => c1._1.getOrElse(Int.MaxValue) < c2._1.getOrElse(Int.MaxValue))
      val best = scoredPop.head._1
      val step = limit - generation + 1

      println(s"Generation: $step Best: $best")
      if (best.getOrElse(Int.MaxValue) == 0 || generation == 0) best
      else searchNext(nextGeneration(scoredPop), generation - 1)
    }

    searchNext(population, limit)
  }

}

