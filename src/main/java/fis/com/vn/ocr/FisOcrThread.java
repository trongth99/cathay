/**
 * 
 */
package fis.com.vn.ocr;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author ChinhVD4
 *
 */
@Component
public class FisOcrThread {
	@Autowired CallApi callApi;
	@Async
	public CompletableFuture<String> request(String base64Image) {
		String jsonString = callApi.requestCmtCccd(base64Image);
		return CompletableFuture.completedFuture(jsonString);
	}
}
