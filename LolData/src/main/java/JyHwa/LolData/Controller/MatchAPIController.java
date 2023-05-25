package JyHwa.LolData.Controller;

import JyHwa.LolData.Dto.MatchDto.MatchDto;
import JyHwa.LolData.Service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MatchAPIController {

    private final MatchService matchService;

    @PostMapping("/matchIdsByPuuid")
    @ResponseBody
    public String[] matchIdByPuuid(String puuid){

        String[] matchIds = matchService.callRiotAPIMatchIdByPuuid(puuid);

        return matchIds;
    }

    @PostMapping("/matchByMatchId")
    @ResponseBody
    public MatchDto matchByMatchId(String matchId){
        MatchDto matchDto = matchService.callRiotAPIMatchByMatchId(matchId);
        return matchDto;
    }
}
