package com.common.excel.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.common.excel.model.User;

public interface ExcelService {

	public ByteArrayOutputStream exportDataToExcel(List<User> users) throws Exception;
	
	public Map<String, Object> processExcelFile(MultipartFile file);
	
}
