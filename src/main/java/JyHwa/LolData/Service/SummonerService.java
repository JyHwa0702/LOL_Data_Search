package JyHwa.LolData.Service;

import JyHwa.LolData.Dto.SummonerDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;

@Service
@PropertySource(ignoreResourceNotFound = false, value="classpath:riotApiKey.properties")
public class SummonerService {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${riot.api.key}")
    private String myKey;
    private final String serverUrl =  "https://kr.api.riotgames.com";
    @Transactional
    public SummonerDto callRiotAPISummonerByName(String summonerName){

        summonerName = summonerName.replaceAll(" ", "%20");//빈공간 %20으로 바꾸기

        SummonerDto result;

        try{
            CloseableHttpClient client = HttpClientBuilder.create().build();


            String url = String.format("%s/lol/summoner/v4/summoners/by-name/%s?api_key=%s",serverUrl,summonerName,myKey);
            HttpGet request = new HttpGet(url);
            CloseableHttpResponse response = client.execute(request);

            if(response.getStatusLine().getStatusCode() != 200){
                System.out.println("not 200 null");
                return null;
            }

            HttpEntity entity =response.getEntity();
            result = objectMapper.readValue(entity.getContent(), SummonerDto.class);

        }catch(IOException e) {
            e.printStackTrace();
            System.out.println("null");
            return null;
        }
        System.out.println("result = "+result);
    return result;
    }


}
