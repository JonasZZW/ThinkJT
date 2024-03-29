package org.noear.thinkjt.dao;

import org.noear.solon.annotation.XNote;
import org.noear.solon.core.XContext;

/**
 * 分页条数据模型
 * */
public class PagingModel {

    public PagingModel(XContext ctx, long pageSize, boolean fixedSize) {
        _page = ctx.paramAsLong("_page", 1);
        _pageSize_def = pageSize;

        if (_page < 1) {
            _page = 1;
        }

        if(fixedSize){
            _pageSize = pageSize;
        }else {
            _pageSize = ctx.paramAsInt("_pageSize", 0);

            if (_pageSize < 1) {
                _pageSize = Integer.parseInt(ctx.cookie("_pageSize", "0"));
            }

            if (_pageSize > 99) {
                _pageSize = 99;
            }

            if (_pageSize < 1) {
                _pageSize = pageSize;
            } else {
                ctx.cookieSet("_pageSize", _pageSize + "", "", ctx.path(), Integer.MAX_VALUE);
            }
        }

    }

    private final long _page_begin = 1;

    private long _page; //从1开始
    private long _pageSize;
    private long _pageSize_def;
    private long _total;

    @XNote("设置总记录数")
    public PagingModel totalSet(int total){
        _total = total;
        return this;
    }

    @XNote("起始记录数")
    public long start(){
        return (int)(_pageSize * (_page - _page_begin));
    }

    @XNote("分页长度")
    public long pageSize(){
        return _pageSize;
    }

    @XNote("默认分页长度")
    public long pageSizeDef(){
        return _pageSize_def;
    }

    @XNote("当前页码")
    public long page(){
        return _page;
    }

    @XNote("页码区间的起始页")
    public long pageBegin(int size) {
        boolean isleft = (pages() - page()) > page();//当前页，是否区于左侧
        long _bef = 0;
        if (isleft) {
            //如果在左侧
            _bef = page() - ((size - 1) / 2);
        } else {
            //如果在右侧，先算出最右侧的页码
            long _aft = page() + ((size - 1) / 2);

            if (_aft > pages()) {
                _aft = pages();
            }

            _bef = _aft - size; //最右侧-长度=最左侧
        }

        if (_bef < 1) {
            _bef = 1;
        }

        return _bef;
    }

    @XNote("上一页码")
    public long pagePrev(){
        if(_page>1) {
            return _page - 1;
        }else{
            return _page;
        }
    }

    @XNote("下一页码")
    public long pageNext(){
        return _page + 1;
    }

    @XNote("是否有上一页")
    public boolean hasPrev() {
        return page()>1;
    }

    @XNote("是否有下一页")
    public boolean hasNext() {
        return pages() >= pageNext();
    }

    @XNote("总页数")
    public long pages() {
        if (_pages < 0) {
            if (_total % _pageSize > 0) {
                _pages = (_total / _pageSize) + 1;
            } else {
                _pages = (_total / _pageSize);
            }
        }

        return _pages;
    }
    private long _pages =-1;

    @XNote("总记录数")
    public long total(){
        return _total;
    }
}
