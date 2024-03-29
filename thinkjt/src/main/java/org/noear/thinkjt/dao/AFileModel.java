package org.noear.thinkjt.dao;

import java.util.*;

public class AFileModel {

    /**  */
    public int file_id;

    public String tag;
    public String label;
    public String note;

    /** 路径 */
    public String path;
    /** 是否静态 */
    public boolean is_staticize;
    /** 可编辑 */
    public boolean is_editable;
    /** 禁止访问 */
    public boolean is_disabled;
    /** 连接到 */
    public String link_to;
    /** 分类标签 */
    public String edit_mode;
    /** 内容类型 */
    public String content_type;
    /** 内容 */
    public String content;

    public int plan_state;
    public Date plan_begin_time;
    public Date plan_last_time;
    public long  plan_last_timespan;
    public String plan_interval;
    public int plan_max;
    public int plan_count;

    /** 创建时间 */
    public Date create_fulltime;
    /** 更新时间 */
    public Date update_fulltime;

    public boolean _is_day_task;

}