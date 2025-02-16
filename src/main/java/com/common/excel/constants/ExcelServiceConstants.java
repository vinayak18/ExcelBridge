package com.common.excel.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelServiceConstants {

	public static final Map<String, List<String>> TABLE_RECORDS = Collections.unmodifiableMap(getTableRecordsMap());

	public static Map<String, List<String>> getTableRecordsMap(){
		Map<String, List<String>> map = new HashMap<>();
		map.put("STUDENT_RECORDS", List.of("student_id", "name", "age", "grade", "enrollment_date"));
		map.put("VEHICLE_INFORMATION", List.of("vehicle_id", "model", "manufacturer", "registration_year", "price"));
		map.put("MOVIE_LISTINGS", List.of("movie_id", "title", "genre", "release_year", "duration_minutes"));
		map.put("WEATHER_REPORTS", List.of("report_id", "city", "temperature", "humidity", "report_date"));
		map.put("BOOK_CATALOG", List.of("book_id", "title", "author", "publication_year", "price"));
		map.put("FLIGHT_SCHEDULES", List.of("flight_id", "airline", "departure_city", "arrival_city", "departure_time"));
		map.put("MUSIC_PLAYLIST", List.of("song_id", "title", "artist", "album", "release_year"));
		map.put("RESTAURANT_MENU", List.of("item_id", "dish_name", "category", "price", "available"));
		map.put("SPACE_MISSIONS", List.of("mission_id", "mission_name", "launch_date", "destination", "duration_days"));
		map.put("TECH_GADGETS", List.of("gadget_id", "name", "brand", "release_year", "price"));
		return map;
	}
}
