package com.sasikumar.DogecoinPriceForecasting.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sasikumar.DogecoinPriceForecasting.Model.Forecast;
@Repository
public interface DogecoinRepository extends JpaRepository<Forecast, String> {
	List<Forecast> findTop30ByOrderByDate();
	
	@Query("select min(f.date) from Forecast f ")
	String getMinDate();
}
