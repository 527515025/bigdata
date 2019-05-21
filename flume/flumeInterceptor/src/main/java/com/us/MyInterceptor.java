package com.us;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.flume.interceptor.RegexFilteringInterceptor.Constants.DEFAULT_REGEX;
import static org.apache.flume.interceptor.RegexFilteringInterceptor.Constants.REGEX;

/**
 * Created by yangyibo on 17/1/6.
 */
public class MyInterceptor  implements Interceptor {
    private final Pattern regex;

    private MyInterceptor(Pattern regex) {
        this.regex = regex;
    }

    @Override
    public void initialize() {
        // NO-OP...
    }

    @Override
    public void close() {
        // NO-OP...
    }
    //    JAVA中用于处理字符串常用的有三个类：
//
//    java.lang.String、
//
//    java.lang.StringBuffer、
//
//    java.lang.StringBuilder，
//
//    这三者的共同之处都是 final 类，不允许被继承，这主要是从性能和安全性上考虑的，因为这几个类都是经常被使用着的，且考虑到防止其中的参数被修改影响到其它的应用。
//
//    StringBuffer 与 StringBuilder 两个基本上差不多，只是 StringBuffer 是线程安全，可以不需要额外的同步用于多线程中；
//
//    StringBuilder 是非同步，运行于多线程中就需要使用着单独同步处理，但是速度就比 StringBuffer 快多了；二者之间的共同点都可以通过append、insert进行字符串的操作。
//
//    String 实现了三个接口：Serializable、Comparable<String>、CharSequence，
//
//    而 StringBuffer 及 StringBuilder 只实现了两个接口 Serializable、CharSequence，相比之下 String 的实例可以通过 compareTo 方法进行比较，而其它两个就不可以。
    @Override
    public Event intercept(Event event) {
        String body = new String(event.getBody(), Charsets.UTF_8);


        //匹配日志信息中以 Parsing events: 为开头关键字,以END 为结尾关键字 的日志信息段
//        String pattern= "(Parsing events)(.*)(END)";
        // 创建 Pattern 对象
//        Pattern r= Pattern.compile(pattern);
        // 现在创建 matcher 对象
//        Matcher m= r.matcher(body);
        Matcher m= regex.matcher(body);
        StringBuffer bodyoutput = new StringBuffer();
        if(m.find())//此处可以用多个正则进行匹配,多条件可以用&& 或者|| 的方式约束连接。
        {
            //多个正则匹配后可以将多个匹配的结果append 到bodyoutput
            bodyoutput=bodyoutput.append(m.group(0));
            event.setBody(bodyoutput.toString().getBytes());
        }else{
            event=null;
        }
        return event;
    }

    @Override
    public List<Event> intercept(List<Event> events) {
        List<Event> intercepted = Lists.newArrayListWithCapacity(events.size());
        for (Event event : events) {
            Event interceptedEvent = intercept(event);
            if (interceptedEvent != null) {
                intercepted.add(interceptedEvent);
            }
        }
        return intercepted;
    }

    public static class Builder implements Interceptor.Builder {
        private Pattern regex;
        //使用Builder初始化Interceptor
        @Override
        public Interceptor build() {
            return new MyInterceptor(regex);
        }

        @Override
        public void configure(Context context) {
            String regexString = context.getString(REGEX, DEFAULT_REGEX);
            regex = Pattern.compile(regexString);
        }
    }
}
