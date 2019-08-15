package org.noear.thinkjt;

import org.noear.thinkjt.dao.DbApi;

public class Config {
    public static final String code="thinkjt";
    public static final String code_db="thinkjt.db";
    public static final String code_center="thinkjt.center";

    public static final String code_ext="jt_ext";

    public static final String frm_root_img = DbApi.cfgGet("_frm_root_img","/img/");
    public static final String frm_actoin_suffix = DbApi.cfgGet("_frm_actoin_suffix",".jsx");

}
