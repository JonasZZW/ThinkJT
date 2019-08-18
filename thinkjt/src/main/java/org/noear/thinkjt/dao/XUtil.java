package org.noear.thinkjt.dao;

import net.coobird.thumbnailator.Thumbnails;
import org.noear.thinkjt.controller.SufHandler;
import org.noear.thinkjt.utils.*;
import org.noear.snack.ONode;
import org.noear.snack.core.Constants;
import org.noear.snack.core.utils.NodeUtil;
import org.noear.solon.XApp;
import org.noear.solon.annotation.XNote;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XFile;
import org.noear.solon.core.XMap;
import org.noear.weed.DbContext;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * 引擎扩展工具，提供一些基础的操作支持
 * */
public class XUtil {
    private final Map<String,DbContext> _db_cache = new HashMap<>();

    /**
     * 生成GUID
     */
    @XNote("生成GUID")
    public String guid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    @XNote("获取当前用户IP")
    public String ip() {
        return IPUtils.getIP(XContext.current());
    }

    /**
     * 生成数据库上下文
     */
    @XNote("生成数据库上下文")
    public DbContext db(String cfg) {
        return db(cfg,null);
    }

    @XNote("生成数据库上下文")
    public DbContext db(String cfg, DbContext def) {
        if (TextUtils.isEmpty(cfg)) {
            return def;
        }

        DbContext tmp = _db_cache.get(cfg);

        if (tmp == null) {
            synchronized (cfg) {
                tmp = _db_cache.get(cfg);

                if (tmp != null) {
                    String[] args = cfg.split(" ");
                    tmp = DbUtil.getDb(XMap.from(args));

                    if (tmp != null) {
                        _db_cache.put(cfg, tmp);
                    }
                }
            }
        }

        return tmp;
    }

    @XNote("创建一个Map<String,Object>集合")
    public Map<String, Object> newMap() {
        return new HashMap<>();
    }

    @XNote("创建一个List<Object>集合")
    public List<Object> newList() {
        return new ArrayList<>();
    }

    @XNote("创建一个List<Object>集合")
    public List<Object> newList(Object[] ary) {
        return Arrays.asList(ary);
    }

    @XNote("创建一个Set<Object>集合")
    public Set<Object> newSet() {
        return new HashSet<>();
    }

    @XNote("创建一个ByteArrayOutputStream")
    public OutputStream newOutputStream(){
        return new ByteArrayOutputStream();
    }

    @XNote("执行 HTTP 请求")
    public HttpUtils http(String url) {
        return new HttpUtils(url);
    }

    /**
     * 编码html
     */
    @XNote("编码html")
    public String htmlEncode(String str) {
        if (str == null) {
            return "";
        } else {
            str = str.replaceAll("<", "&lt;");
            str = str.replaceAll(">", "&gt;");
        }
        return str;
    }

    @XNote("编码url")
    public String urlEncode(String str) throws Exception{
        if(str == null){
            return str;
        }

       return URLEncoder.encode(str, "utf-8");
    }

    @XNote("解码url")
    public String urlDecode(String str) throws Exception{
        if(str == null){
            return str;
        }

        return URLDecoder.decode(str, "utf-8");
    }

    /**
     *
     * ****************************/

    /**
     * 配置获取
     */
    @XNote("配置获取")
    public String cfgGet(String name) throws Exception {
        return DbApi.cfgGet(name);
    }

    @XNote("配置获取")
    public String cfgGet(String name,String def) throws Exception {
        return DbApi.cfgGet(name, def);
    }

    /**
     * 配置设置
     */
    @XNote("配置设置")
    public boolean cfgSet(String name, String value) throws Exception {
        return DbApi.cfgSet(name, value);
    }

    /**
     * 保存图片
     */
    @XNote("保存图片")
    public String imgSet(XFile file) throws Exception {
        return imgSet(file, file.extension);
    }

    @XNote("保存图片")
    public String imgSet(XFile file, String tag, String dir, int name_mod) throws Exception {
        String extension = file.extension;
        byte[] data_byte = IOUtils.toBytes(file.content);
        String data = Base64Utils.encodeByte(data_byte);
        StringBuilder path = new StringBuilder();

        if(name_mod==0) {
            //自动
            tag = null;
            path.append("/img/");
            path.append(guid());
            path.append(".");
            path.append(extension);
        }else{
            //保持原名
            path.append("/img/");

            if(TextUtils.isEmpty(tag)==false){
                path.append(tag).append("/");
            }

            if(TextUtils.isEmpty(dir)==false){
                path.append(dir).append("/");
            }

            path.append(file.name);
        }

        String path2= path.toString().replace("//", "/");

        DbApi.imgSet(tag, path2, file.contentType, data, "");

        return path2;
    }

    /**
     * 保存图片（后缀名可自定义）
     */
    @XNote("保存图片（后缀名可自定义）")
    public String imgSet(XFile file, String extension) throws Exception {
        byte[] data_byte = IOUtils.toBytes(file.content);
        String data = Base64Utils.encodeByte(data_byte);
        String path = "/img/" + guid() + "." + extension;

        DbApi.imgSet(null,path, file.contentType, data, "");

        return path;
    }

    /**
     * 保存图片（内容，类型，后缀名可自定义）
     */
    @XNote("保存图片（内容，类型，后缀名可自定义）")
    public String imgSet(String content, String contentType, String extension) throws Exception {
        String data = Base64Utils.encode(content);
        String path = "/img/" + guid() + "." + extension;

        DbApi.imgSet(null,path, contentType, data, "");

        return path;
    }

    @XNote("保存图片（内容，类型，名字，后缀名可自定义）")
    public String imgSet(String content, String contentType, String extension, String name) throws Exception {
        String data = Base64Utils.encode(content);
        String path = "/img/" + name + "." + extension;

        DbApi.imgSet(null,path, contentType, data, "");

        return path;
    }

    @XNote("保存图片（内容，类型，名字，后缀名，标签可自定义）")
    public String imgSet(String content, String contentType, String extension, String name, String label) throws Exception {
        String data = Base64Utils.encode(content);
        String path = "/img/" + name + "." + extension;

        DbApi.imgSet(null, path, contentType, data, label);

        return path;
    }

    @XNote("设定图片输出名称")
    public String imgOutName(XContext ctx, String filename) throws Exception {
        String filename2 = URLEncoder.encode(filename, "utf-8");

        ctx.headerSet("Content-Disposition", "attachment; filename=\"" + filename2 + "\"");
        return filename;
    }

    /**
     * 修改图片
     */
    @XNote("修改图片")
    public String imgUpd(String path, String content) throws Exception {
        String data = Base64Utils.encode(content);

        DbApi.imgUpd(path, data);

        return path;
    }

    /**
     * 获取图片内容
     */
    @XNote("获取图片内容")
    public String imgGet(String path) throws Exception {
        AImageModel img = DbApi.imgGet(path);
        return img2String(img.data);
    }

    @XNote("图片内容转为字符串")
    public String img2String(String data) {
        if (TextUtils.isEmpty(data)) {
            return "";
        } else {
            return Base64Utils.decode(data);
        }
    }

    /**
     * 新建文件
     */
    @XNote("文件新建")
    public boolean fileNew(int fid, XContext ctx) throws Exception {
        return DbApi.fileNew(fid, ctx);
    }

    /**
     * 文件设置内容
     */
    @XNote("文件设置内容")
    public boolean fileSet(int fid, String fcontent) throws Exception {
        return DbApi.fileSet(fid, fcontent);
    }

    @XNote("文件刷新缓存")
    public boolean fileFlush(String path, boolean is_del) {
        String path2 = path;
        String name = path2.replace("/", "__");

        if(is_del){
            RouteHelper.del(path);
        }else{
            RouteHelper.add(path);
        }

        AFileUtil.remove(path2);
        FtlUtil.g().del(name);
        JsxUtil.g().del(name);
        return true;
    }

    @XNote("重启缓存")
    public boolean restart() {
        AFileUtil.removeAll();
        AImageUtil.removeAll();
        FtlUtil.g().delAll();
        JsxUtil.g().delAll();
        DbUtil.cache.clear();
        SufHandler.g().reset();
        RouteHelper.reset();
        return true;
    }

    /**
     *
     ****************************/
    @XNote("获取接口列表")
    public List<Map<String, Object>> apiList() {
        Map<String, Object> tmp = new HashMap<>();

        tmp.putAll(XApp.global().shared());
        tmp.put("XUtil.http(url)", HttpUtils.class);
        tmp.put("XUtil.db(cfg)", DbContext.class);
        tmp.put("XUtil.paging(ctx,pageSize)", PagingModel.class);
        tmp.put("XUtil.thumbnail(file)", Thumbnails.Builder.class);

        tmp.put("ctx", XContext.class);

        tmp.put("XFile", XFile.class);

        tmp.put("new Datetime()", Datetime.class);
        tmp.put("new Timecount()", Timecount.class);
        tmp.put("new Timespan(date)", Timespan.class);

        return MethodUtils.getMethods(tmp);
    }


    /**
     * 生成md5码
     */
    @XNote("生成md5码")
    public String md5(String str) {
        return EncryptUtils.md5(str);
    }

    /**
     * 生成sha1码
     */
    @XNote("生成sha1码")
    public String sha1(String str) {
        return EncryptUtils.sha1(str);
    }


    /**
     * base64
     */
    @XNote("BASE64编码")
    public String base64Encode(String text) {
        return Base64Utils.encode(text);
    }

    @XNote("BASE64解码")
    public String base64Decode(String text) {
        return Base64Utils.decode(text);
    }

    /**
     * 生成随机码
     */
    @XNote("生成随机码")
    public String codeByRandom(int len) {
        return CodeUtils.codeByRandom(len);
    }

    /**
     * 字符码转为图片
     */
    @XNote("字符码转为图片")
    public BufferedImage codeToImage(String code) throws Exception {
        return ImageUtils.getValidationImage(code);
    }

    @XNote("InputStream转为String")
    public String streamToString(InputStream inStream) throws Exception {
        return IOUtils.toString(inStream, "utf-8");
    }

    @XNote("OutStream转为InputStream")
    public InputStream streamOutToIn(OutputStream outStream) throws Exception
    {
        return IOUtils.outToIn(outStream);
    }

    @XNote("String转为InputStream")
    public InputStream stringToStream(String str) throws Exception{
        return new ByteArrayInputStream(str.getBytes("UTF-8"));
    }

    @XNote("Object转为ONode")
    public ONode objectToNode(Object obj) throws Exception {
        return NodeUtil.fromObj(Constants.def, obj);
    }

    @XNote("生成分页数据模型")
    public PagingModel paging(XContext ctx, int pageSize) {
        return new PagingModel(ctx, pageSize);
    }

    @XNote("格式化活动时间")
    public String liveTime(Date date) {
        return TimeUtils.liveTime(date);
    }


    @XNote("创建缩略图工具")
    public Object thumbnail(XFile file){
        return Thumbnails.of(file.content);
    }


    @XNote("是否为数字")
    public boolean isNumber(String str) {
        return TextUtils.isNumber(str);
    }

    @XNote("加载插件里的jar包")
    public boolean loadJar(String path, String data64, String plugin) throws Exception {
        return JarUtils.loadJar(path,data64,plugin);
    }

    @XNote("调用一个文件")
    public Object call(String path) throws Exception {
        return CallUtil.callFile(path);
    }

    @XNote("调用一组勾子")
    public String callHook(String tag,String label) {
        return CallUtil.callHook(tag, label);
    }

    @XNote("日志")
    public long log(Map<String,Object> data) {
        return LogUtil.log(data);
    }
}
