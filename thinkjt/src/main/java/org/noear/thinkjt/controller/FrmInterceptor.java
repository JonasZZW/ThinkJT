package org.noear.thinkjt.controller;

import org.noear.thinkjt.dao.*;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XHandler;
import org.noear.thinkjt.utils.ExceptionUtils;

import java.io.PrintWriter;

/**
 * 拉截器的代理（数据库安全）
 * */
public class FrmInterceptor implements XHandler {
    public static final FrmInterceptor g = new FrmInterceptor();

    @Override
    public void handle(XContext ctx) throws Exception {
        String path = ctx.path();
        String path2 = AFileUtil.path3(path);

        if(path2 == null){
            return;
        }

        String name = path2.replace("/", "__");

        AFileModel file = AFileUtil.get(path2);

        if (file.file_id == 0) {
            return;
        }

        try {
            JsxUtil.g().runApi(name, file, true);
        }catch (Exception ex) {
            String err = ExceptionUtils.getString(ex);
            ctx.output(err);
            LogUtil.log("_file", file.tag, file.path, 0, ctx.path(), err);

            ctx.setHandled(true);
        }
    }
}
