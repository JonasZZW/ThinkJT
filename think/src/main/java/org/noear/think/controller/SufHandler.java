package org.noear.think.controller;

import org.noear.think.dao.*;
import org.noear.think.utils.TextUtils;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XHandler;

import java.util.HashMap;
import java.util.List;

/**
 * 某类后缀文件的代理（数据库安全）
 * */
public class SufHandler implements XHandler {
    private HashMap<String,String> _sufMap = new HashMap<>();

    private static final SufHandler _g = new SufHandler();
    public static SufHandler g(){
        return  _g;
    }

    private SufHandler(){
        reset();
    }

    @Override
    public void handle(XContext ctx) throws Exception {
        String path = ctx.path();
        for (String suf : _sufMap.keySet()) {
            if (path.endsWith(suf)) {
                ctx.setHandled(true);

                exec(ctx, _sufMap.get(suf));
                return;
            }
        }
    }

    private void exec(XContext ctx, String path) throws Exception {
        String path2 = path;//AFileUtil.path2(path);//不需要转为*
        String name = path2.replace("/", "__");

        AFileModel file = AFileUtil.get(path2);

        //文件不存在，则404
        if (file.file_id == 0) {
            ctx.status(404);
            return;
        }

        ExcUtil.exec(name,file,ctx);
    }

    public void reset() {
        if (DbUtil.db() == null) {
            return;
        }

        try {
            _sufMap.clear();

            List<AFileModel> list = DbApi.fileFilters();
            for (AFileModel c : list) {
                if(TextUtils.isEmpty(c.note)){
                    continue;
                }

                String suf = c.note.split("#")[0];

                if (suf.length()>0) {
                    if (suf.startsWith(".")) {
                        _sufMap.put(suf, c.path);
                    } else {
                        _sufMap.put("." + suf, c.path);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
