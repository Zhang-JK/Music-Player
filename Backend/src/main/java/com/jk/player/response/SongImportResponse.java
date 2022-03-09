package com.jk.player.response;

import java.util.List;
import java.util.Map;

public class SongImportResponse {
    private Integer totalCount;
    private List<Object> successList;
    private Map<Object, String> failList;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<Object> getSuccessList() {
        return successList;
    }

    public void setSuccessList(List<Object> successList) {
        this.successList = successList;
    }

    public Map<Object, String> getFailList() {
        return failList;
    }

    public void setFailList(Map<Object, String> failList) {
        this.failList = failList;
    }
}
