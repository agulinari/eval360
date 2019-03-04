package com.gire.eval360.evaluations.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

@EnableReactiveMongoRepositories
public class MongoConfiguration extends AbstractReactiveMongoConfiguration {
	
	@Value("${mongodb.dbname}")
	private String databaseName;
	
	@Value("${spring.data.mongodb.uri}")
	private String mongoUri;

	@Override
	public MongoClient reactiveMongoClient() {
		return MongoClients.create(mongoUri);
	}

	@Override
	protected String getDatabaseName() {
		return databaseName;
	}
	
}
