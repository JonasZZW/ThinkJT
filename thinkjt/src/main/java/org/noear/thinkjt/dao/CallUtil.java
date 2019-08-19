package org.noear.thinkjt.dao;

import org.noear.thinkjt.utils.TextUtils;
import org.noear.solon.core.XContext;

import java.util.ArrayList;

public class CallUtil {

    private static Object do_call(String path, boolean asApi) throws Exception {
        String path2 = path;//不用转换*
        String name = path2.replace("/", "__");

        AFileModel file = AFileUtil.get(path2);

        if (file.file_id > 0 && TextUtils.isEmpty(file.content) == false) {
            return ExcUtil.call(name, file, XContext.current(), asApi);
        } else {
            return "";
        }
    }

    /**
     * 调用文件
     * */
    public static Object callFile(String path) throws Exception {
        return do_call(path,false);
    }

    /**
     * 调用勾子。勾子调用不能出错，以免影响主业务
     */
    public static String callHook(String tag,String label, boolean isCache) {
        if(TextUtils.isEmpty(tag) && TextUtils.isEmpty(label)){
            return "";
        }

        StringBuilder sb = new StringBuilder();

        try {
            DbApi.fileGetPaths(tag,label,isCache)
                    .forEach((f) -> {
                        try {
                            Object tmp = do_call(f.path, true);
                            if (tmp != null) {
                                sb.append(tmp.toString()).append("\r\n");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return sb.toString();
    }
}
