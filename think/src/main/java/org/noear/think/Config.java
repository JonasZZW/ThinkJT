package org.noear.think;

import org.noear.think.dao.DbApi;

public class Config {
    public static final String frm_root_img = DbApi.cfgGet("_frm_root_img","/img/");
    public static final String frm_actoin_suffix = DbApi.cfgGet("_frm_actoin_suffix",".jsx");

}
