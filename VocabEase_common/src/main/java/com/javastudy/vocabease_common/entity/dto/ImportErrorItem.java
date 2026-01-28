package com.javastudy.vocabease_common.entity.dto;

import java.util.List;

public class ImportErrorItem {
    private Integer rowNumber;
    private List<String> errorItemList;

    public ImportErrorItem(){}

    public ImportErrorItem(Integer rowNumber, List<String> errorItemList) {
        this.rowNumber = rowNumber;
        this.errorItemList = errorItemList;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }
    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }
    public List<String> getErrorItemList() {
        return errorItemList;
    }
    public void setErrorItemList(List<String> errorItemList) {
        this.errorItemList = errorItemList;
    }
}
