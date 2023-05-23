package JyHwa.LolData.Service;

import JyHwa.LolData.Dto.MatchDto.MatchDto;
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
@PropertySource(ignoreResourceNotFound = false,value = "classpath:riotApiKey.properties")
public class MatchService {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${riot.api.key}")
    private String myKey;

    private final String serverUrl = "https://asia.api.riotgames.com";

    public String[] callRiotAPIMatchIdByPuuid(String puuid){

        String[] matchIds;
        try{
            CloseableHttpClient client = HttpClientBuilder.create().build();

            String url = String.format("%s/lol/match/v5/matches/by-puuid/%s/ids?type=ranked&start=0&count=20&api_key=%s",serverUrl,puuid,myKey);
            HttpGet request = new HttpGet(url);

            CloseableHttpResponse response = client.execute(request);

            if(response.getStatusLine().getStatusCode() != 200){
                System.out.println("try문 안에 != 200");
                return null;
            }

            HttpEntity entity =response.getEntity();
            String[] responseIds = objectMapper.readValue(entity.getContent(),String[].class);
            matchIds = responseIds;

        }catch(IOException e) {
            e.printStackTrace();
            return null;
        }
        return matchIds;

    }

    public MatchDto callRiotAPIMatchByMatchId(String MatchId){
        MatchDto matchDto;

        try{

            CloseableHttpClient client = HttpClientBuilder.create().build();

            String url = String.format()

        }
    }

}
