package org.noear.think.dao;

import org.noear.solon.annotation.XNote;
import org.noear.solon.core.XContext;

/**
 * 分页条数据模型
 * */
public class PagingModel {

    public PagingModel(XContext ctx, int pageSize){
        _page = ctx.paramAsInt("_page",1);
        if(_page<1){
            _page=1;
        }
        _pageSize = pageSize;
    }

    private final int _page_begin = 1;

    private int _page; //从1开始
    private int _pageSize;
    private long _total;

    @XNote("设置总记录数")
    public PagingModel totalSet(int total){
        _total = total;
        return this;
    }

    @XNote("起始记录数")
    public int start(){
        return (int)(_pageSize * (_page - _page_begin));
    }

    @XNote("分页长度")
    public int pageSize(){
        return _pageSize;
    }

    @XNote("当前页码")
    public int page(){
        return _page;
    }

    @XNote("上一页码")
    public int pagePrev(){
        if(_page>1) {
            return _page - 1;
        }else{
            return _page;
        }
    }

    @XNote("下一页码")
    public int pageNext(){
        return _page + 1;
    }

    @XNote("是否有上一页")
    public boolean hasPrev() {
        return page()>1;
    }

    @XNote("是否有下一页")
    public boolean hasNext() {
        return pages() > pageNext();
    }

    @XNote("总页数")
    public long pages() {
        if (_total % _pageSize > 0) {
            return (_total / _pageSize) + 1;
        } else {
            return (_total / _pageSize);
        }
    }

    @XNote("总记录数")
    public long total(){
        return _total;
    }
}
