/**
  * Created by yangyibo on 16/11/1.
  */

import scala.math._;


object ScalaApp {
  def main(args: Array[String]) {
    //    var answer=8*2+5;
    //     answer=5;
    //    val an,greeting : String ="安安安";
    //    var sWang="HELLO WORlD!";
    //    sWang= 1.toString();
    //    var yang= 5.to(10);
    //
    //    var yy="99.44".toDouble;
    //    var bb=99.44.toInt;
    //
    //    sWang="yangan".intersect("swang");
    //    println("sWang:"+sWang+"+++++++++"+yang+"------"+yy+"--------"+bb);
    //
    //    println("sWang:"+sWang+answer+an+"杨+++++"+greeting);
    var a, b: Int = 6;
    b += 1;
    println(b + "------" + a.+(b));

    println("hello".apply(1) + "hello" (1));
    //"hello".distinct 去掉字符串中重复的字符
    /*
    sqrt(9) 对9开根号
    pow(2,4) 2的4次方
     */
    println("hello".distinct + "---" + sqrt(9) + "-----" + pow(2, 4));


  }
}
