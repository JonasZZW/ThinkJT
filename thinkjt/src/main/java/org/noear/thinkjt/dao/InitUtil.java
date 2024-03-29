package org.noear.thinkjt.dao;

import org.noear.thinkjt.Config;
import org.noear.thinkjt.utils.Base64Utils;
import org.noear.thinkjt.utils.HttpUtils;
import org.noear.thinkjt.utils.JarUtils;
import org.noear.thinkjt.utils.TextUtils;
import org.noear.snack.ONode;
import org.noear.solon.core.XMap;
import org.noear.weed.DbContext;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * 初始化工具类（提供引擎初始化支持）
 * */
public class InitUtil {

    public static DbContext db(){
        return DbUtil.db();
    }

    public static void tryInitDb(){
        try{
             do_initDb();
        }catch (Throwable ex){
            ex.printStackTrace();
        }
    }

    private static void do_initDb() throws Exception {
        int num = db().sql("SHOW TABLES LIKE 'a\\_%';").getDataList().getRowCount();
        if (num >= 5) {
            return;
        }

        db().sql("CREATE TABLE IF NOT EXISTS `a_config` (\n" +
                "  `cfg_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '配置ID',\n" +
                "  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '分组标签',\n" +
                "  `label` varchar(40) NOT NULL DEFAULT '' COMMENT '标记',\n" +
                "  `name` varchar(99) NOT NULL COMMENT '名称',\n" +
                "  `value` varchar(999) NOT NULL DEFAULT '' COMMENT '值',\n" +
                "  `note` varchar(99) NOT NULL DEFAULT '' COMMENT '备注',\n" +
                "  `edit_mode` varchar(40) DEFAULT NULL COMMENT '编辑模式',\n" +
                "  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '禁止使用',\n" +
                "  `is_exclude` tinyint(1) NOT NULL DEFAULT '0' COMMENT '排除导入',\n" +
                "  `is_modified` tinyint(1) NOT NULL DEFAULT '0' COMMENT '可修改的（终端用户）',\n" +
                "  `create_fulltime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                "  `update_fulltime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',\n" +
                "  PRIMARY KEY (`cfg_id`),\n" +
                "  UNIQUE KEY `IX_key` (`name`) USING BTREE,\n" +
                "  KEY `IX_tag` (`tag`) USING BTREE,\n" +
                "  KEY `IX_label` (`label`) USING BTREE\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='jt-配置表';").execute();

        db().sql("CREATE TABLE IF NOT EXISTS `a_file` (\n" +
                "  `file_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文件ID',\n" +
                "  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '分组村签',\n" +
                "  `label` varchar(40) NOT NULL DEFAULT '' COMMENT '标记',\n" +
                "  `path` varchar(60) NOT NULL COMMENT '文件路径',\n" +
                "  `is_staticize` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否静态',\n" +
                "  `is_editable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可编辑',\n" +
                "  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',\n" +
                "  `is_exclude` tinyint(1) NOT NULL DEFAULT '0' COMMENT '排除导入',\n" +
                "  `link_to` varchar(100) DEFAULT NULL COMMENT '连接到',\n" +
                "  `edit_mode` varchar(40) NOT NULL DEFAULT '' COMMENT '编辑模式',\n" +
                "  `content_type` varchar(60) NOT NULL DEFAULT '' COMMENT '内容类型',\n" +
                "  `content` longtext COMMENT '内容',\n" +
                "  `note` varchar(99) DEFAULT '' COMMENT '备注',\n" +
                "  `plan_state` int(11) NOT NULL DEFAULT '0' COMMENT '计划状态',\n" +
                "  `plan_begin_time` datetime DEFAULT NULL COMMENT '计划开始执行时间',\n" +
                "  `plan_last_time` datetime DEFAULT NULL COMMENT '计划最后执行时间',\n" +
                "  `plan_last_timespan` bigint(20) NOT NULL DEFAULT '0' COMMENT '计划最后执行时间长度',\n" +
                "  `plan_interval` varchar(10) NOT NULL DEFAULT '' COMMENT '计划执行间隔',\n" +
                "  `plan_max` int(11) NOT NULL DEFAULT '0' COMMENT '计划执行最多次数',\n" +
                "  `plan_count` int(11) NOT NULL DEFAULT '0' COMMENT '计划执行累计次数',\n" +
                "  `create_fulltime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                "  `update_fulltime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',\n" +
                "  PRIMARY KEY (`file_id`),\n" +
                "  UNIQUE KEY `IX_key` (`path`) USING HASH,\n" +
                "  KEY `IX_tag` (`tag`) USING BTREE,\n" +
                "  KEY `IX_label` (`label`) USING BTREE\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='jt-文件表';").execute();

        db().sql("CREATE TABLE IF NOT EXISTS `a_image` (\n" +
                "  `img_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '图片ID',\n" +
                "  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '分组标签',\n" +
                "  `label` varchar(40) NOT NULL DEFAULT '' COMMENT '标签',\n" +
                "  `path` varchar(99) NOT NULL COMMENT '文件路径',\n" +
                "  `content_type` varchar(99) NOT NULL COMMENT '内容类型',\n" +
                "  `data` longtext COMMENT '数据',\n" +
                "  `data_size` int(11) NOT NULL DEFAULT '0' COMMENT '数据长度',\n" +
                "  `note` varchar(99) DEFAULT NULL COMMENT '备注',\n" +
                "  `create_fulltime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                "  `update_fulltime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',\n" +
                "  PRIMARY KEY (`img_id`),\n" +
                "  UNIQUE KEY `IX_key` (`path`) USING BTREE,\n" +
                "  KEY `IX_tag` (`tag`) USING BTREE,\n" +
                "  KEY `IX_label` (`label`) USING BTREE\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='tj-图片表';").execute();

        db().sql("CREATE TABLE IF NOT EXISTS `a_log` (\n" +
                "  `log_id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `level` int(11) NOT NULL DEFAULT '0' COMMENT '等级',\n" +
                "  `tag` varchar(99) NOT NULL DEFAULT '' COMMENT '标签',\n" +
                "  `tag1` varchar(99) NOT NULL DEFAULT '' COMMENT '标签1',\n" +
                "  `tag2` varchar(99) NOT NULL DEFAULT '' COMMENT '标签2',\n" +
                "  `tag3` varchar(99) NOT NULL DEFAULT '' COMMENT '标签3',\n" +
                "  `tag4` varchar(99) NOT NULL DEFAULT '' COMMENT '标签4',\n" +
                "  `summary` varchar(1000) NOT NULL DEFAULT '' COMMENT '摘要',\n" +
                "  `content` longtext COMMENT '内容',\n" +
                "  `log_date` int(11) NOT NULL DEFAULT '0' COMMENT '记录日期',\n" +
                "  `log_fulltime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录完整时间',\n" +
                "  PRIMARY KEY (`log_id`),\n" +
                "  KEY `IX_date` (`log_date`) USING BTREE,\n" +
                "  KEY `IX_tag` (`tag`) USING BTREE,\n" +
                "  KEY `IX_tag1` (`tag1`) USING BTREE,\n" +
                "  KEY `IX_tag2` (`tag2`) USING BTREE,\n" +
                "  KEY `IX_tag3` (`tag3`) USING BTREE,\n" +
                "  KEY `IX_tag4` (`tag4`) USING BTREE\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='jt-日志表';").execute();

        db().sql("CREATE TABLE IF NOT EXISTS `a_plugin` (\n" +
                "  `plugin_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '插件ID',\n" +
                "  `plugin_tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '插件标签',\n" +
                "  `tag` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',\n" +
                "  `label` varchar(40) NOT NULL DEFAULT '' COMMENT '标记',\n" +
                "  `category` varchar(40) NOT NULL DEFAULT '' COMMENT '分类（预留）',\n" +
                "  `name` varchar(40) NOT NULL COMMENT '名称',\n" +
                "  `author` varchar(40) NOT NULL COMMENT '作者',\n" +
                "  `contacts` varchar(99) DEFAULT NULL COMMENT '联系方式',\n" +
                "  `ver_code` int(11) NOT NULL DEFAULT '0' COMMENT '版本代号',\n" +
                "  `ver_name` varchar(40) NOT NULL COMMENT '版本名称',\n" +
                "  `description` varchar(255) DEFAULT NULL COMMENT '描述',\n" +
                "  `thumbnail` varchar(255) DEFAULT NULL COMMENT '缩略图',\n" +
                "  `url` varchar(255) NOT NULL DEFAULT '' COMMENT '地址',\n" +
                "  `is_installed` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已安装（相对于自己）',\n" +
                "  `is_approved` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否审核通过',\n" +
                "  `num_downloads` int(11) NOT NULL DEFAULT '0' COMMENT '下载量',\n" +
                "  `create_fulltime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                "  `update_fulltime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',\n" +
                "  PRIMARY KEY (`plugin_id`),\n" +
                "  UNIQUE KEY `IX_key` (`plugin_tag`) USING HASH,\n" +
                "  KEY `IX_tag` (`tag`) USING BTREE,\n" +
                "  KEY `IX_category` (`category`) USING BTREE,\n" +
                "  KEY `IX_label` (`label`) USING BTREE\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='jt-插件表';").execute();


        System.out.println("Complete table structure");
    }

    public static void tryInitCore(XMap map){
        try{
             do_initCore(map);
        }catch (Throwable ex){
            ex.printStackTrace();
        }
    }
    private static void do_initCore(XMap map) throws Exception {
        String center = map.get(Config.code_center);
        if (TextUtils.isEmpty(center)) {
            center = map.get("center");
        }

        if (TextUtils.isEmpty(center)) {
            return;
        }

        if (db().table("a_plugin").exists()) {
            return;
        }

        String url = null;

        if(center.indexOf("://")>0){
            url =  center + "/.plugin/pull.jsx?plugin_tag=_core.noear";
        }else {
            url = "http://" + center + "/.plugin/pull.jsx?plugin_tag=_core.noear";
        }


        String json = new HttpUtils(url).get();
        ONode data = ONode.map(json);
        if (data.get("code").getInt() != 1) {
            return;
        }

        ONode body = data.get("data").get("body");
        ONode meta = data.get("data").get("meta");
        String tag = meta.get("tag").getString();

        String j_config = Base64Utils.decode(body.get("config").getString());
        String j_file = Base64Utils.decode(body.get("file").getString());
        String j_img = Base64Utils.decode(body.get("image").getString());

        List<Map<String, Object>> d_config = ONode.deserialize(j_config, List.class);
        for (Map<String, Object> m : d_config) {
            m.remove("cfg_id");
            db().table("a_config").setMap(m).insert();
        }

        List<Map<String, Object>> d_file = ONode.deserialize(j_file, List.class);
        for (Map<String, Object> m : d_file) {
            m.remove("file_id");
            if (m.get("content") != null) {
                String c2 = Base64Utils.decode(m.get("content").toString());
                m.put("content", c2);
            }
            db().table("a_file").setMap(m).insert();
        }

        //23.资源表
        //db().table("a_image").where("tag=?", tag).delete();
        List<Map<String, Object>> d_img = ONode.deserialize(j_img, List.class);
        if (d_img != null) {
            for (Map<String, Object> m : d_img) {
                m.remove("img_id");
                db().table("a_image").setMap(m).insert();
            }
        }

        db().table("a_plugin")
                .set("plugin_tag", meta.get("plugin_tag").getString())
                .set("tag", meta.get("tag").getString())
                .set("name", meta.get("name").getString())
                .set("author", meta.get("author").getString())
                .set("contacts", meta.get("contacts").getString())
                .set("ver_name", meta.get("ver_name").getString())
                .set("ver_code", meta.get("ver_code").getString())
                .set("description", meta.get("description").getString())
                .set("thumbnail", meta.get("thumbnail").getString())
                .set("is_installed", 1)
                .updateExt("plugin_tag");


        db().table("a_config")
                .set("value", "Iv1H81dI2ZNzDS2n")
                .where("name=?", "_frm_admin_pwd")
                .update();

        db().table("a_config")
                .set("value", "0")
                .where("name=?", "_frm_enable_dev")
                .update();

        db().table("a_file")
                .set("link_to", "")
                .where("path='/'")
                .update();


        System.out.println("Complete _core loading");
    }

    public static String tryInitExtend(XMap xarg) {
        String extend = xarg.get("extend");
        if (extend == null) {
            extend = do_buildRoot();
        }

        return extend;
    }

    private static String do_buildRoot() {
        String fileName = "application.properties";

        URL temp = org.noear.solon.XUtil.getResource(fileName);

        if (temp == null) {
            return null;
        } else {
            String uri = temp.toString();
            if (uri.startsWith("file:/")) {
                uri = uri.substring(5, uri.length() - 30);
            } else {
                int idx = uri.indexOf("jar!/");
                idx = uri.lastIndexOf("/", idx) + 1;

                uri = uri.substring(9, idx);
            }

            uri = uri + Config.code_ext+ "/";
            File dir = new File(uri);
            if (dir.exists() == false) {
                dir.mkdir();
            }

            return uri;
        }
    }

    public static void trySaveConfig(String extend,XMap map) throws Exception {
        File file = new File(extend + "_db.properties");
        file.delete();
        file.createNewFile();

        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            if("center".equals(k)){
                sb.append(Config.code).append(".");
            }else{
                sb.append(Config.code_db).append(".");
            }
            sb.append(k).append("=").append(v).append("\r\n");
        });
        FileWriter fw = new FileWriter(file);
        fw.write(sb.toString());
        fw.flush();
        fw.close();
    }
}
