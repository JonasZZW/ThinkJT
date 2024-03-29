package org.noear.thinkjt.dao;

import org.noear.thinkjt.Config;
import org.noear.thinkjt.controller.*;
import org.noear.thinkjt.utils.IOUtils;
import org.noear.thinkjt.utils.JarUtils;
import org.noear.thinkjt.utils.TextUtils;
import org.noear.solon.XApp;
import org.noear.solon.XProperties;
import org.noear.solon.core.XHandler;
import org.noear.solon.core.XHandlerLink;
import org.noear.solon.core.XMap;
import org.noear.solon.core.XMethod;

import java.net.URL;

public class AppUtil {
    /**
     * 初始化数据库和内核
     * */
    public static void init(XMap xarg, boolean initDb){
        if(initDb) {
            XProperties prop = new XProperties();
            xarg.forEach((k, v) -> {
                prop.setProperty(k, v);
            });

            DbUtil.setDefDb(prop.getXmap(Config.code_db));
        }

        InitUtil.tryInitDb();
        InitUtil.tryInitCore(xarg);
    }


    public static void runAsInit(XApp app, String extend){
        URL temp = org.noear.solon.XUtil.getResource("setup.htm");
        String html = null;
        try {
            html = IOUtils.toString(temp.openStream(), "utf-8");
        }catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        final String  html2 = html.trim();

        app.post("/setup.jsx",(ctx)->{
            DbUtil.setDefDb(ctx.paramMap());
            try {
                DbUtil.db().sql("SHOW TABLES").execute();

                InitUtil.trySaveConfig(extend,ctx.paramMap());
                AppUtil.init(ctx.paramMap(),false);

                ctx.outputAsJson("{\"code\":1}");

                new Thread(()->{
                    XApp.global().router().clear();
                    AppUtil.runAsWork(XApp.global());
                }).start();
            }catch (Exception ex){
                ctx.outputAsJson("{\"code\":0}");
            }

        });

        app.get("/",(ctx)->{
            ctx.outputAsHtml(html2);
        });

        app.get("/**",(ctx)->{
            ctx.redirect("/");
        });
    }

    /**
     * 运行应用
     * */
    public static void runAsWork(XApp app) {

        String max_post_size = DbApi.cfgGet("http_max_post_size");

        if (TextUtils.isEmpty(max_post_size) == false) {
            app.prop().setProperty("org.eclipse.jetty.server.Request.maxFormContentSize", max_post_size);
        }


        String sss = app.prop().argx().get("sss");

        /*
         * 注入共享对传（会传导到javascript 和 freemarker 引擎）
         * */
        app.sharedAdd("db", DbUtil.db());
        app.sharedAdd("cache", DbUtil.cache);

        //2.尝试运行WEB应用
        if (TextUtils.isEmpty(sss) || sss.indexOf("web") >= 0) {
            RouteHelper.reset();

            do_runWeb(app);
        }

        //3.尝试运行SEV应用（即定时任务）
        if (TextUtils.isEmpty(sss) || sss.indexOf("sev") >= 0) {
            do_runSev(app);
        }

        CallUtil.callHook(null, "hook.start", false);
    }

    private static void do_runWeb(XApp app) {
        //拉截代理
        app.before("/**", XMethod.GET, FrmInterceptor.g());
        app.before("/**", XMethod.POST, FrmInterceptor.g());


        //资源代理(/img/**)
        app.get(Config.frm_root_img + "**", new ImgHandler());

        //文件代理
        app.all("/", AppHandler.g);
        app.all("/**", AppHandler.g);

        //后缀代理（置于所有代理的前面）
        XHandler h1 = app.handlerGet();
        XHandlerLink hx = new XHandlerLink();
        hx.node = SufHandler.g();
        hx.nextNode = h1;

        app.handlerSet(hx);
    }

    private static void do_runSev(XApp app){
        TaskUtil.run(new TaskProcessor());
    }
}
