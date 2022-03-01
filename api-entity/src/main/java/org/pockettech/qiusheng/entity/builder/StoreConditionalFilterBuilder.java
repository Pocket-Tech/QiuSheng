package org.pockettech.qiusheng.entity.builder;

import org.pockettech.qiusheng.entity.filter.StoreConditionalFilter;

public class StoreConditionalFilterBuilder {
    StoreConditionalFilter filter = new StoreConditionalFilter();

    public StoreConditionalFilter constructListFilter(String word, Integer org, Integer mode, Integer lvge, Integer lvle, Integer beta, Integer from) {
        reset();
        if (word != null) {
            filter.setWord(word);
        }
        if (org != null) {
            filter.setOrg(org);
        }
        if (mode != null) {
            filter.setMode(mode);
        }
        if (lvge != null) {
            filter.setLvge(lvge);
        }
        if (lvle != null) {
            filter.setLvle(lvle);
        }
        if (beta != null) {
            filter.setBeta(beta);
        }
        if (from != null) {
            filter.setFrom(from);
        }
        return filter;
    }

    public StoreConditionalFilter constructPromoteFilter(Integer org, Integer mode, Integer from) {
        reset();
        if (org != null) {
            filter.setOrg(org);
        }
        if (mode != null) {
            filter.setMode(mode);
        }
        if (from != null) {
            filter.setFrom(from);
        }
        return filter;
    }

    private void reset() {
        filter = new StoreConditionalFilter();
    }
}
