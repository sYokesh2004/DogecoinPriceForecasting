package com.sasikumar.DogecoinPriceForecasting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Demo {
	@GetMapping("try")
	public String hi() {
		return"how r u";
	}
	 @GetMapping("/forecast")
	    public List<Map<String, String>> getForecast() {
	        List<Map<String, String>> forecastList = new ArrayList<>();

	        try {
	            // Assuming the CSV is in src/main/resources
	           
	            Path path = Paths.get("C:/Users/itssk/Downloads/forecast.csv");
	            List<String> lines = Files.readAllLines(path);
	            for (int i = 1; i < lines.size(); i++) { // skip header
	                String[] parts = lines.get(i).split(",");
	                Map<String, String> entry = new HashMap<>();
	                entry.put("date", parts[0].substring(0,8));
	                entry.put("close", parts[1].substring(0,8));
	                entry.put("high", parts[2].substring(0,8));
	                entry.put("low", parts[3].substring(0,8));
	                entry.put("open", parts[4].substring(0,8));
	                entry.put("volume", parts[5]);
	                forecastList.add(entry);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        return forecastList;
	    }

}
