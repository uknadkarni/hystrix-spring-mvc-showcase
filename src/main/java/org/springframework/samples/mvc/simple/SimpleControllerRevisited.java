package org.springframework.samples.mvc.simple;

import org.apache.log4j.Logger;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
@Configuration
@Component
@Controller
@EnableCircuitBreaker
public class SimpleControllerRevisited {
	
	private Logger logger = Logger.getLogger(SimpleControllerRevisited.class);

	@RequestMapping(value="/simple/revisited", method=RequestMethod.GET, headers="Accept=text/plain")
	public @ResponseBody String simple() {
		String message = getMessage();
		return message;
	}
//	
//    @HystrixCommand(commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500")
//        })
//
    @HystrixCommand(commandProperties = {
    	    // Use this when we want to add debug, so the request will not timeout
    	    // @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",
    	    // value = "300000"),

    	    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "1"),
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500"), 
            @HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE"),
            @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "1"),
            @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "60000"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "1"),
            @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="50000")
        },fallbackMethod = "getHystrixMessage")
    private String getMessage(){
		try {
			logger.info("Sleeping....");
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String message = "Hello world revisited!";
		logger.info("Returning " + message);
    		return message;
    }
    
    private String getHystrixMessage(){
    		String message = "Hello world with Hystrix!";
    		return message;
    }
}
