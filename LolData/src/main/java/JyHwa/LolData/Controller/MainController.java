package JyHwa.LolData.Controller;

import JyHwa.LolData.Dto.SummonerDto;
import JyHwa.LolData.Entity.Summoner;
import JyHwa.LolData.Repository.MainRepository;
import JyHwa.LolData.Service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;
    private final SummonerAPIController summonerAPIController;
    private final LeagueAPIController leagueAPIController;

    @GetMapping("/")
    public String indexPage(){
        return "index";
    }

    @PostMapping("/searchBySummonerName")
    public String SearchByName(String summonerName, Model model){
        SummonerDto summonerDto = summonerAPIController.callSummonerByName(summonerName);
        mainService.saveSummoner(summonerDto);

        //전적 검색한 필드
        List<Summoner> summonersCheckField1 = mainService.FindBycheckField(1);
        model.addAttribute("summonersCheckField1",summonersCheckField1);
        return "searchForm";
    }
}
