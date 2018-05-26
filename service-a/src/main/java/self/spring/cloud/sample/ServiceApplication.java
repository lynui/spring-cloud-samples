package self.spring.cloud.sample;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@EnableDiscoveryClient
@SpringBootApplication
@EnableHystrix
@EnableHystrixDashboard
@MapperScan("self.spring.cloud.sample.dao.client")
public class ServiceApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ServiceApplication.class).web(true).run(args);
	} 
}
