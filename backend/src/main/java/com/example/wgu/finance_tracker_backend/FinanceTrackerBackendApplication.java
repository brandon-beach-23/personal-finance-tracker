package com.example.wgu.finance_tracker_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FinanceTrackerBackendApplication {

	public static void main(String[] args) {
		System.out.println("Starting Finance Tracker Backend...");

		SpringApplication.run(FinanceTrackerBackendApplication.class, args);
	}

}
