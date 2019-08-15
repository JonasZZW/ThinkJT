package org.noear.think.controller;

import org.noear.think.dao.*;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XHandler;

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
        }catch (Exception ex){
            PrintWriter pw = new PrintWriter(ctx.outputStream());

            ex.printStackTrace(pw);

            pw.flush();

            ctx.setHandled(true);
        }
    }
}
