package org.workspace13

/**
 * Created with IntelliJ IDEA.
 * User: ctranxuan
 * Date: 10/6/13
 * Time: 10:27 AM
 * To change this template use File | Settings | File Templates.
 */
package object genetics {
  type Chromosome = (Option[Int], String)
  type Population = List[Chromosome]
}
