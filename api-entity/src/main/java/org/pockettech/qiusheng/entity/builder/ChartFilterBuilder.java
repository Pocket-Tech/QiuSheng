package org.pockettech.qiusheng.entity.builder;

import org.pockettech.qiusheng.entity.filter.ChartFilter;

public class ChartFilterBuilder {
    ChartFilter filter = new ChartFilter();

    public ChartFilter constructSimpleFilter(Integer sid, Integer beta, Integer mode, Integer from, Integer promote) {
        reset();
        if (sid != null) {
            filter.setSid(sid);
        }
        if (beta != null) {
            filter.setBeta(beta);
        }
        if (mode != null) {
            filter.setMode(mode);
        }
        if (from != null) {
            filter.setFrom(from);
        }
        if (promote != null) {
            filter.setPromote(promote);
        }
        return filter;
    }

    public ChartFilter constructQueryFilter(Integer sid, Integer cid, Integer org) {
        reset();
        if (sid != null) {
            filter.setSid(sid);
        }
        if (cid != null) {
            filter.setCid(cid);
        }
        if (org != null) {
            filter.setMode(org);
        }
        return filter;
    }
    private void reset() {
        filter = new ChartFilter();
    }
}
