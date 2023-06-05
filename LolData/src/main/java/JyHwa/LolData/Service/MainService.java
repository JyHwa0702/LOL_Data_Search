package JyHwa.LolData.Service;

import JyHwa.LolData.Dto.LeagueEntryDto.LeagueEntryDto;
import JyHwa.LolData.Dto.LolUrl;
import JyHwa.LolData.Dto.MatchDto.MatchDto;
import JyHwa.LolData.Dto.SummonerDto;
import JyHwa.LolData.Dto.UserDto;
import JyHwa.LolData.Entity.User;
import JyHwa.LolData.Repository.SummonerRepository;
import JyHwa.LolData.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MainService {

    private final SummonerRepository summonerRepository;
    private final UserRepository userRepository;
    private final SummonerService summonerService;
    private final LeagueService leagueService;
    private final MatchService matchService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LolUrl lolUrl = new LolUrl();


    public void saveSummoner(SummonerDto summonerDto) {
        summonerDto.setCheckField(1);
        summonerRepository.save(summonerDto.toEntity());
    }
    public UserDto saveUser(UserDto userDto) {
        userDto.setCheckField(1);
        User user = userRepository.save(userDto.toEntity());
        return userDto;
    }
    public List<User> FindBycheckField(int checkField) {
        List<User> users = userRepository.findByCheckField(checkField);
        return users;
    }
    public UserDto SearchBySummonerName(String summonerName,Model model) {
        SummonerDto summonerDto = summonerService.callRiotAPISummonerByName(summonerName);
        LeagueEntryDto[] leagueEntryDtos = leagueService.LeagueBySummonerId(summonerDto.getId());
        UserDto userDto = new UserDto();

        UserDtoBysummonerDtoAndLeagueEntryDtos(userDto, summonerDto, leagueEntryDtos);
        model.addAttribute("user",userDto);
        return userDto;
    }
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
    public List<MatchDto> matchDtosByUserPuuid(String puuId,Model model) {

        String[] matchIds = matchService.callRiotAPIMatchIdByPuuid(puuId);
        List<MatchDto> matchDtos = matchService.callRiotAPIMatchsByMatchIds(matchIds);
        model.addAttribute("matchDtos", matchDtos);
        return matchDtos;
    }
    public void showRankedEmblemByTier(String tier,Model model) {
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
        model.addAttribute("rankedEmblem",rankedEmblem); //랭크 엠블럼 표시
    }
    public void showProfileIconUrlByUserDto(UserDto userDto,Model model) {
        int profileIconId = userDto.getProfileIconId();
        String profileIconUrl = String.format("%s/profileicon/%d.png", lolUrl.getImgUrl(), profileIconId);
        //http://ddragon.leagueoflegends.com/cdn/profileicon/3584.png
        model.addAttribute("profileIconUrl",profileIconUrl); //프로필 아이콘 표시
    }
    public void showSpellImageUrlByMatchDtos(List<MatchDto> matchDtos, Model model) {
        Set<Integer> spellKeys = matchService.extractSpellKeysFromMatches(matchDtos); //매치의 스펠키 가져옴
        Map<Integer, String> spellImagesUrlBySpellKey = matchService.spellImagesUrlBySpellKey(spellKeys);//스펠키로 스펠이름 뽑음
        model.addAttribute("spellImagesUrlBySpellKey", spellImagesUrlBySpellKey);
    }
    public void showRuneImageUrlByMatchDtos(List<MatchDto> matchDtos,Model model){
        Set<Integer> mainRuneIds = matchService.extractMainRuneIdsFromMatches(matchDtos);
        Set<Integer> subRuneIds = matchService.extractSubRuneIdsFromMatches(matchDtos);

        Map<Integer, String> mainRuneImagesUrlByRuneId = matchService.mainRuneImageUrlByRuneId(mainRuneIds);
        Map<Integer, String> subRuneImagesUrlByRuneId = matchService.subRuneImageUrlByRuneId(subRuneIds);
        model.addAttribute("mainRuneImagesUrlByRuneId",mainRuneImagesUrlByRuneId);
        model.addAttribute("subRuneImagesUrlByRuneId",subRuneImagesUrlByRuneId);


    }
    public void showChampionImageUrlByMatchDtos(List<MatchDto> matchDtos,Model model) {
        Set<String> championNames = matchService.extractChampionNameFromMatches(matchDtos); //매치에 있는 챔피언들 불러옴
        Map<String, String> championImagesUrlByChampionNames = matchService.championImagesUrlByChampionNames(championNames); //챔피언 이름으로 url뽑아오는 거 map에 담음
        model.addAttribute("championImagesUrlByChampionNames", championImagesUrlByChampionNames);
    }
    public void showItemImageUrlByMatchDtos(List<MatchDto> matchDtos,Model model){
        Set<Integer> itemCodes = matchService.extractItemCodeFromMatches(matchDtos);
        Map<Integer, String> itemImagesUrlByMatchDtos = matchService.itemImagesUrlByMatchDtos(itemCodes);
        model.addAttribute("itemImagesUrlByMatchDtos",itemImagesUrlByMatchDtos);

    }
}