package part1;


import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.SwipeApi;
import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;

public class SingleClient1 {
    private static String url = "http://52.41.206.117:8080/twinder/swipe";

    public static void main(String[] args) {
        run(new MultithreadClient1());
    }
    public static void run(MultithreadClient1 obj) throws RuntimeException{
        SwipeApi swipeApi = new SwipeApi();
        io.swagger.client.model.SwipeDetails body= new io.swagger.client.model.SwipeDetails();
        ApiClient apiClient = swipeApi.getApiClient();
        String[] strs = generateRandom();
        apiClient.setBasePath(url);
        body.setSwiper(strs[1]);
        body.setSwipee(strs[2]);
        body.setComment(strs[3]);
        try{
            //return status code
            int result = swipeApi.swipe(body,strs[0]);
            //retry times maximum
            int i = 0;
            while(result / 100 != 2 && i < 5){
                result = swipeApi.swipe(body,strs[0]);
                i++;
            }
            //record successful and unsuccessful request counts
            if(result/100 == 2) obj.getSuccessCount().getAndIncrement();
            else obj.getFailCount().getAndIncrement();






        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

    }
    //generate random data
    public static String[] generateRandom(){
        String[] str = new String [4];

        String leftorright = new SecureRandom().nextInt(2) == 0 ? "left" : "right";
        String swiper = String.valueOf(new SecureRandom().nextInt(5000) + 1);
        String swipee = String.valueOf(new SecureRandom().nextInt(1000000) + 1);
        String comment = RandomStringUtils.randomAlphabetic(256);
        str[0] = leftorright;
        str[1] = swiper;
        str[2] = swipee;
        str[3] = comment;
        return str;

    }

}
