package JyHwa.LolData.Service;

import JyHwa.LolData.Controller.LeagueAPIController;
import JyHwa.LolData.Controller.MatchAPIController;
import JyHwa.LolData.Controller.SummonerAPIController;
import JyHwa.LolData.Dto.LeagueEntryDto.LeagueEntryDto;
import JyHwa.LolData.Dto.SummonerDto;
import JyHwa.LolData.Dto.UserDto;
import JyHwa.LolData.Entity.Summoner;
import JyHwa.LolData.Entity.User;
import JyHwa.LolData.Repository.SummonerRepository;
import JyHwa.LolData.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {

    private final SummonerRepository summonerRepository;
    private final UserRepository userRepository;
    private final SummonerAPIController summonerAPIController;
    private final MatchAPIController matchAPIController;
    private final LeagueAPIController leagueAPIController;

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
        SummonerDto summonerDto = summonerAPIController.callSummonerByName(summonerName);
        LeagueEntryDto[] leagueEntryDtos = leagueAPIController.LeagueBySummonerId(summonerDto.getId());
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
    public String showRankedEmblem(String tier) {
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
        public String showProfileIconUrl (UserDto userDto){
            int profileIconId = userDto.getProfileIconId();
            String profileIconsUrl = String.format("http://ddragon.leagueoflegends.com/cdn/13.10.1/img/profileicon/%d.png", profileIconId);
            return profileIconsUrl;
        }
    }