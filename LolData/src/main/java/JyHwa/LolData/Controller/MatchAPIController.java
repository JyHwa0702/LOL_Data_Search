package JyHwa.LolData.Controller;

import JyHwa.LolData.Dto.MatchDto.MatchDto;
import JyHwa.LolData.Dto.UserDto;
import JyHwa.LolData.Entity.User;
import JyHwa.LolData.Service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matches")
public class MatchAPIController {

    private final MatchService matchService;

    @PostMapping("/ids")
    public String[] getMatchIdByUserPuuid(String puuid){
        return matchService.callRiotAPIMatchIdByPuuid(puuid);
    }

    @PostMapping("/details")
    public MatchDto getMatchsByMatchId(String matchId){
        return matchService.callRiotAPIMatchByMatchId(matchId);
    }

    @PostMapping("/list")
    public MatchDto[] getMatchListByMatchIds(String[] matchIds){

        return Stream.of(matchIds)
                .map(matchService::callRiotAPIMatchByMatchId)
                .toArray(MatchDto[]::new);
    }
}
