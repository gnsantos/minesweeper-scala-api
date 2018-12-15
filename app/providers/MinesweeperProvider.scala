package controllers

import javax.inject._
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

}
