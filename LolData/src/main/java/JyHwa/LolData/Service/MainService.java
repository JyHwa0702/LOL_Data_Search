package JyHwa.LolData.Service;

import JyHwa.LolData.Dto.SummonerDto;
import JyHwa.LolData.Entity.Summoner;
import JyHwa.LolData.Repository.MainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {

    private final MainRepository mainRepository;

    public void saveSummoner(SummonerDto summonerDto){
        summonerDto.setCheckField(1);
        mainRepository.save(summonerDto.toEntity());
    }

    public List<Summoner> FindBycheckField(int checkField){
        List<Summoner> summoners = mainRepository.findByCheckField(checkField);
        return summoners;
    }

}
