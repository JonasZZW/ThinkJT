package org.noear.thinkjt.controller;

import org.noear.thinkjt.dao.*;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XHandler;
import org.noear.thinkjt.utils.ExceptionUtils;

import java.io.PrintWriter;

/**
 * 路径拦截器的代理（数据库安全）
 * */
public class FrmInterceptor implements XHandler {
    public static final FrmInterceptor g = new FrmInterceptor();

    @Override
    public void handle(XContext ctx) throws Exception {
        String path = ctx.path();
        String path3 = AFileUtil.path3(path);

        if(path3 == null){
            return;
        }

        String name = path3.replace("/", "__");

        AFileModel file = AFileUtil.get(path3);

        if (file.file_id == 0) {
            return;
        }

        try {
            JsxUtil.g().runApi(name, file, true);
        }catch (Exception ex) {
            String err = ExceptionUtils.getString(ex);
            ctx.output(err);
            LogUtil.log("_file", file.tag, file.path, 0, "", err);

            ctx.setHandled(true);
        }
    }
}
