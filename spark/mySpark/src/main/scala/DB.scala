import java.sql.{Connection, DriverManager, ResultSet}

/**
  * Created by yangyibo on 16/11/23.
  */
object DB {

  def main(args: Array[String]) {
    val user = "root"
    val password = "admin"
    val host = "localhost"
    val database = "msm"
    val conn_str = "jdbc:mysql://" + host + ":3306/" + database + "?user=" + user + "&password=" + password
    println(conn_str)
    val conn = connect(conn_str)
    val statement =conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    // Execute Query
    val rs = statement.executeQuery("select * from sec_user")
    // Iterate Over ResultSet
    while (rs.next) {
      // 返回行号
      // println(rs.getRow)
      val name = rs.getString("name")
      println(name)
    }
    closeConn(conn)
  }

  def connect(conn_str: String): Connection = {
    //classOf[com.mysql.jdbc.Driver]
    Class.forName("com.mysql.jdbc.Driver").newInstance();
    return  DriverManager.getConnection(conn_str)
  }

  def closeConn(conn:Connection): Unit ={
    conn.close()
  }

}
