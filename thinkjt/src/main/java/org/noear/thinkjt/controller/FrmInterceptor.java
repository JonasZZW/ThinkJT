package org.noear.thinkjt.controller;

import org.noear.thinkjt.Config;
import org.noear.thinkjt.dao.*;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XHandler;
import org.noear.thinkjt.utils.ExceptionUtils;
import org.noear.thinkjt.utils.TextUtils;

import java.util.HashMap;
import java.util.List;

/**
 * 文件路径拦截器的代理（数据库安全）
 * */
public class FrmInterceptor implements XHandler {
    private static final FrmInterceptor _g = new FrmInterceptor();
    public static FrmInterceptor g(){
        return  _g;
    }


    private FrmInterceptor(){
        reset();
    }


    @Override
    public void handle(XContext ctx) throws Exception {
        String path = ctx.path();

        for (String p1 : _cacheMap.keySet()) {
            if (path.startsWith(p1)) {
                exec(ctx, _cacheMap.get(p1));
                return;
            }
        }
    }
    private void exec(XContext ctx, String path2) throws Exception {
        AFileModel file = AFileUtil.get(path2);

        if (file.file_id == 0) {
            return;
        }

        //不支持路径代理，跳过
        if (Config.filter_path.equals(file.label) == false) {
            return;
        }

        String name = path2.replace("/", "__");

        try {
            JsxUtil.g().runApi(name, file, true);
        }catch (Exception ex) {
            String err = ExceptionUtils.getString(ex);
            ctx.output(err);
            LogUtil.log("_file", file.tag, file.path, 0, "", err);

            ctx.setHandled(true);
        }
    }

    private HashMap<String,String> _cacheMap = new HashMap<>();
    public void del(String note) {
        if (TextUtils.isEmpty(note)) {
            return;
        }

        String suf = note.split("#")[0];
        if (suf.length() > 0) {
            if (suf.startsWith("/")) {
                _cacheMap.remove(suf);
            } else {
                _cacheMap.remove("/" + suf);
            }
        }
    }
    public void add(String path, String note){
        if(TextUtils.isEmpty(note)){
            return;
        }

        String suf = note.split("#")[0];

        if (suf.length()>0) {
            if (suf.startsWith("/")) {
                _cacheMap.put(suf, path);
            } else {
                _cacheMap.put("/" + suf, path);
            }
        }
    }

    public void reset() {
        if (DbUtil.db() == null) {
            return;
        }

        try {
            _cacheMap.clear();

            List<AFileModel> list = DbApi.pathFilters();
            for (AFileModel c : list) {
                if(TextUtils.isEmpty(c.note)){
                    continue;
                }

                String suf = c.note.split("#")[0];

                if (suf.length()>0) {
                    if (suf.startsWith("/")) {
                        _cacheMap.put(suf, c.path);
                    } else {
                        _cacheMap.put("/" + suf, c.path);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
