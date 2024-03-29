package org.noear.thinkjt.controller;

import org.noear.thinkjt.dao.AImageHandler;
import org.noear.thinkjt.dao.AImageModel;
import org.noear.thinkjt.dao.AImageUtil;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XHandler;

/**
 * 图片路径的代理（有可能会，数据库会被恶意刷暴了）
 * */
public class ImgHandler implements XHandler {
    @Override
    public void handle(XContext ctx) throws Exception {
        String path = ctx.path();

        AImageModel file = AImageUtil.get(path);

        //文件不存在，则404
        if (file == null || file.img_id == 0) {
            ctx.status(404);
            return;
        }

        //如果是静态
        if (file.data == null) {
            ctx.status(404);
        } else {
            AImageHandler.handle(ctx, file);
        }
    }
}
