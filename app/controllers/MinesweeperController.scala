package controllers

import java.util.UUID

import javax.inject._
import models.{Board, MinesweeperMovePostRequest, MinesweeperCell}

import play.api.libs.json._
import play.api.mvc._

import utils.RandomUtils

import providers.MinesweeperBoardStateProvider

@Singleton
class MinesweeperController @Inject()(cc: ControllerComponents, stateProvider: MinesweeperBoardStateProvider) extends AbstractController(cc) {

  def startBoard() = Action { implicit req: Request[AnyContent] =>
    val board = RandomUtils.randomBoard()
    stateProvider.createFirstBoarState(board)
    Ok(Json.toJson(board))
  }

  def move() = Action(parse.json) { implicit request: Request[JsValue] =>
    val body = request.body
    body.validate[MinesweeperMovePostRequest] match {
      case JsError(errors) =>
        val errorJson = JsObject(Seq("message" -> JsString("Invalid Request")))
        BadRequest(errorJson)
      case JsSuccess(value, _) =>
        val boarId = value.boardId

        val board: Board = stateProvider.getBoardState(boarId)

        val newBoard = board.cells(value.row*board.cols + value.column).content match {
          case "B" => board.revealBombs()
          case _ => board.revealPosition(value.row, value.column)
        }
        stateProvider.updateBoardState(boarId, newBoard)
        Ok(Json.toJson(newBoard))
    }
  }

}
