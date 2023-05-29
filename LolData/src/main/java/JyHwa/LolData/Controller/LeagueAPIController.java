package JyHwa.LolData.Controller;

import JyHwa.LolData.Dto.LeagueEntryDto.LeagueEntryDto;
import JyHwa.LolData.Service.LeagueService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/league")
public class LeagueAPIController {

    private final LeagueService leagueService;

    @PostMapping("/summoner")
    public LeagueEntryDto[] LeagueBySummonerId(String summonerId){
        return leagueService.LeagueBySummonerId(summonerId);
    }

}
