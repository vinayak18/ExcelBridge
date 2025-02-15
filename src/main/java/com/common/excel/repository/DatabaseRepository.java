package com.common.excel.repository;

import java.util.List;
import java.util.Map;

public interface DatabaseRepository {

	public void saveSheetData(String sheetName,List<Map<String, Object>> rowData);
}
