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

        _cacheMap.forEach((path2,suf)->{
            if (path.startsWith(suf)) {
                exec(ctx, path2);
            }
        });
    }

    private void exec(XContext ctx, String path2){
        try{
            do_exec(ctx,path2);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void do_exec(XContext ctx, String path2) throws Exception {
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
            JsxUtil.g().runApi(name, file, false);
        }catch (Exception ex) {
            String err = ExceptionUtils.getString(ex);
            ctx.output(err);
            LogUtil.log("_file", file.tag, file.path, 0, "", err);

            ctx.setHandled(true);
        }
    }

    private HashMap<String,String> _cacheMap = new HashMap<>();
    public void del(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }

        _cacheMap.remove(path);
    }
    public void add(String path, String note) {
        if (TextUtils.isEmpty(note)) {
            return;
        }

        String suf = note.split("#")[0];

        //要有字符，且必须是目录
        if (suf.length() > 3 &&
                suf.endsWith("/") &&
                suf.startsWith("/")) {
            _cacheMap.put(path, suf);
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

                if (suf.length()>3 &&
                        suf.endsWith("/") &&
                        suf.startsWith("/")) {
                    _cacheMap.put(c.path, suf);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
