package models

import java.util.UUID
import play.api.libs.json._
import play.api.libs.functional.syntax._

object MinesweeperMovePostRequest {
  implicit val moveRequestWrites: Writes[MinesweeperMovePostRequest] = (
    (JsPath \ "board_id").write[UUID] and
    (JsPath \ "row").write[Int] and
      (JsPath \ "col").write[Int]
    )(unlift(MinesweeperMovePostRequest.unapply))

  implicit val moveRequestReads: Reads[MinesweeperMovePostRequest] = (
    (JsPath \ "board_id").read[UUID] and
    (JsPath \ "row").read[Int] and
      (JsPath \ "col").read[Int]
    )(MinesweeperMovePostRequest.apply _)

}

case class MinesweeperMovePostRequest(boardId: UUID, row: Int, column: Int)
