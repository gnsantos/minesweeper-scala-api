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

  def insertBombs(numBombs: Int, rows: Int, cols: Int, cells: Seq[MinesweeperCell]): Seq[MinesweeperCell] = {
    if(numBombs.equals(0)) {
      return cells
    }

    val bombPosition = randomBoardPosition(rows, cols)
    var placedBombs = 0

    val newCells = cells.zipWithIndex.map {
      case (cell, index) =>
        if(index.equals(bombPosition) && cell.content.ne("B")){
          placedBombs = 1
          MinesweeperCell("B", false)
        }
        else cell
    }

    insertBombs(numBombs - placedBombs, rows, cols, newCells)
  }
}
