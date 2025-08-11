package com.sasikumar.DogecoinPriceForecasting.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

@Service
public class Worker {

	public boolean isForecastedToday(String minDateStr) {
		// Convert String to LocalDate (adjust format if needed)
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	    LocalDate minDate = LocalDate.parse(minDateStr, formatter);
        // Compare with today
        return minDate.equals(LocalDate.now());}

	public void forecast() {
		String scriptPath="untitled23.py"; 
		System.out.println("I am Doing this!!!!!!!!!!!!!!!");
		try {
            ProcessBuilder pb = new ProcessBuilder("python", scriptPath);
            pb.inheritIO(); // Optional: comment this out if you want it completely silent
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}

}
