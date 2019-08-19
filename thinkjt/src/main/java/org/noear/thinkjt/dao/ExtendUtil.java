package org.noear.thinkjt.dao;


import org.noear.thinkjt.utils.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ExtendUtil {
    private static String _path;
    private static File _file;
    public static void init(String path){
        _path = path;
        _file = new File(_path);
    }

    /** 如果是目录的话，只处理一级 */
    public static List<String> scan() {
        List<String> list = new ArrayList<>();

        if (_file.exists()) {
            if (_file.isDirectory()) {
                File[] tmps = _file.listFiles();
                for (File tmp : tmps) {
                    list.add(tmp.getName());
                }
            }
        }

        Collections.sort(list, Comparator.comparing(m -> m));
        return list;
    }

    public static boolean del(String name){
        if(TextUtils.isEmpty(name)){
            return false;
        }

        if(name.endsWith(".jar") == false){
            return false;
        }

        if (_file.exists()) {
            if (_file.isDirectory()) {
                File[] tmps = _file.listFiles();
                for (File tmp : tmps) {
                    if(name.equals(tmp.getName())){
                        tmp.delete();
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
