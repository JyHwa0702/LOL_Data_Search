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

    @GetMapping("/showRankedEmblem")
    public void showRankedEmblem(User user,Model model){
        String rankedEmblem=null;

        switch (user.getTier()) {
            case "IRON":
                rankedEmblem = "emblem-iron.png";
                break;
            case "BRONZE":
                rankedEmblem = "emblem-bronze.png";
                break;
            case "SILVER":
                rankedEmblem = "emblem-silver.png";
                break;
            case "GOLD":
                rankedEmblem = "emblem-gold.png";
                break;
            case "PLATINUM":
                rankedEmblem = "emblem-platinum.png";
                break;
            case "DIAMOND":
                rankedEmblem = "emblem-diamond.png";
                break;
            case "MASTER":
                rankedEmblem = "emblem-master.png";
                break;
            case "GRANDMASTER":
                rankedEmblem = "emblem-grandmaster.png";
                break;
            case "CHALLENGER":
                rankedEmblem = "emblem-challenger.png";
                break;
            case "UNRANKED":
                rankedEmblem = "emblem-provisional.png";
                break;
        }
        model.addAttribute("rankedEmblem",rankedEmblem);

        }

    @PostMapping("/searchBySummonerName")
    public String SearchByName(String summonerName, Model model){
        UserDto userDto = mainService.SearchBySummonerName(summonerName);
        User user = mainService.saveUser(userDto);
        model.addAttribute("user",user);
        showRankedEmblem(user, model);




        return "searchForm";
    }
}
