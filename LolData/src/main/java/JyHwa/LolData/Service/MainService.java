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

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {

    private final SummonerRepository summonerRepository;
    private final UserRepository userRepository;
    private final SummonerAPIController summonerAPIController;
    private final MatchAPIController matchAPIController;
    private final LeagueAPIController leagueAPIController;

    public void saveSummoner(SummonerDto summonerDto){
        summonerDto.setCheckField(1);
        summonerRepository.save(summonerDto.toEntity());
    }
    public void saveUser(UserDto userDto){
        userDto.setCheckField(1);
        userRepository.save(userDto.toEntity());
    }

    public List<User> FindBycheckField(int checkField){
        List<User> users = userRepository.findByCheckField(checkField);
        return users;
    }

    public UserDto SearchBySummonerName(String summonerName){
        SummonerDto summonerDto = summonerAPIController.callSummonerByName(summonerName);
        LeagueEntryDto[] leagueEntryDtos = leagueAPIController.LeagueBySummonerId(summonerDto.getId());
        UserDto userDto = new UserDto();

        UserDtoBysummonerDtoAndLeagueEntryDtos(userDto,summonerDto,leagueEntryDtos);

        return userDto;
    }
    public void UserDtoBysummonerDtoAndLeagueEntryDtos(UserDto userDto,SummonerDto summonerDto,LeagueEntryDto[] leagueEntryDtos){
        LeagueEntryDto leagueEntryDto = leagueEntryDtos[0]; //솔로큐

        userDto.setSummonerName(leagueEntryDto.getSummonerName());
        userDto.setQueueType(leagueEntryDto.getQueueType());
        userDto.setTier(leagueEntryDto.getTier());
        userDto.setRank(leagueEntryDto.getRank());
        userDto.setLeaguePoints(leagueEntryDto.getLeaguePoints());
        userDto.setWins(leagueEntryDto.getWins());
        userDto.setLosses(leagueEntryDto.getLosses());
        userDto.setSummonerLevel(summonerDto.getSummonerLevel());
        userDto.setCheckField(summonerDto.getCheckField());
    }

}
