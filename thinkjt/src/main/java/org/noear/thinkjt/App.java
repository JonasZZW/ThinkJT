package org.noear.thinkjt;

import org.noear.thinkjt.dao.*;
import org.noear.solon.XApp;
import org.noear.solon.core.*;

public class App {

    public static void main(String[] args) {

        //0.构建参数
        XMap xarg = XMap.from(args);

        //1.获取扩展目录
        String extend = InitUtil.tryInitExtend(xarg);

        if (extend == null) {
            throw new RuntimeException("Please enter an 'extend' parameter!");
        }

        //2.加载扩展目录（包括：配置、jar）
        ExtendUtil.init(extend);
        ExtendLoader.load(extend, xarg);

        xarg.put("extend",extend);

        //3.启动服务
        XApp app = XApp.start(null, xarg,(x)->{});
        app.sharedAdd("XUtil", new XUtil());

        //4.
        if (xarg.size() < 4) {
            //4.1.如果没有DB配置；则启动配置服务
            AppUtil.runAsInit(app,extend);
        } else {
            //4.2.如果有DB配置了；则启动工作服务
            AppUtil.init(xarg, true);

            AppUtil.runAsWork(app);
        }
    }


}
