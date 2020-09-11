package io.yelp.project;

import redis.clients.jedis.HostAndPort;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ApplicationProperties {

	private static final String PATH = "/application.properties";
	private final int threadsCount;
	private final int queriesCount;
	private final Set<HostAndPort> hostsAndPorts;
	private final String memcachedHosts;

	public ApplicationProperties(String configFile) {
		var properties = new Properties();

		int tempThreadsCount;
		int tempQueriesCount;
		Set<HostAndPort> tempHostsAndPorts;

		try {
			properties.load(new FileInputStream(configFile));

			tempThreadsCount = integer(properties, "count.threads");
			tempQueriesCount = integer(properties, "count.queries");
			tempHostsAndPorts = readHostsAndPorts(properties);

		} catch (IOException e) {
			e.printStackTrace();

			tempThreadsCount = 64;
			tempQueriesCount = 10;
			tempHostsAndPorts = Collections.emptySet();
		}

		threadsCount = tempThreadsCount;
		queriesCount = tempQueriesCount;
		hostsAndPorts = tempHostsAndPorts;

		this.memcachedHosts = properties.getProperty("memcached.servers").replaceAll("\"", "");
	}

	private Set<HostAndPort> readHostsAndPorts(Properties properties) {
		Map<String, Map.Entry<String, Integer>> nodesHostsAndPorts = new HashMap<>();

		properties.forEach((keyObject, valueObject) -> {
			String key = (String) keyObject;
			String value = (String) valueObject;

			if (key.startsWith("redis.cluster")) {
				var name = key.split("\\.")[2];

				var existingEntry = nodesHostsAndPorts.getOrDefault(name, Map.entry("", -1));

				if (key.endsWith("host")) {
					nodesHostsAndPorts.put(name, Map.entry(value, existingEntry.getValue()));
				} else if (key.endsWith("port")) {
					nodesHostsAndPorts.put(name, Map.entry(existingEntry.getKey(), Integer.parseInt(value)));
				}
			}
		});

		return nodesHostsAndPorts.values().stream()
				.map(entry -> new HostAndPort(entry.getKey(), entry.getValue()))
				.collect(Collectors.toUnmodifiableSet());
	}

	private int integer(Properties properties, String intProperty) {
		return Integer.parseInt(properties.getProperty(intProperty));
	}

	public int getThreadsCount() {
		return threadsCount;
	}

	public int getQueriesCount() {
		return queriesCount;
	}

	public String getMemcachedHosts() {
		return memcachedHosts;
	}

	public Set<HostAndPort> getHostsAndPorts() {
		return hostsAndPorts;
	}

	@Override
	public String toString() {
		return "ApplicationProperties{" +
				"threadsCount=" + threadsCount +
				", queriesCount=" + queriesCount +
				", hostsAndPorts=" + hostsAndPorts +
				'}';
	}
}
