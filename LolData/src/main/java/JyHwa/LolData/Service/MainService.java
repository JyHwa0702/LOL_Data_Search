package JyHwa.LolData.Service;

import JyHwa.LolData.Controller.LeagueAPIController;
import JyHwa.LolData.Controller.MatchAPIController;
import JyHwa.LolData.Controller.SummonerAPIController;
import JyHwa.LolData.Dto.LeagueEntryDto.LeagueEntryDto;
import JyHwa.LolData.Dto.LolUrl;
import JyHwa.LolData.Dto.SummonerDto;
import JyHwa.LolData.Dto.UserDto;
import JyHwa.LolData.Entity.Summoner;
import JyHwa.LolData.Entity.User;
import JyHwa.LolData.Repository.SummonerRepository;
import JyHwa.LolData.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {

    private final SummonerRepository summonerRepository;
    private final UserRepository userRepository;
    private final SummonerService summonerService;
    private final LeagueService leagueService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LolUrl lolUrl = new LolUrl();


    @Transactional
    public void saveSummoner(SummonerDto summonerDto) {
        summonerDto.setCheckField(1);
        summonerRepository.save(summonerDto.toEntity());
    }

    @Transactional
    public UserDto saveUser(UserDto userDto) {
        userDto.setCheckField(1);
        User user = userRepository.save(userDto.toEntity());
        return userDto;
    }

    @Transactional
    public List<User> FindBycheckField(int checkField) {
        List<User> users = userRepository.findByCheckField(checkField);
        return users;
    }

    @Transactional
    public UserDto SearchBySummonerName(String summonerName) {
        SummonerDto summonerDto = summonerService.callRiotAPISummonerByName(summonerName);
        LeagueEntryDto[] leagueEntryDtos = leagueService.LeagueBySummonerId(summonerDto.getId());
        UserDto userDto = new UserDto();

        UserDtoBysummonerDtoAndLeagueEntryDtos(userDto, summonerDto, leagueEntryDtos);

        return userDto;
    }

    @Transactional
    public void UserDtoBysummonerDtoAndLeagueEntryDtos(UserDto userDto, SummonerDto summonerDto, LeagueEntryDto[] leagueEntryDtos) {
        LeagueEntryDto leagueEntryDto = leagueEntryDtos[0]; //솔로큐

        userDto.setSummonerName(summonerDto.getName());
        userDto.setQueueType(leagueEntryDto.getQueueType());
        userDto.setTier(leagueEntryDto.getTier());
        userDto.setRank(leagueEntryDto.getRank());
        userDto.setLeaguePoints(leagueEntryDto.getLeaguePoints());
        userDto.setWins(leagueEntryDto.getWins());
        userDto.setLosses(leagueEntryDto.getLosses());
        userDto.setPuuid((summonerDto.getPuuid()));
        userDto.setProfileIconId(summonerDto.getProfileIconId());
        userDto.setSummonerLevel(summonerDto.getSummonerLevel());
        userDto.setCheckField(summonerDto.getCheckField());
    }

    @Transactional
    public String showRankedEmblemByTier(String tier) {
        String rankedEmblem = null;

        switch (tier) {
            case "IRON":
                rankedEmblem = "emblem-iron.png";
                break;
            case "BRONZE":
                rankedEmblem = "emblem-bronze.png";
                break;
            case "SILVER":
                rankedEmblem = "emblem-silver.png";
                break;
            case "GOLD":
                rankedEmblem = "emblem-gold.png";
                break;
            case "PLATINUM":
                rankedEmblem = "emblem-platinum.png";
                break;
            case "DIAMOND":
                rankedEmblem = "emblem-diamond.png";
                break;
            case "MASTER":
                rankedEmblem = "emblem-master.png";
                break;
            case "GRANDMASTER":
                rankedEmblem = "emblem-grandmaster.png";
                break;
            case "CHALLENGER":
                rankedEmblem = "emblem-challenger.png";
                break;
            case "UNRANKED":
                rankedEmblem = "emblem-provisional.png";
                break;

        }
        return rankedEmblem;
    }

    @Transactional
    public String showProfileIconUrlByUserDto(UserDto userDto) {
        int profileIconId = userDto.getProfileIconId();
        String profileIconsUrl = String.format("%s/profileicon/%d.png", lolUrl.getImgUrl(), profileIconId);
        //http://ddragon.leagueoflegends.com/cdn/profileicon/3584.png
        return profileIconsUrl;
    }

//    public JsonNode getSpellData() {
//        String spellDataUrl = lolUrl.getLoljsonUrl() + "summoner.json";
//
//        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
//            HttpGet request = new HttpGet(spellDataUrl);
//            try (CloseableHttpResponse response = httpClient.execute(request)) {
//                if (response.getStatusLine().getStatusCode() != 200) {
//                    System.out.println("MainService클래스 getSpellData메서드 try문 안에 !=200");
//                    return null;
//                }
//
//                HttpEntity entity = response.getEntity();
//                JsonNode spellData = objectMapper.readTree(entity.getContent());
//                return spellData;
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    @Transactional
//    public String getSpellImageUrlByKey(String spellKey) {
//        JsonNode spellData = getSpellData();
//        if (spellData == null) {
//            return null;
//        }
//
//        //'data'속성의 노드 가져오기
//        JsonNode dataNode = spellData.get("data");
//
//        if (dataNode == null) {
//            return null;
//        }
//
//        //'data' 노드의 모든 요소를 반복
//        for (Iterator<JsonNode> it = dataNode.elements(); it.hasNext(); ) {
//
//            //현재 요소 가져옴
//            JsonNode spellNode = it.next();
//
//            //현재 요소가 문제없고,'key'속성이 있고, 주어진 스펠 키 값과 일치하면
//            if (spellNode != null && spellNode.get("key") != null && spellNode.get("key").asText().equals(spellKey)) {
//                //'id'속성에 이름 가져오기
//                String spellId = spellNode.get("id").get("full").asText();
//
//
//                return String.format("%s/spell/%s.png", lolUrl.getLolImgUrl(), spellId);
//            }
//        }
//        return null;
//    }
}