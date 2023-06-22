package JyHwa.LolData.Dto.KakaoApi;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Link {
    private String web_url; //pc버전 카카오톡에서 사용하는 웹링크
    private String mobile_web_url; //모바일 카카오톡에서 사용하는 웹링크
//    private String android_execution_params; //안드로이드 카톡에서 사용하는 앱링크, //없을경우 모바일 웹 url사용
//    private String ios_execution_params; //IOS 카톡에서 사용하는 앱링크, //없을경우 모바일 웹 url사용
    public Link (String web_url,String mobile_web_url){
        this.web_url = web_url;
        this.mobile_web_url = mobile_web_url;
    }
}
