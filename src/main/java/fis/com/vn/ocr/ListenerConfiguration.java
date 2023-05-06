/**
 * 
 */
package fis.com.vn.ocr;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author ChinhVD4
 *
 */
@Configuration
@EnableAsync
public class ListenerConfiguration implements AsyncConfigurer {
	/**
	 * khoi tao thread
	 */
//	@Bean
//    TaskExecutor taskExecutor() {
//        return new SimpleAsyncTaskExecutor();
//    }

	/**
	 * khoi tao thread pool
	 * 
	 * @return
	 */
	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(50);
		executor.setMaxPoolSize(100);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("MyExecutor-");
		executor.initialize();
		return executor;
	}
}
