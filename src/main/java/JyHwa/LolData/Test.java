package JyHwa.LolData;

import JyHwa.LolData.Dto.KakaoApi.template_object;

public class Test {
    public static void main(String[] args) {
        template_object templateObject = new template_object("테스트 메시지");
        System.out.println(templateObject.toString());
    }
}
