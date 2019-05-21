# flume－plugin
 包含flume的一些小插件  如：MysqlSink   MyregexInterceptor  tailDirSource

 tailDirSource 支持读取log4j 的日志文件（解决文件切分问题），实现多文件监控，断点续传。

 MysqlSink 支持将采集数据发送到 mysql ，并嵌套了通过http 请求发送数据的代码。
 
 关于代码介绍 查看博客： 

 flume 读取数据存入mysql（http://blog.csdn.net/u012373815/article/details/54098581）
 
 flume 自定义正则过滤器（http://blog.csdn.net/u012373815/article/details/54207972）

