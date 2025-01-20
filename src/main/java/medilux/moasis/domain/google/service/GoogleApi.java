package medilux.moasis.domain.google.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Service
@Data
public class GoogleApi {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    @Value("${google.redirect_uri:http://localhost:8080/api/google/auth/login/oauth2}")
    private String googleRedirectUri;

    private static final Logger log = LoggerFactory.getLogger(GoogleApi.class);

    public String getAccessToken(String code) {
        String tokenUrl = "https://oauth2.googleapis.com/token";
        try {
            log.info("1) Preparing API call to get access token.");

            // 1. API 호출 준비
            URL url = new URL(tokenUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            log.info("2) API connection established: {}", conn);

            // 2. POST 바디 작성
            StringBuilder params = new StringBuilder();
            params.append("grant_type=authorization_code");
            params.append("&client_id=").append(googleClientId);
            params.append("&client_secret=").append(googleClientSecret);
            params.append("&redirect_uri=").append(googleRedirectUri);
            params.append("&code=").append(code);

            log.info("3) Parameters constructed: {}", params);

            // 3. 요청 파라미터 전송
            try (DataOutputStream dos = new DataOutputStream(conn.getOutputStream())) {
                dos.writeBytes(params.toString());
                dos.flush();
            }

            log.info("4) Parameters sent to token endpoint.");

            // 4. 응답 확인
            int responseCode = conn.getResponseCode();
            log.info("5) Response code received: {}", responseCode);

            BufferedReader br;
            if (responseCode >= 200 && responseCode < 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            // 5. 응답(JSON) 읽기
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();

            log.info("6) Response body received: {}", sb);

            // 6. JSON 파싱 및 access_token 추출
            JsonObject json = JsonParser.parseString(sb.toString()).getAsJsonObject();
            return json.get("access_token").getAsString();
        } catch (IOException e) {
            log.error("Failed to get access token.", e);
        }
        return null;
    }

    public HashMap<String, Object> getUserInfo(String accessToken) {
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqUrl = "https://www.googleapis.com/oauth2/v2/userinfo";

        try {
            log.info("1) Preparing API call to get user info.");

            // 1. 연결 설정
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            log.info("2) API connection established: {}", conn);

            // 2. 응답 코드 확인
            int responseCode = conn.getResponseCode();
            log.info("3) Response code received: {}", responseCode);

            BufferedReader br;
            if (responseCode >= 200 && responseCode <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            // 3. 응답(Body) 읽기
            StringBuilder responseSb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                responseSb.append(line);
            }
            String result = responseSb.toString();
            br.close();

            log.info("4) Response body received: {}", result);

            // 4. JSON 파싱
            JsonObject json = JsonParser.parseString(result).getAsJsonObject();

            // 5. 필요한 정보 추출
            String email = json.has("email") ? json.get("email").getAsString() : "No email provided";
            String name = json.has("name") ? json.get("name").getAsString() : "No name provided";
            String picture = json.has("picture") ? json.get("picture").getAsString() : "No picture provided";

            log.info("5) Extracted user info: email={}, name={}, picture={}", email, name, picture);

            // 6. 추출한 정보 저장
            userInfo.put("email", email);
            userInfo.put("name", name);
            userInfo.put("picture", picture);

        } catch (Exception e) {
            log.error("Failed to retrieve user info.", e);
        }

        return userInfo;
    }
}
