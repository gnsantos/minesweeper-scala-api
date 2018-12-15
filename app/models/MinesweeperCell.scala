package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

object MinesweeperCell {
  implicit val cellWrites: Writes[MinesweeperCell] = (
    (JsPath \ "content").write[String] and
      (JsPath \ "seen").write[Boolean]
    ) (unlift(MinesweeperCell.unapply))

  implicit val cellReads: Reads[MinesweeperCell] = (
    (JsPath \ "content").read[String] and
      (JsPath \ "seen").read[Boolean]
    ) (MinesweeperCell.apply _)
}

case class MinesweeperCell(content: String, seen: Boolean)
