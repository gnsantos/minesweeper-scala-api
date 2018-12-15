package utils

import java.util.UUID

import models.{Board, MinesweeperCell}

import scala.util.Random

object RandomUtils {
  lazy val r = new Random

  def intWithin(min: Int, max: Int): Int = {
    min + r.nextInt( (max - min) + 1 )
  }

  def randomBoardPosition(rows: Int, cols: Int): Int = {
    val row = r.nextInt(rows)
    val col = r.nextInt(cols)

    row*cols + col
  }
  
}
