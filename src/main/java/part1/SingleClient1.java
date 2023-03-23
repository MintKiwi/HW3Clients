package part1;


import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.MatchesApi;
import io.swagger.client.api.StatsApi;
import io.swagger.client.api.SwipeApi;
import io.swagger.client.model.MatchStats;
import io.swagger.client.model.Matches;
import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SingleClient1 {
    private static String url_post = "http://new-101717130.us-west-2.elb.amazonaws.com/twinder/swipe";
    private static String url_match = "http://new-101717130.us-west-2.elb.amazonaws.com/twinder/matches/";
    private static String url_stats = "http://new-101717130.us-west-2.elb.amazonaws.com/twinder/stats/";

static List<Long> postLatencies = Collections.synchronizedList(new ArrayList<Long>());


    public static void main(String[] args) {
        List<Long> l = new ArrayList<Long>();
        l.add(0l);
        l.add(1l);
        l.add(3l);
        l.add(5l);
        l.add(2l);

        System.out.println(l.stream().max(Long::compare).get());
        System.out.println(l.stream().min(Long::compare).get());
        System.out.println(l.stream().count());
        System.out.println(l.stream().mapToLong(Long::longValue).average().getAsDouble());




    }

    public static void run(MultithreadClient1 obj) throws RuntimeException{
        SwipeApi swipeApi = new SwipeApi();
        io.swagger.client.model.SwipeDetails body= new io.swagger.client.model.SwipeDetails();
        ApiClient apiClient = swipeApi.getApiClient();
        String[] strs = generateRandom();
        apiClient.setBasePath(url_post);
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
//            System.out.println(result);


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
    /*
       Send a getMatchRequest request
     */
    public static void getMatchRequest(){

        MatchesApi apiInstance = new MatchesApi();
        ApiClient apiClient = apiInstance.getApiClient();
        String userID = generateRandom()[1];
        apiClient.setBasePath(url_match+userID);
        try {
            long start = System.currentTimeMillis();
            Matches result = apiInstance.matches(userID);
            //add the post latencies to the list
            postLatencies.add(System.currentTimeMillis() - start);
//            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling MatchesApi#matches");
            e.printStackTrace();
        }

    }
    /*
       Send a getMatchStats request
     */
    public static void getMatchStatsRequest() {


        StatsApi statsApi = new StatsApi();
        ApiClient apiClient1 = statsApi.getApiClient();

        String userID = generateRandom()[1];
        apiClient1.setBasePath(url_stats + userID);
        try {
            long start = System.currentTimeMillis();
            MatchStats matchStats = statsApi.matchStats(userID);
            //add the post latencies to the list
            postLatencies.add(System.currentTimeMillis() - start);
//            System.out.println(matchStats);
        } catch (ApiException e) {
            System.err.println("Exception when calling StatsApi#matchStats");
            e.printStackTrace();
        }


    }
}
