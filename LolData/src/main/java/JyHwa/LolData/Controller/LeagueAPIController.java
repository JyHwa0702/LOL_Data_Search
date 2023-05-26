package JyHwa.LolData.Controller;

import JyHwa.LolData.Dto.LeagueEntryDto.LeagueEntryDto;
import JyHwa.LolData.Service.LeagueService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LeagueAPIController {

    private final LeagueService leagueService;

    @PostMapping("/LeagueBySummonerId")
    @ResponseBody
    public LeagueEntryDto[] LeagueBySummonerId(String summonerId){


        LeagueEntryDto[] leagueEntryDtos = leagueService.LeagueBySummonerId(summonerId);



        return leagueEntryDtos;
    }

}
