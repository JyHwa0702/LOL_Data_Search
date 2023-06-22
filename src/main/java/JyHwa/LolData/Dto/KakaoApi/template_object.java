package JyHwa.LolData.Dto.KakaoApi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class template_object {


    private String object_type;
    private String text; //텍스트 정보
    private Link link; //콘텐츠 클릭시 이동할 링크정보
    private String button_title;


    public template_object(String text){
        String url = "http://localhost:8080/";

        this.object_type = "text";
        this.text = text;
        this.link = new Link(url, url);
        this.button_title="확인 해보기";
    }

    public String toJson(ObjectMapper objectMapper){
        try{
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting template_object to JSON", e);
        }
    }
}
