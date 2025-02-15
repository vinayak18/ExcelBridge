package com.common.excel.serviceImpl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.common.excel.model.SaveSheetResponse;
import com.common.excel.model.User;
import com.common.excel.repository.DatabaseRepository;
import com.common.excel.service.ExcelService;

@Service
public class ExcelServiceImpl implements ExcelService {
	
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Autowired
    private DatabaseRepository databaseRepository; // Assume this handles DB operations


	@Override
	public ByteArrayOutputStream exportDataToExcel(List<User> users) throws Exception {
		Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Users");

        // Header Row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Id");
        header.createCell(1).setCellValue("Name");
        header.createCell(2).setCellValue("Email");

        // Data Rows
        int rowIndex = 1;
        for (User user : users) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getName());
            row.createCell(2).setCellValue(user.getEmail());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out;
	}
	
	@Override
    public Map<String, Object> processExcelFile(MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        List<SaveSheetResponse> sheetDetails = new ArrayList<>();

        int totalRowsProcessed = 0;

        try (InputStream inputStream = file.getInputStream(); 
        		XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            List<CompletableFuture<SaveSheetResponse>> futures = new ArrayList<>();
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                int sheetIndex = i;
                futures.add(CompletableFuture.supplyAsync(() -> processSheet(workbook, sheetIndex), executorService));
            }

            CompletableFuture<Void> allFutures = CompletableFuture
                    .allOf(futures.toArray(new CompletableFuture[0]));

            CompletableFuture<List<SaveSheetResponse>> finalResults = allFutures.thenApply(v ->
                futures.stream().map(CompletableFuture::join).collect(Collectors.toList())
            );

            sheetDetails = finalResults.join();
            totalRowsProcessed = sheetDetails.stream()
                    .mapToInt(SaveSheetResponse::getRowsProcessed)
                    .sum();

        } catch (Exception e) {
            response.put("status", "Failure");
            response.put("error", e.getMessage());
            return response;
        }

        response.put("status", "Success");
        response.put("details", sheetDetails);
        response.put("total_rows_processed", totalRowsProcessed);
        return response;
    }

    private SaveSheetResponse processSheet(Workbook workbook, int sheetIndex) {
    	List<String> errors = new ArrayList<>();
        int rowsProcessed = 0;
        String sheetName = "";

        try {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            sheetName = sheet.getSheetName();

            List<Map<String, Object>> rowData = new ArrayList<>();
            Iterator<Row> rowIterator = sheet.iterator();
            if (!rowIterator.hasNext()) {
            	return new SaveSheetResponse(sheetName, rowsProcessed, errors);
            }

            Row headerRow = rowIterator.next();
            List<String> headers = new ArrayList<>();
            headerRow.forEach(cell -> headers.add(cell.getStringCellValue()));
            
            int rowIndex = 1;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Map<String, Object> rowMap = new HashMap<>();
                boolean isValidRow = true;
                for (int i = 0; i < headers.size(); i++) {
                	try {
                        Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        rowMap.put(headers.get(i), getCellValueAsString(cell));
                    } catch (Exception e) {
                    	isValidRow = false;
                        errors.add("Row #" + rowIndex + " - " + "Cell #" + i + " | " + headers.get(i) + " : " + e.getMessage());
                    }
                }
                if(isValidRow) {
                	rowData.add(rowMap);
                }
                rowsProcessed++;
                rowIndex++;
            }

            // Simulate saving data to database
            databaseRepository.saveSheetData(sheetName, rowData);

        } catch (Exception e) {
            errors.add("Sheet Index " + sheetIndex + ": " + e.getMessage());
        }

        return new SaveSheetResponse(sheetName, rowsProcessed, errors);
    }

    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            case FORMULA: return cell.getCellFormula();
            default: return "";
        }
    }
	
}
