package com.javastudy.vocabease_common.entity.vo;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class AppUpdateVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1709835576200332171L;

    private Integer id;
    private String version;
    private long size;
    private List<String> updateList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public List<String> getUpdateList() {
        return updateList;
    }

    public void setUpdateList(List<String> updateList) {
        this.updateList = updateList;
    }
}
