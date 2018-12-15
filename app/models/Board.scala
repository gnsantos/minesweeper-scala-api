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

case class Board(id: UUID, cells: Seq[MinesweeperCell], rows: Int, cols: Int, bombs: Int, status: String)
