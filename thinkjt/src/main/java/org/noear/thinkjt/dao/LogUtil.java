package org.noear.thinkjt.dao;


import java.util.Map;

public class LogUtil {
    public static long log(Map<String,Object> data)  {
        return DbApi.do_log(data);
    }

    public static long log(String tag, int level,String summary,String content) {
        return log(tag, null, null, null, level, summary, content);
    }

    public static long log(String tag, String tag1, int level,String summary,String content) {
        return log(tag, tag1, null, null, level, summary, content);
    }

    public static long log(String tag, String tag1, String tag2,int level,String summary,String content) {
        return log(tag, tag1, tag2, null, level, summary, content);
    }

    public static long log(String tag, String tag1,String tag2, String tag3,int level,String summary,String content)  {
        return DbApi.do_log(tag,tag1,tag2,tag3,level,summary,content);
    }
}
