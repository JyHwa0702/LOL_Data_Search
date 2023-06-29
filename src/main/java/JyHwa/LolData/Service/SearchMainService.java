package JyHwa.LolData.Service;

import JyHwa.LolData.Dto.LeagueEntryDto.LeagueEntryDto;
import JyHwa.LolData.Dto.LolUrlDto;
import JyHwa.LolData.Dto.MatchDto.MatchDto;
import JyHwa.LolData.Dto.SummonerDto;
import JyHwa.LolData.Dto.UserDto;
import JyHwa.LolData.Entity.User;
import JyHwa.LolData.Repository.SummonerRepository;
import JyHwa.LolData.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchMainService {

    private final SummonerRepository summonerRepository;
    private final UserRepository userRepository;
    private final SummonerService summonerService;
    private final LeagueService leagueService;
    private final MatchService matchService;
    private final KakaoService kakaoServic4e;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LolUrlDto lolUrlDto = new LolUrlDto();


    public void saveSummoner(SummonerDto summonerDto) {
        summonerDto.setCheckField(1);
        summonerRepository.save(summonerDto.toEntity());
    }
    public UserDto showUserInfo(String summonerName,Model model){
        UserDto userDto = searchBySummonerName(summonerName,model); //model user,userDto
        showRankedEmblemByTier(userDto.getTier(),model); //랭크 엠블럼
        showProfileIconUrlByUserDto(userDto,model);//프로필 아이콘 표시

        return userDto;
    }

    public void showMatchInfo(UserDto userDto,Model model){
        List<MatchDto> matchDtos = matchDtosByUserPuuid(userDto, model);
        showSpellImageUrlByMatchDtos(matchDtos,model); //스펠 이미지 보여주기
        showChampionImageUrlByMatchDtos(matchDtos,model); //챔피언 이미지 보여주기
        showRuneImageUrlByMatchDtos(matchDtos,model); //룬 이미지 보여주기
        showItemImageUrlByMatchDtos(matchDtos,model); //아이템 이미지 보여주기
    }
    public User saveUser(UserDto userDto,Model model) {

        Optional<User> bySummonerName = userRepository.findBySummonerName(userDto.getSummonerName());

        if(bySummonerName.isEmpty()){
            User user = userDto.toEntity();

            User savedUser = userRepository.save(user);

            Long id = savedUser.getId();
            log.info("savedUser id = "+id);
            model.addAttribute("userId", id);
            return savedUser;
        }
        model.addAttribute("userId",userDto.getId());
        return userDto.toEntity();

    }
    public List<User> FindBycheckField(int checkField) {
        List<User> users = userRepository.findByCheckField(checkField);
        return users;
    }


    public UserDto searchBySummonerName(String summonerName, Model model) {
        System.out.println("MainSErvice = "+summonerName);
        SummonerDto summonerDto = summonerService.callRiotAPISummonerByName(summonerName);
        LeagueEntryDto[] leagueEntryDtos = leagueService.LeagueBySummonerId(summonerDto.getId());
        UserDto userDto = new UserDto();


        userDtoBysummonerDtoAndLeagueEntryDtos(userDto, summonerDto, leagueEntryDtos);
        model.addAttribute("user",userDto);
        return userDto;
    }



    public void userDtoBysummonerDtoAndLeagueEntryDtos(UserDto usersDto, SummonerDto summonerDto, LeagueEntryDto[] leagueEntryDtos) {
        LeagueEntryDto leagueEntryDto = leagueEntryDtos[0]; //솔로큐

        usersDto.setSummonerName(summonerDto.getName());
        usersDto.setQueueType(leagueEntryDto.getQueueType());
        usersDto.setTier(leagueEntryDto.getTier());
        usersDto.setRank(leagueEntryDto.getRank());
        usersDto.setLeaguePoints(leagueEntryDto.getLeaguePoints());
        usersDto.setWins(leagueEntryDto.getWins());
        usersDto.setLosses(leagueEntryDto.getLosses());
        usersDto.setPuuid((summonerDto.getPuuid()));
        usersDto.setProfileIconId(summonerDto.getProfileIconId());
        usersDto.setSummonerLevel(summonerDto.getSummonerLevel());
        usersDto.setCheckField(summonerDto.getCheckField());
    }
    public List<MatchDto> matchDtosByUserPuuid(UserDto userDto,Model model) {

        String puuid = userDto.getPuuid();
        String[] matchIds = matchService.callRiotAPIMatchIdByPuuid(puuid);
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
    public void showProfileIconUrlByUserDto(UserDto usersDto, Model model) {
        int profileIconId = usersDto.getProfileIconId();
        String profileIconUrl = String.format("%s/profileicon/%d.png", lolUrlDto.getImgUrl(), profileIconId);
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