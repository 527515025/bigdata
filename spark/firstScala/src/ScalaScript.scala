///**
//  * Created by yangyibo on 16/11/15.
//  */
//println("我是一个脚本,请在终端执行我,")
//
//println("0  -------------------------------------")
////当作脚本执行的一条语句,脚本执行语句  scala ScalaScript.scala AN  YANG
////planet 为args(0) 参数
//println("我是第一个参数脚本," + args(0) + ";我是第二个参数" + args(1) + "!")
//
//println()
//println("1  -------------------------------------")
//println()
//
////脚本输入 scala ScalaScript.scala Scala is fun
////我是脚本里的while 语句
////Scala 和 Java 一样,必须把 while 或 if 的布尔表达式放在括号里
//var i = 0
//while (i < args.length) {
//  print(args(i))
//  i += 1
//}
//
//println()
//println("2  -------------------------------------")
//println()
////函数式编程遍历
////args.foreach(arg=>println(arg))
////显式遍历写法
////args.foreach((arg:String )=> println(arg))
////简洁写法
//args.foreach(println)
//
//println()
//println("3  -------------------------------------")
//println()
////熟悉的for表达式
//for (arg <- args)
//  print(arg)
//
//println()
//println("4  -------------------------------------")
//println()
//
//
//
//
////带类型的参数话数组
//val greetStringsAn = new Array[String](3)
////更加显式的书写
////val greetStringsAn : Array[String]=new Array[String](3)
////简洁的写法 定义了一个字符串数组,和三个元素
////val greetStringsAn =Array("你好",",","世界")
//
//greetStringsAn(0) = "你好"
//greetStringsAn(1) = ","
//greetStringsAn(2) = "世界"
//for (i <- 0 to 2)
//  print(greetStringsAn(i))
//
//
//
//println()
//println("5  -------------------------------------")
//println()
//
//
