package JyHwa.LolData.Service;

import JyHwa.LolData.Dto.KakaoDto;
import JyHwa.LolData.Dto.UserDto;
import JyHwa.LolData.Entity.Kakao;
import JyHwa.LolData.Entity.User;
import JyHwa.LolData.Repository.KakaoRepository;
import JyHwa.LolData.Repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@PropertySource(ignoreResourceNotFound = false, value = "classpath:kakaoApiKey.properties")
@Slf4j
@RequiredArgsConstructor
public class KakaoService {
    private final UserRepository userRepository;
    private final KakaoRepository kakaoRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${kakao.restapi.key}")
    private String client_id;

//    @Value("${redirect.uri}")
    private String redirect_uri = "http://localhost:8080/oauth/kakao";

    private String grantType = "authorization_code";

    public String getKakaoCodeUrl(){
        String tokenRequestUrl = String.format("https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=%s&redirect_uri=%s", client_id,redirect_uri);
        return tokenRequestUrl;
    }
    public JsonNode getKakaoInfo(String accessToken) {

        try{
            String getInfoUrl = "https://kapi.kakao.com/v2/user/me";

            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(getInfoUrl);
            httpPost.setHeader("Authorization","Bearer "+accessToken);


            CloseableHttpResponse response = httpClient.execute(httpPost);
            log.info("response = "+response.toString());
            HttpEntity entity = response.getEntity();
            JsonNode kakaoInfoNode = objectMapper.readTree(entity.getContent());

            return kakaoInfoNode;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
    public String sendNotification(String message,String accessToken) throws IOException {
        String sendMeUrl = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(sendMeUrl);
        httpPost.setHeader("Authorization","Bearer"+accessToken);
        httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");

        List<NameValuePair> urlParameters = new ArrayList<>();
        String jsonMessage = String.format("{/\"object_type\":\"text\",\"text\":\"%s\"}", message);
        urlParameters.add(new BasicNameValuePair("template_object",jsonMessage));

        try{
            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters,"UTF-8"));
            CloseableHttpResponse response = httpClient.execute(httpPost);

            return response.toString();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally{
            httpClient.close();
        }
    }
    public String getKakaoAccessToken(String code) {

        try{
            String getTokenUrl="https://kauth.kakao.com/oauth/token";

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("grant_type",grantType));
            params.add(new BasicNameValuePair("client_id",client_id));
            params.add(new BasicNameValuePair("redirect_uri",redirect_uri));
            params.add(new BasicNameValuePair("code",code));


            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(getTokenUrl);
            httpPost.setHeader("Content-type","application/x-www-form-urlencoded;charset=utf-8");
            httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

            CloseableHttpResponse response = httpClient.execute(httpPost);
            log.info("getKakaoAccessToken response = "+response);
            HttpEntity entity = response.getEntity();
            log.info("entity = "+entity.toString());

            JsonNode jsonToken = objectMapper.readTree(entity.getContent());
            log.info("accessToken = " + jsonToken.toString());
            String accessToken = jsonToken.get("access_token").asText();
            log.info("string AccessToken = "+accessToken);

            return accessToken;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Transactional
    public void saveKakao(JsonNode kakaoInfo, Model model){

//      properties{
//          nickname
//        }
//      kakao_account{
//          email
//      }

        String email = kakaoInfo.get("kakao_account").get("email").asText();
        String nickname = kakaoInfo.get("properties").get("nickname").asText();

        KakaoDto kakaoDto = new KakaoDto();

        kakaoDto.setEmail(email);
        kakaoDto.setNickname(nickname);

        Kakao kakao = kakaoDto.toEntity();
        log.info("savedKakao before = "+kakao.toString());
        Kakao savedKakao = kakaoRepository.save(kakao);
        log.info("savedKakao after = "+savedKakao.toString());

        model.addAttribute("kakaoId",savedKakao.getId());
        log.info("saved kakaoId = "+savedKakao.getId());
    }

    @Transactional
    public void saveUser(User user, Long kakaoId){
        Optional<Kakao> kakao = kakaoRepository.findById(kakaoId);
        if(kakao.isPresent()){
            UserDto usersDto = new UserDto(user);

            usersDto.setKakao(kakao.get()); // UserDto 객체에 Kakao 객체 삽입

            user = usersDto.toEntity();

            kakao.get().getUsers().add(user);

            kakaoRepository.save(kakao.get()); // Kakao 객체 DB에 저장
        }
    }
    @Transactional
    public List<User> findUsers(Long kakaoId){
        Optional<Kakao> kakao = kakaoRepository.findById(kakaoId);
        List<User> users = kakao.get().getUsers();
        return users;
    }
    @Transactional
    public void kakaoDeleteUser(Long kakaoId, Long userId){
        List<User> users = findUsers(kakaoId);
        Optional<User> userById = userRepository.findById(userId);
        users.remove(userById);
    }
}
