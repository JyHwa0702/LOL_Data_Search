package JyHwa.LolData.Service;

import JyHwa.LolData.Dto.LeagueEntryDto.LeagueEntryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
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
public class LeagueService {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String serverUrl = "https://kr.api.riotgames.com/lol/league/v4";

    @Value("${riot.api.key}")
    private  String myKey;


    public LeagueEntryDto[] LeagueBySummonerId(String summonerId){
        LeagueEntryDto[] leagueEntryDto = new LeagueEntryDto[2];
        for (int i =0; i<leagueEntryDto.length; i++){
            leagueEntryDto[i] = new LeagueEntryDto();
        }

        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();

            String url = String.format("%s/entries/by-summoner/%s?api_key=%s",serverUrl,summonerId,myKey);
            HttpGet request = new HttpGet(url);
            CloseableHttpResponse response = client.execute(request);

            if (response.getStatusLine().getStatusCode() != 200){
                System.out.println("코드 200이 아닙니다.");
                return null;
            }
            HttpEntity entity = response.getEntity();

            leagueEntryDto= objectMapper.readValue(entity.getContent(), LeagueEntryDto[].class);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return leagueEntryDto;
    }
}