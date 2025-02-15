package com.common.excel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.common.excel.model.User;
import com.common.excel.service.ExcelService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @GetMapping("/user/export")
    public ResponseEntity<byte[]> exportExcel() throws Exception {
        // Sample data
        List<User> users = Arrays.asList(
                new User(1, "John Doe", "john@example.com"),
                new User(2, "Jane Doe", "jane@example.com")
        );

        ByteArrayOutputStream out = excelService.exportDataToExcel(users);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=users.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(out.toByteArray());
    }
    
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadExcelFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            		.body(Map.of("status", "Failure", "message", "File is empty"));
        }
        if(!isExcelFile(file)) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        			.body(Map.of("status", "Failure", "message", "Invalid file type. Please upload an Excel file."));
        }
        try {
            Map<String, Object> response = excelService.processExcelFile(file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "Failure", "message", e.getMessage()));
        }
    }
    
    private boolean isExcelFile(MultipartFile file) {
    	String contentType = file.getContentType();
    	System.out.println(contentType);
    	return null != contentType && (contentType.equals("application/vnd.ms-excel") || contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    }

	/*
	 * @PostMapping("/import") public List<User> importExcel(@RequestParam("file")
	 * MultipartFile file) throws Exception { return
	 * excelService.importDataFromExcel(file); }
	 */
}
