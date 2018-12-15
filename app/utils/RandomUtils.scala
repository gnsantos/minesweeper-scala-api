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

  def randomBoard(): Board = {
    val numRows = RandomUtils.intWithin(10, 20)
    val numCols = RandomUtils.intWithin(10, 20)
    val numBombs = RandomUtils.intWithin(20, 40)
    val id = UUID.randomUUID()
    val status = "ongoing"
    val cells = (0 until numCols*numRows).map(_ => MinesweeperCell("0", false))

    val cellsWithBombs = insertBombs(numBombs, numRows, numCols, cells)

    val boardWithBombs = Board(id, cellsWithBombs, numRows, numCols, numBombs, status)

    val hintValues = for(i <- 0 until numRows; j <- 0 until numCols) yield {
      val neighbors = boardWithBombs.neighbors(i,j)
      val bombsNear = neighbors.count(n => n.content.equals("B"))
      bombsNear
    }

    val bombsAndHints = cellsWithBombs.zip(hintValues).map {
      case (cell, hint) =>
        cell.content match {
          case "B" => cell
          case _ => MinesweeperCell(hint.toString, false)
        }
    }

    Board(id, bombsAndHints, numRows, numCols, numBombs, status)
  }
}
