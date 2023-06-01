package JyHwa.LolData.Service;

import JyHwa.LolData.Dto.LolUrl;
import JyHwa.LolData.Dto.MatchDto.MatchDto;
import JyHwa.LolData.Dto.MatchDto.ParticipantDto;
import com.fasterxml.jackson.databind.JsonNode;
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
import org.springframework.ui.Model;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service
@PropertySource(ignoreResourceNotFound = false,value = "classpath:riotApiKey.properties")
public class MatchService {
    private ObjectMapper objectMapper = new ObjectMapper();
    private final LolUrl lolUrl = new LolUrl();

    @Value("${riot.api.key}")
    private String myKey;

    private final String serverUrl = "https://asia.api.riotgames.com/lol/match/v5/matches";
    @Transactional
    public String[] callRiotAPIMatchIdByPuuid(String puuid){

        String[] matchIds;
        try{
            CloseableHttpClient client = HttpClientBuilder.create().build();

            String url = String.format("%s/by-puuid/%s/ids?type=ranked&start=0&count=20&api_key=%s",serverUrl,puuid,myKey);
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
    public Set<String> extractSpellKeysFromMatches(List<MatchDto> matchDtos){

        Set<String> spellKeys = new HashSet<>();
        for(MatchDto matchDto : matchDtos){
            List<ParticipantDto> participantDtos = matchDto.getInfo().getParticipants();

            for(ParticipantDto participantDto : participantDtos){
                int summoner1Id = participantDto.getSummoner1Id();
                spellKeys.add(String.valueOf(summoner1Id));
                int summoner2Id = participantDto.getSummoner2Id();
                spellKeys.add(String.valueOf(summoner2Id));
            }
        }
        return spellKeys;
    }

    public void modelSpellUrlBySpellKey(Set<String> spellKeys, Model model){
        Map<String,String> spellKeyUrlMap = new HashMap<>();

        for(String spellKey: spellKeys){
            String spellImageUrlByKey = getSpellImageUrlByKey(spellKey);
            spellKeyUrlMap.put(spellKey,spellImageUrlByKey);
        }
        model.addAttribute("spellKeyUrlMap",spellKeyUrlMap);
    }


    public MatchDto callRiotAPIMatchByMatchId(String matchId){
        MatchDto matchDto;
        try{

            CloseableHttpClient client = HttpClientBuilder.create().build();

            String url = String.format("%s/%s?api_key=%s",serverUrl,matchId,myKey);
            HttpGet request = new HttpGet(url);

            CloseableHttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            matchDto = objectMapper.readValue(entity.getContent(),MatchDto.class);


        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return matchDto;
    }
    public List<MatchDto> callRiotAPIMatchsByMatchIds(String matchIds[]){
        List<MatchDto> matchDtos = new ArrayList<>();
        for (String matchId : matchIds) {
            MatchDto matchDto = callRiotAPIMatchByMatchId(matchId);
            matchDtos.add(matchDto);
        }
        return matchDtos;
    }
    public JsonNode getSpellData() {
        String spellDataUrl = lolUrl.getJsonUrl() + "summoner.json";

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(spellDataUrl);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() != 200) {
                    System.out.println("MatchService클래스 getSpellData메서드 try문 안에 !=200");
                    return null;
                }

                HttpEntity entity = response.getEntity();
                JsonNode spellData = objectMapper.readTree(entity.getContent());
                return spellData;

            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public String getSpellImageUrlByKey(String spellKey) {
        JsonNode spellData = getSpellData();
        if (spellData == null) {
            return null;
        }

        //'data'속성의 노드 가져오기
        JsonNode dataNode = spellData.get("data");

        if (dataNode == null) {
            return null;
        }

        //'data' 노드의 모든 요소를 반복
        for (Iterator<JsonNode> it = dataNode.elements(); it.hasNext(); ) {

            //현재 요소 가져옴
            JsonNode spellNode = it.next();

            //현재 요소가 문제없고,'key'속성이 있고, 주어진 스펠 키 값과 일치하면
            if (spellNode != null && spellNode.get("key") != null && spellNode.get("key").asText().equals(spellKey)) {
                //'id'속성에 이름 가져오기
                String spellId = spellNode.get("id").asText();
                return String.format("%s/spell/%s.png", lolUrl.getImgUrl(), spellId);
            }
        }
        return null;
    }


}
