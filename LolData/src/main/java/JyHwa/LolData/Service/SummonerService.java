package JyHwa.LolData.Service;

import JyHwa.LolData.Dto.MatchDto.MatchDto;
import JyHwa.LolData.Dto.SummonerDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@PropertySource(ignoreResourceNotFound = false, value="classpath:riotApiKey.properties")
public class SummonerService {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${riot.api.key}")
    private String myKey;
    private final String serverUrl =  "https://kr.api.riotgames.com";

    public SummonerDto callRiotAPISummonerByName(String summonerName){

        SummonerDto result;

        try{
            CloseableHttpClient client = HttpClientBuilder.create().build();


            String url = String.format("%s/lol/summoner/v4/summoners/by-name/%s?api_key=%s",serverUrl,summonerName,myKey);
            HttpGet request = new HttpGet(url);
            CloseableHttpResponse response = client.execute(request);

            if(response.getStatusLine().getStatusCode() != 200){
                return null;
            }

            HttpEntity entity =response.getEntity();
            result = objectMapper.readValue(entity.getContent(), SummonerDto.class);

        }catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    return result;
    }


}
