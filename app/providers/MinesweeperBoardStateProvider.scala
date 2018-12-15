package providers

import java.sql.Connection
import java.util.UUID

import models.{Board, MinesweeperCell}

class MinesweeperBoardStateProvider extends ConnectionProvider {
  val schema = "minesweeper"
  val table = "boardstate"

  def createFirstBoarState(board: Board) = {
    val conn = createConnection()
    val statement = conn.createStatement()

    val encodedBoard = board.cells.map(_.content).mkString("")
    val currentBoard = board.cells.map {
      cell => cell.seen match {
        case true => cell.content
        case false => "?"
      }
    }.mkString("")

    val sql = s"INSERT INTO ${schema}.${table} VALUES ('${board.id}', ${board.rows}, ${board.cols}, '${encodedBoard}', '${currentBoard}', ${board.bombs}, '${board.status}')"
    statement.execute(sql)
    conn.close()
  }

}
