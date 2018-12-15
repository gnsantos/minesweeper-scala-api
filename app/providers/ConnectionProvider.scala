package providers

import java.sql.DriverManager
import java.sql.Connection

import play.Play._

abstract class ConnectionProvider {
  lazy val config = play.Play.application.configuration

  def createConnection(): Connection = {
    val driver = config.getString("db.default.driver")
    val url = config.getString("db.default.url")
    val username = config.getString("db.default.user")
    val passwd = config.getString("db.default.password")

    Class.forName(driver)
    val conn = DriverManager.getConnection(url, username, passwd)

    conn
  }

}
