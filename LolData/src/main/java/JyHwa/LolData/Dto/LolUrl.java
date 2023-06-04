package JyHwa.LolData.Dto;

import lombok.Data;

@Data
public class LolUrl {

    String URL = "http://ddragon.leagueoflegends.com/cdn";
    String version = "13.10.1";
    String language = "ko_KR";
    String ImgUrl = String.format("%s/%s/img", URL, version); // http://ddragon.leagueoflegends.com/cdn/13.10.1/img
    String jsonUrl = String.format("%s/%s/data/%s", URL, version, language); // http://ddragon.leagueoflegends.com/cdn/13.10.1/data/ko_KR
}
