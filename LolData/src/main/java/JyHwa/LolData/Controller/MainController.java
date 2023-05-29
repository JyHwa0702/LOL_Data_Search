package JyHwa.LolData.Controller;

import JyHwa.LolData.Dto.MatchDto.MatchDto;
import JyHwa.LolData.Dto.MatchDto.ParticipantDto;
import JyHwa.LolData.Dto.UserDto;
import JyHwa.LolData.Entity.User;
import JyHwa.LolData.Service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
@ToString
public class MainController {

    private final MainService mainService;
    private final SummonerAPIController summonerAPIController;
    private final MatchAPIController matchAPIController;

    @GetMapping("/")
    public String indexPage(){
        return "index";
    }


    @GetMapping("/matchDetails/{matchId}")
    public String matchDetails(@PathVariable String matchId,Model model){
        MatchDto matchDto = matchAPIController.getMatchsByMatchId(matchId);
        model.addAttribute("matchDto",matchDto);
        return "matchDetails";
    }


    @PostMapping("/searchBySummonerName")
    public String SearchByName(String summonerName, Model model){
        UserDto userDto = mainService.SearchBySummonerName(summonerName);
        model.addAttribute("user",userDto);

        String rankedEmblem = mainService.showRankedEmblem(userDto.getTier());
        model.addAttribute("rankedEmblem",rankedEmblem);


        String profileIconUrl = mainService.showProfileIconUrl(userDto);
        model.addAttribute("profileIconUrl",profileIconUrl);

        String[] matchIds = matchAPIController.getMatchIdByUserPuuid(userDto.getPuuid());
        log.info(matchIds.toString());
        MatchDto[] matchDtos = matchAPIController.getMatchListByMatchIds(matchIds);
        log.info(matchDtos.toString());
        model.addAttribute("matchDtos",matchDtos);
        return "searchForm";
    }
}
