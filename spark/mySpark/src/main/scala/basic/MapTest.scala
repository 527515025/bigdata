package basic
import java.math.BigInteger;

/**
  * @author yyb
  */
object MapTest {
  var yang: String = "1";

  def main(args: Array[String]) {
    var capital = Map("US" -> "Washington", "France" -> "Paris")
    capital += ("Japan" -> "Tokyo")
    capital += ("Shanghai" -> "lianzhan")
    //通过map返回 值
    println(capital("Shanghai"))

    println(factorial(40));

    //调用toInt 方法
    println(toInt("88.91"))

    //调用max 方法
    println("最大的数是:" + max(4, 6))

    //调用无返回值函数
    greet()


  }


  //如果函数仅由一个句子组成,你可以可选地不写大括号
  def max(x: Int, y: Int) = if (x > y) x else y

  def greet() = println("我是一个无返回值函数")

  //: Int”类型标注。这个东 西定义了函数的结果类型
  def toInt(x: String): Int = {
    x.toDouble.toInt + 5;
  }

  def factorial(x: BigInteger): BigInteger = {
    if (x == BigInteger.ZERO)
      BigInteger.ONE
    else
      x.multiply(factorial(x.subtract(BigInteger.ONE)))
  }

  def factorial(x: BigInt): BigInt =
    if (x == 0) 1 else x * factorial(x - 1)
}
