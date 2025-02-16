package com.common.excel.repository.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.common.excel.constants.ExcelServiceConstants;
import com.common.excel.repository.DatabaseRepository;

@Repository
public class JdbcDatabaseRepository implements DatabaseRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

    @Override
	public int saveSheetData(String sheetName, List<Map<String, Object>> rowData) {
		if (rowData.isEmpty()) {
            return 0;
        }
		String tableName = getTableName(sheetName);
		if(isTableExists(tableName,rowData.get(0))) {
    	}
		System.out.println(tableName);
        int rowsProcessed = 0;
        String sql = getInsertQuery(tableName, rowData.get(0));

        for (Map<String, Object> row : rowData) {
            Object[] values = row.values().toArray();
            try {
                jdbcTemplate.update(sql, values);
                rowsProcessed++;
            } catch (Exception e) {
                System.err.println("Error inserting row into " + sheetName + ": " + e.getMessage());
            }
        }
        return rowsProcessed;
    }

    private String getInsertQuery(String tableName, Map<String, Object> firstRow) {
        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(tableName).append(" (");

        // Column names
        String columnNames = String.join(", ", firstRow.keySet());
        query.append(columnNames).append(") VALUES (");

        // Placeholders for values
        query.append("?, ".repeat(firstRow.size()));
        query.setLength(query.length() - 2); // Remove last comma
        query.append(")");

        return query.toString();
    }

    private String getTableName(String sheetName) {
    	return sheetName.toUpperCase().replace(" ", "_");
    }

    private boolean isTableExists(String tableName, Map<String, Object> firstRow) {
    	if(!ExcelServiceConstants.TABLE_RECORDS.containsKey(tableName)) {
			throw new RuntimeException("Table - "+ tableName +" does not exists for the given sheet.");
		}
    	List<String> columnsNames = ExcelServiceConstants.TABLE_RECORDS.get(tableName);
    	for(String column: firstRow.keySet()) {
    		if(!columnsNames.contains(column)) {
    			throw new RuntimeException("Column - "+ column +" does not exists for the given sheet.");
    		}
    	}
    	return true;
    }
}
