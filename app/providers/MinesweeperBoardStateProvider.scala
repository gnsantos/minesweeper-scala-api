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

  def getBoardState(id: UUID): Board  = {
    val conn = createConnection()
    val statement = conn.createStatement()
    val state = statement.executeQuery(s"SELECT * FROM ${schema}.${table} WHERE id = '${id}';")
    state.next()
    val encodedBoard = state.getString("encoded_board").split("")
    val currentBoard = state.getString("current_board").split("")

    val cells = encodedBoard.zip(currentBoard).map {
      case (content, displayed) =>
        val seen = displayed match {
          case "?" => false
          case _ => true
        }

        MinesweeperCell(content, seen)
    }

    conn.close()

    Board(id, cells, state.getInt("rows"), state.getInt("cols"),
          state.getInt("bombs"), state.getString("game_status"))
  }

  def updateBoardState(id: UUID, newBoard: Board) = {
    val conn = createConnection()
    val statement = conn.createStatement()

    val currentBoard = newBoard.cells.map {
      cell => cell.seen match {
        case true => cell.content
        case false => "?"
      }
    }.mkString("")

    val sql = s"UPDATE ${schema}.${table} SET current_board = '${currentBoard}' WHERE id = '${id}'; "

    statement.execute(sql)
    conn.close()
  }

}
