import scala.collection.mutable.ArrayBuffer

/**
  * Created by yangyibo on 16/11/23.
  *
  * LIST  AND  foreach
  */
object MyList {
  def main(args: Array[String]) {


    //    mapDemo()
    //    list()
    //    listString()
    //    setDemo()
    //    seqString()
    //    formatArgs(Array("yang", "yi", "bo"))

    val arrayBuffer = ArrayBuffer("one", "two")
    arrayBuffer.append("three")
    arrayBuffer.insert(1,"zero")
    arrayBuffer.foreach(println)
    println("------------------------")
    val numNames2 = Array.apply("zero", "one", "two")
    numNames2(2) = "three"
    numNames2.foreach(println)
  }

  def mapDemo(): Unit = {
    import scala.collection.mutable.Map
    //val默认是不能使用+= 但是引入了如上声明
    val capital = Map("US" -> "Washington", "France" -> "Paris")
    capital += ("Japan" -> "Tokyo")
    capital += ("Shanghai" -> "lianzhan")
    println("Shanghai :" + capital("Shanghai"))
  }

  //更加函数式编程
  def formatArgs(args: Array[String]) = println(args.mkString("\n"))


  def pair(): Unit = {
    var tuple = (100, "an", 10.342323, true, 'a')
    println(tuple._3)
  }

  def seqString(): Unit = {
    var seq = Seq("a")
    for (i <- 1 to 10)
      seq = seq :+ ",b" + i.toString

    print("sqe:")
    seq.foreach(print)
    println("--------------------------")

  }


  def setDemo(): Unit = {

    var set1 = Set("a", "b")
    set1 += "c"
    println("set:" + set1)

  }


  def listString(): Unit = {
    val listString = "a" :: "b" :: "c" :: Nil
    println(listString)


    val onelistString = "d" :: listString
    for (i <- onelistString)
      println(i)


    val towlistString = listString ::: onelistString
    towlistString.foreach(println)


    var threeListString = "a"
    for (i <- 1 to 10)
      threeListString += " ,b" + i.toString
    print("threeListString:")
    threeListString.foreach(print)

    println("------------------------------")
  }


  def list(): Unit = {
    //Nil 定义一个空的list
    // :: 添加一个元素
    // ::: 合并两个数组
    val list = 1 :: Nil
    println(list)


    println("----------------onelist")
    val onelist = List(2, 3)
    println("onelist:" + onelist)
    for (arg <- onelist)
      println(arg)


    println("----------------twolist")
    val twolist = 1 :: onelist
    println("towlist:" + twolist)
    for (i <- 0 to 2)
      println(twolist(i))


    println("----------------threelist")
    val threelist = onelist ::: twolist
    println("threelist:" + threelist)
    threelist.foreach(println)
  }
}
