package com.moimus.casestudy;

import org.slf4j.Logger;

import spark.Spark;
import spark.Spark.*;
import org.slf4j.LoggerFactory;
import org.sql2o.Sql2o;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Main Class
 *
 */
public class App {
	public static final Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		System.out.println("Starting...");
		Spark.port(8080);
		Spark.get("/hello", (req, res) -> API.boilGet(req, res));
		Spark.post("/hello", (req, res) -> API.boilPost(req, res));
		Spark.put("/hello", (req, res) -> API.boilPut(req, res));
		logger.info("Example log from {}", App.class.getSimpleName());
	}
}
