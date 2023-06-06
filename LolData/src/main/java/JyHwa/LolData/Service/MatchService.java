package JyHwa.LolData.Service;

import JyHwa.LolData.Dto.LolUrl;
import JyHwa.LolData.Dto.MatchDto.MatchDto;
import JyHwa.LolData.Dto.MatchDto.ParticipantDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

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
    public Set<Integer> extractMainRuneIdsFromMatches(List<MatchDto> matchDtos) {
        Set<Integer> mainRuneIds = new HashSet<>();
        for (MatchDto matchDto : matchDtos) {
            List<ParticipantDto> participantDtos = matchDto.getInfo().getParticipants();

            for (ParticipantDto participantDto : participantDtos) {
                int mainRuneId = participantDto.getPerks().getStyles().get(0).getStyle();
                mainRuneIds.add(mainRuneId);
            }
        }
        return mainRuneIds;
    }
    public Set<Integer> extractSubRuneIdsFromMatches(List<MatchDto> matchDtos) {
        Set<Integer> subRuneIds = new HashSet<>();
        for (MatchDto matchDto : matchDtos) {
            List<ParticipantDto> participantDtos = matchDto.getInfo().getParticipants();

            for (ParticipantDto participantDto : participantDtos) {
                int mainRuneId = participantDto.getPerks().getStyles().get(1).getStyle();
                subRuneIds.add(mainRuneId);
            }
        }
        return subRuneIds;
    }
    public Set<Integer> extractSpellKeysFromMatches(List<MatchDto> matchDtos){
        Set<Integer> spellKeys = new HashSet<>();
        for(MatchDto matchDto : matchDtos){
            List<ParticipantDto> participantDtos = matchDto.getInfo().getParticipants();

            for(ParticipantDto participantDto : participantDtos){
                int summoner1Id = participantDto.getSummoner1Id();
                spellKeys.add(summoner1Id);
                int summoner2Id = participantDto.getSummoner2Id();
                spellKeys.add(summoner2Id);
            }
        }
        return spellKeys;
    }
    public Set<String> extractChampionNameFromMatches(List<MatchDto> matchDtos){
        Set<String> championNames =new HashSet<>();
        for (MatchDto matchDto : matchDtos){
            List<ParticipantDto> participants = matchDto.getInfo().getParticipants();

            for(ParticipantDto participantDto : participants){
                String championName = participantDto.getChampionName();
                championNames.add(championName);
            }
        }
        return championNames;
    }
    public Set<Integer> extractItemCodeFromMatches(List<MatchDto> matchDtos){
        Set<Integer> itemCodes = new HashSet<>();
        for(MatchDto matchDto:matchDtos){
            List<ParticipantDto> participants = matchDto.getInfo().getParticipants();
            for (ParticipantDto participantDto : participants){
                int item0 = participantDto.getItem0();
                int item1 = participantDto.getItem1();
                int item2 = participantDto.getItem2();
                int item3 = participantDto.getItem3();
                int item4 = participantDto.getItem4();
                int item5 = participantDto.getItem5();
                int item6 = participantDto.getItem6();

                itemCodes.add(item0);
                itemCodes.add(item1);
                itemCodes.add(item2);
                itemCodes.add(item3);
                itemCodes.add(item4);
                itemCodes.add(item5);
                itemCodes.add(item6);
            }
        }
        return itemCodes;
    }
    public Map<Integer,String> mainRuneImageUrlByRuneId(Set<Integer> mainRuneIds) {
        Map<Integer,String> mainRuneIdUrlMap = new HashMap<>();

        for(int mainRuneId : mainRuneIds) {
            System.out.println("mainRuneKey = "+mainRuneId);
            String mainRuneImageUrlByKey = getMainRuneImageUrlByKey(mainRuneId);
            mainRuneIdUrlMap.put(mainRuneId,mainRuneImageUrlByKey);
        }
        return mainRuneIdUrlMap;
    }
    public Map<Integer,String> subRuneImageUrlByRuneId(Set<Integer> subRuneIds) {
        Map<Integer,String> subRuneIdUrlMap = new HashMap<>();

        for(int subRuneId : subRuneIds) {
            System.out.println("subRuneKey = "+subRuneId);
            String mainRuneImageUrlByKey = getSubRuneImageUrlByKey(subRuneId);
            subRuneIdUrlMap.put(subRuneId,mainRuneImageUrlByKey);
        }
        return subRuneIdUrlMap;
    }
    public Map<Integer,String> spellImagesUrlBySpellKey(Set<Integer> spellKeys){
        Map<Integer,String> spellKeyUrlMap = new HashMap<>();

        for(int spellKey: spellKeys){
            System.out.println("spellKey="+spellKey);
            String spellImageUrlByKey = getSpellImageUrlByKey(spellKey);
            spellKeyUrlMap.put(spellKey,spellImageUrlByKey);
        }
        System.out.println("spellKeyUrlMap.get(14) = "+spellKeyUrlMap.get(14));
        return spellKeyUrlMap;
    }
    public Map<String,String> championImagesUrlByChampionNames(Set<String> championName){
        Map<String,String> championImagesUrlMap = new HashMap<>();
        for (String chapionName:championName){
            String championImageUrl = String.format("%s/champion/%s.png",lolUrl.getImgUrl(),chapionName);
            //https://ddragon.leagueoflegends.com/cdn/10.6.1/img/champion/<champion_name>.png
            championImagesUrlMap.put(chapionName,championImageUrl);
        }
        return championImagesUrlMap;
    }
    public Map<Integer,String> itemImagesUrlByMatchDtos(Set<Integer> itemCodes){
        Map<Integer,String> itemImagesUrlMap = new HashMap<>();
        for(int itemcode : itemCodes){
            String itemImageUrl = String.format("%s/item/%s.png", lolUrl.getImgUrl(),itemcode);
            //https://ddragon.leagueoflegends.com/cdn/10.6.1/img/item/3108.png
            itemImagesUrlMap.put(itemcode,itemImageUrl);
        }
        return itemImagesUrlMap;
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
        String spellDataUrl = lolUrl.getJsonUrl() + "/summoner.json"; //http://ddragon.leagueoflegends.com/cdn/13.10.1/data/ko_KR/summoner.json

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
    public JsonNode getRuneData(){
        String spellDataUrl = lolUrl.getJsonUrl() + "/runesReforged.json"; //http://ddragon.leagueoflegends.com/cdn/13.10.1/data/ko_KR/runesReforged.json


        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(spellDataUrl);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() != 200) {
                    System.out.println("MatchService클래스 getRuneData메서드 try문 안에 !=200");
                    return null;
                }

                HttpEntity entity = response.getEntity();
                JsonNode runeData = objectMapper.readTree(entity.getContent());
                return runeData;

            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String getSpellImageUrlByKey(int spellKey) {
        JsonNode spellData = getSpellData();
        if (spellData == null) {
            System.out.println("getSpellImageUrlByKey spellData == null");
            return null;
        }

        //'data'속성의 노드 가져오기
        JsonNode dataNode = spellData.get("data");

        if (dataNode == null) {
            System.out.println("getSpellImageUrlByKey dataNode == null");
            return null;
        }

        //'data' 노드의 모든 요소를 반복
        for (Iterator<JsonNode> it = dataNode.elements(); it.hasNext(); ) {

            //현재 요소 가져옴
            JsonNode spellNode = it.next();

            String spellKey_String=""+spellKey;
            //현재 요소가 문제없고,'key'속성이 있고, 주어진 스펠 키 값과 일치하면
            if (spellNode != null && spellNode.get("key") != null && spellNode.get("key").asText().equals(spellKey_String)) {
                //'id'속성에 이름 가져오기
                String spellId = spellNode.get("id").asText();
                System.out.println("MatchService getSpellImageUrlByKey spellId = "+spellId);
                return String.format("%s/spell/%s.png", lolUrl.getImgUrl(), spellId);
            }
        }
        System.out.println("getSpellImageUrlByKey 마지막 null");
        return null;
    }

    public String getMainRuneImageUrlByKey(int mainRuneId) {
        JsonNode runeData = getRuneData();
        if (runeData == null) {
            System.out.println("getMainRuneImageUrlByKey runeData == null");
            return null;
        }

        ArrayNode mainRuneArray = (ArrayNode) runeData;
        for (JsonNode slotDataNode : mainRuneArray) {
            if (slotDataNode != null && slotDataNode.get("slots") != null) {
                JsonNode slotNode = slotDataNode.get("slots");
                if (slotNode != null && slotNode.get("runes") != null) {
                    JsonNode runeNode = slotNode.get("runes");
                    if (runeNode != null && runeNode.get("id") != null && runeNode.get("id").asText().equals(String.valueOf(mainRuneId))) {
                        String runeImageUrl = runeNode.get("icon").asText();
                        System.out.println("MatchServcie getMainRuneImageUrlKey runeImageUrl = " + runeImageUrl);
                        return String.format("%s/img/%s", lolUrl.getURL(), runeImageUrl);
                    }
                    return null;
                }
            }
        }
            System.out.println("getMainRuneImageUrlByKey 마지막 null");
            return null;
    }
//        //'slots''속성의 노드 가져오기
//        JsonNode slotsDataNode = runeData.get("slots");
//        if (slotsDataNode == null) {
//            System.out.println("getMainRuneImage SlotsDataNode == null");
//            return null;
//        }
//        //'slots' 속성 안에 'runes' 노드 가져오기
//        JsonNode runesDataNode = slotsDataNode.get("runes");
//        if (runesDataNode == null) {
//            System.out.println("getMainRuneImage runesDataNode dataNode == null");
//            return null;
//        }
//
//        //'runes' 노드의 모든 요소를 반복
//        for (Iterator<JsonNode> it = runesDataNode.elements(); it.hasNext(); ) {
//
//            //현재 요소 가져옴
//            JsonNode runeNode = it.next();
//
//            String runeKey_String=""+runeNode;
//            //현재 요소가 문제없고,'key'속성이 있고, 주어진 스펠 키 값과 일치하면
//            if (runeNode != null && runeNode.get("id") != null && runeNode.get("id").asText().equals(runeKey_String)) {
//                //'id'속성에 이름 가져오기
//                String runeImageUrl = runeNode.get("icon").asText(); //perk-images/Styles/Domination/Electrocute/Electrocute.png
//                System.out.println("MatchService getMainRuneImageUrlByKey spellId = "+runeImageUrl);
//                return String.format("%s/img/%s", lolUrl.getURL(), runeImageUrl);
//                //https://ddragon.leagueoflegends.com/cdn/img/perk-images/Styles/Precision/PressTheAttack/PressTheAttack.png
//            }
//        }
//        System.out.println("getMainRuneImageUrlByKey 마지막 null");
//        return null;


    public String getSubRuneImageUrlByKey(int subRuneKey){
        JsonNode runeData = getRuneData();
        if (runeData == null) {
            System.out.println("getSpellImageUrlByKey runeData == null");
            return null;
        }
//        //'slots''속성의 노드 가져오기
//        JsonNode slotsDataNode = runeData.get("slots");
//        if (slotsDataNode == null) {
//            System.out.println("getSlotsDataNode == null");
//        }
//        //'slots' 속성 안에 'runes' 노드 가져오기
//        JsonNode runesDataNode = slotsDataNode.get("runes");
//        if (runesDataNode == null) {
//            System.out.println("runesDataNode dataNode == null");
//            return null;
//        }

        //'runes' 노드의 모든 요소를 반복
        for (Iterator<JsonNode> it = runeData.elements(); it.hasNext(); ) {

            //현재 요소 가져옴
            JsonNode runeNode = it.next();

//            String runeKey_String=""+runeNode;
            //현재 요소가 문제없고,'key'속성이 있고, 주어진 스펠 키 값과 일치하면
            if (runeNode != null && runeNode.get("id") != null && runeNode.get("id").asText().equals(String.valueOf(runeNode))) {
                //'id'속성에 이름 가져오기
                String runeImageUrl = runeNode.get("icon").asText(); //perk-images/Styles/7203_Whimsy.png
                System.out.println("MatchService getSubRuneImageUrlByKey spellId = "+runeImageUrl);
                return String.format("%s/img/%s", lolUrl.getURL(), runeImageUrl);
                //https://ddragon.leagueoflegends.com/cdn/img/perk-images/Styles/7203_Whimsy.png
            }
        }
        System.out.println("getSubRuneImageUrlByKey 마지막 null");
        return null;
    }
}
