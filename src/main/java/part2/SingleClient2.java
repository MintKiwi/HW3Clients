package part2;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import part1.MultithreadClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class SingleClient {
    private static String url = "http://34.215.96.158:8080/twinder/swipe/";


    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            executePost(new part1.MultithreadClient());
        }

        long end = System.currentTimeMillis();
        System.out.println("The total run time to send 1000 requests is " + (end - start));
        System.out.println("Response time of a requests is " + ((end - start) / 1000) + " milliseconds");
    }

    public static void executePost(MultithreadClient obj) throws Exception {
        long start = System.currentTimeMillis();
        HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                return executionCount <= 5;
            }
        };
        String leftorright = new SecureRandom().nextInt(2) == 0 ? "left" : "right";
        String swiper = String.valueOf(new SecureRandom().nextInt(5000) + 1);
        String swipee = String.valueOf(new SecureRandom().nextInt(1000000) + 1);
        String comment = RandomStringUtils.randomAlphabetic(256);
        String json = new StringBuilder()
                .append("{")
                .append("\'swiper\':\'" + swiper + "\',")
                .append("\'swipee\':\'" + swipee + "\',")
                .append("\'comment\':\'" + comment + "\'")
                .append("}").toString();
        try (CloseableHttpClient httpClient = HttpClients.custom().addInterceptorLast(new HttpResponseInterceptor() {
            @Override
            public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
                if (response.getStatusLine().getStatusCode() / 100 != 2) {
                    throw new IOException("Retry it");
                }

            }
        }).setRetryHandler(requestRetryHandler).build()) {
            final HttpPost httpPost = new HttpPost(url + leftorright);
            StringEntity requestEntity = new StringEntity(json);

            httpPost.setEntity(requestEntity);
            CloseableHttpResponse response = null;
            try {
                response = httpClient.execute(httpPost);
                StatusLine statusLine = response.getStatusLine();
                long end = System.currentTimeMillis();
                System.out.println("The total run time to send a request is " + (end - start));
                if (statusLine.getStatusCode() / 100 == 2) obj.increaseSuccessCount();
                else obj.increaseFailCount();
//                System.out.println(statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
//                System.out.println("Response body: " + responseBody);
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (response != null) response.close();
                httpClient.close();

            }
        }

    }
}

