package org.workspace13.genetics.geneticbasics1

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers
import org.workspace13.genetics.geneticbasic1.Genetics

class GeneticsSpec extends FlatSpec with ShouldMatchers with GivenWhenThen {

  val genetics = new Genetics(15)

  def testChromosome(chromosome: (Option[Int], String)) = {
    chromosome._2.size should equal(48)
    chromosome._2.toIterable.count(c => c == '0' || c == '1') should equal(48)
  }

  "makeChromosome" should "return a string of 48 zero and one" in {
    val chromosome = genetics.makeChromosome

    testChromosome(chromosome)
  }

  "makePopulation" should "return a population of 100 chromosomes" in {
    val population = genetics.makePopulation

    population should have size 100
    population foreach testChromosome
  }

  "chromosomeToGene" should "return 12 genes" in {
    val chromosome = genetics.makeChromosome
    val genes = genetics.chromosomeToGene(chromosome)

    genes should have size 12
  }
  it should "return 12 genes whose the sequence is the same as the given chromosome" in {
    val chromosome = genetics.makeChromosome
    val genes = genetics.chromosomeToGene(chromosome)

    genes.mkString("") should equal(chromosome._2)
  }

  "geneToFormula" should "return the right formula" in {
    val genes = List("0000", "0010", "1010", "1101", "1000")
    Given(s"the genes $genes")

    When("the geneToFormula is applied")
    val formula = genetics.geneToFormula(genes)

    Then("the result should be '02+%8'")
    formula should equal("02+%8")
  }

  "evaluation" should "return a given int when the sequence of genes is an expression" in {
    val genes = List("0010", "1010", "1000")
    Given(s"the genes $genes")

    When("the evaluation method is applied")
    val result = genetics.evaluation(genes)

    Then("the result should be 5")
    assert(result == genetics.value - 10)
  }

  it should "return " + Int.MaxValue + " when the sequence of genes is not an expression" in {
    val genes = List("0000", "0010", "1010", "1101", "1000")
    Given(s"the genes $genes")

    When("the evaluation method is applied")
    val result = genetics.evaluation(genes)

    Then("the result should be " + Int.MaxValue)
    assert(result == Int.MaxValue)
  }

  "scorePopulation" should "return a population whose each individual has a score" in {
    val population = genetics.makePopulation
    val scoredPopulation = genetics.scorePopulation(population)

    scoredPopulation.foreach(individual => assert(individual._1.isDefined))
  }

  "selection" should "return a population of 50 individuals" in {
    val population = genetics.makePopulation
    val selectedPopulation = genetics.selection(population)

    assert(selectedPopulation.size == 50)
  }

  "crossover" should "return two children with a mix of their parents chromosomes and no score" in {
    val parent1 = genetics.makeChromosome
    val parent2 = genetics.makeChromosome

    val children = genetics crossover(parent1, parent2)

    assert(children.size == 2)

    val child1 = children head
    val child2 = children.tail head

    // Probably not the most efficient way…
    val crossPoint = (parent1._2 zip children.head._2) segmentLength(c => c._1 == c._2, 0)

    assert(child1._1 == None)
    assert(child1._2 == parent1._2.take(crossPoint) + parent2._2.drop(crossPoint))

    assert(child2._1 == None)
    assert(child2._2 == parent2._2.take(crossPoint) + parent1._2.drop(crossPoint))
  }

  "nextGeneration" should "return a new population of 100 individuals" in {
    val population = genetics.makePopulation
    val nextGen = genetics.nextGeneration(population)

    assert(nextGen.size == 100)
  }

  "search" should "return an approximation result" in {
    val best = genetics.search(987)
    assert(best.isDefined && best.get < 10)
  }
}

