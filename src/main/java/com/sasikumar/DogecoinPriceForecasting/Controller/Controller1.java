package com.sasikumar.DogecoinPriceForecasting.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sasikumar.DogecoinPriceForecasting.Model.Forecast;
import com.sasikumar.DogecoinPriceForecasting.Service.GiveForecast;
@CrossOrigin(origins = "https://dogecoin-forecasting-master.vercel.app")
@RestController
@RequestMapping("dogecoin")

public class Controller1 {
	@Autowired
	GiveForecast service;
	
	@GetMapping("forecast")
	public List<Forecast> forcast(){
		return service.getForecast();
		
	}

}


