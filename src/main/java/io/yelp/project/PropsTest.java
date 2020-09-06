package io.yelp.project;

public class PropsTest {
	public static void main(String[] args) {
		var applicationProperties = new ApplicationProperties();
		System.out.println(applicationProperties.getThreadsCount());
		System.out.println(applicationProperties.getQueriesCount());
		System.out.println(applicationProperties.getHostsAndPorts());
	}
}
