package JyHwa.LolData.Controller;

import JyHwa.LolData.Dto.SummonerDto;
import JyHwa.LolData.Service.SummonerService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class SummonerAPIController {

    private final SummonerService summonerService;


    @PostMapping("/summonerByName")
    @ResponseBody
    public SummonerDto callSummonerByName(String summonerName){

        summonerName = summonerName.replaceAll(" ","%20");

        SummonerDto apiResult =summonerService.callRiotAPISummonerByName(summonerName);

        return apiResult;
    }




}
