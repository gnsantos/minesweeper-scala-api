package models

import java.util.UUID

import play.api.libs.json._
import play.api.libs.functional.syntax._

object Board {
  implicit val boardWrites: Writes[Board] = (
    (JsPath \ "id").write[UUID] and
      (JsPath \ "cells").write[Seq[MinesweeperCell]] and
      (JsPath \ "rows").write[Int] and
      (JsPath \ "cols").write[Int] and
      (JsPath \ "bombs").write[Int] and
      (JsPath \ "status").write[String]
    ) (unlift(Board.unapply))

  implicit val boardReads: Reads[Board] = (
    (JsPath \ "id").read[UUID] and
      (JsPath \ "cells").read[Seq[MinesweeperCell]] and
      (JsPath \ "rows").read[Int] and
      (JsPath \ "cols").read[Int] and
      (JsPath \ "bombs").read[Int] and
      (JsPath \ "status").read[String]
    ) (Board.apply _)
}

case class Board(id: UUID, cells: Seq[MinesweeperCell], rows: Int, cols: Int, bombs: Int, status: String) {
  def neighborPositions(row: Int, col: Int): Seq[(Int, Int)] = {
    var neighPositions = Seq.empty[(Int, Int)]
    for(i <- -1 to 1) {
      for(j <- -1 to 1) {
        val pos = cols * (row + i) + (col + j)
        if(!(i.equals(0) && j.equals(0)) && (((row + i) >= 0) && ((row + i) < rows)) && (((col + j) >= 0) && ((col + j) < cols)))
          neighPositions ++= Seq((row + i,col + j))
      }
    }

    neighPositions
  }

  def neighbors(row: Int, col: Int): Seq[MinesweeperCell] = {
    neighborPositions(row, col).map {
      case (i, j) => cells(i*cols + j)
    }
  }

  def revealPosition(row: Int, column: Int): Board = {
    val clickedPosition: Int = cols*row + column

    val newCells = cells.zipWithIndex.map {
      case (cell, index) =>
        if (index.equals(clickedPosition)) MinesweeperCell(cell.content, seen=true)
      else cell
    }

    newCells(clickedPosition).content match {
      case "0" =>
        val neighborsAndPositions = neighbors(row, column).zip(neighborPositions(row, column))
        neighborsAndPositions.filter {
          case (cell, _) => !(cell.content == "B" || cell.seen)
        }.foldLeft(this.copy(cells = newCells)) {
          case (b, (_, (r,c))) => b.revealPosition(r, c)
        }
      case _ =>  this.copy(cells = newCells)
    }
  }

  def revealBombs(): Board = {
    val newCells = cells.map {
      cell =>
        cell.content match {
          case "B" => MinesweeperCell(cell.content, seen = true)
          case _ => cell
        }
    }
    this.copy(cells = newCells, status = "lost")
  }
}
