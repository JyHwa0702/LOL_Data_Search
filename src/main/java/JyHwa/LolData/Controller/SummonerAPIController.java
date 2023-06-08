package JyHwa.LolData.Controller;

import JyHwa.LolData.Dto.SummonerDto;
import JyHwa.LolData.Service.SummonerService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/summoners")
public class SummonerAPIController {

    private final SummonerService summonerService;


    @PostMapping("/name")
    public SummonerDto callSummonerByName(String summonerName){
        String encodedSummonerName = summonerName.replaceAll(" ","%20");
        return summonerService.callRiotAPISummonerByName(encodedSummonerName);
    }




}
