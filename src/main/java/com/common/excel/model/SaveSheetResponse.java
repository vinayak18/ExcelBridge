package com.common.excel.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveSheetResponse {
    private String sheetName;
    private int totalRows;
    private int rowsProcessed;
    private List<String> errors;
}
