package com.common.excel.repository.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.common.excel.repository.DatabaseRepository;

@Repository
public class JdbcDatabaseRepository implements DatabaseRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

    @Override
	public void saveSheetData(String sheetName, List<Map<String, Object>> rowData) {
		if (rowData.isEmpty()) {
            return;
        }

        int rowsProcessed = 0;
        String sql = getInsertQuery(sheetName, rowData.get(0));

        for (Map<String, Object> row : rowData) {
            Object[] values = row.values().toArray();
            try {
                jdbcTemplate.update(sql, values);
                rowsProcessed++;
            } catch (Exception e) {
                System.err.println("Error inserting row into " + sheetName + ": " + e.getMessage());
            }
        }
    }

    private String getInsertQuery(String sheetName, Map<String, Object> firstRow) {
        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(getTableName(sheetName)).append(" (");

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
    	return sheetName.replace(" ", "_");
    }

}
