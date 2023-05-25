package JyHwa.LolData.Controller;

import JyHwa.LolData.Dto.SummonerDto;
import JyHwa.LolData.Dto.UserDto;
import JyHwa.LolData.Entity.Summoner;
import JyHwa.LolData.Entity.User;
import JyHwa.LolData.Service.MainService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/")
    public String indexPage(){
        return "index";
    }

    @PostMapping("/searchBySummonerName")
    public String SearchByName(String summonerName, Model model){
        UserDto userDto = mainService.SearchBySummonerName(summonerName);
        mainService.saveUser(userDto);



        //전적 검색한 필드
        List<User> users = mainService.FindBycheckField(1);
        model.addAttribute("users",users);
        return "searchForm";
    }
}
