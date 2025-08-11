package com.sasikumar.DogecoinPriceForecasting.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.sasikumar.DogecoinPriceForecasting.Model.Forecast;
import com.sasikumar.DogecoinPriceForecasting.Repository.DogecoinRepository;

@Service
public class GiveForecast {
	@Autowired 
	DogecoinRepository repo;
	
	@Autowired
	Worker work;
	
	public List<Forecast> getForecast() {
	
		if(work.isForecastedToday(repo.getMinDate())) {
		// TODO Auto-generated method stub
		return repo.findTop30ByOrderByDate();}
		else {
			repo.deleteById(repo.getMinDate());
			work.forecast();
			
			return repo.findTop30ByOrderByDate();
		}
		//return null;
		
		
	}
	
	

}
