package JyHwa.LolData.Controller;

import JyHwa.LolData.Dto.MatchDto.MatchDto;
import JyHwa.LolData.Dto.SummonerDto;
import JyHwa.LolData.Service.MatchService;
import JyHwa.LolData.Service.SummonerService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class SummonerController {

    private final SummonerService summonerService;
    private final MatchService matchService;


    @PostMapping("/summonerByName")
    @ResponseBody
    public SummonerDto callSummonerByName(String summonerName){

        summonerName = summonerName.replaceAll(" ","%20");

        SummonerDto apiResult =summonerService.callRiotAPISummonerByName(summonerName);

        return apiResult;
    }

    @PostMapping("/matchIdsByPuuid")
    @ResponseBody
    public String[] matchIdByPuuid(String puuid){

        String[] matchIds = matchService.callRiotAPIMatchIdByPuuid(puuid);

        return matchIds;
    }

    @PostMapping("/matchByMatchId")
    @ResponseBody
    public MatchDto matchByMatchId(String matchId){


        return matchDto;
    }
}
