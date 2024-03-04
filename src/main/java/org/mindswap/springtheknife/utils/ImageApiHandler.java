package org.mindswap.springtheknife.utils;

import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ImageApiHandler {

    private static final String API_URL = "http://127.0.0.1:7860/sdapi/v1/txt2img";
    private static final Integer BUFFER_SIZE = (16 * 256 * 512); // Max size of ~2MB
    private static final Integer STEPS = 15; //Amount of steps used to generate the image. 15/25 should be good enough

    public static String getImageDataFromAPI(String prompt) {

        Map<String, Object> payload = new HashMap<>();
        payload.put("prompt", prompt);
        payload.put("steps", STEPS);

        WebClient webClient = WebClient.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(BUFFER_SIZE))
                .baseUrl(API_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        JSONObject json = new JSONObject(webClient.post()
                .uri(API_URL)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .block());

        return json.getJSONArray("images").getString(0);
    }
}
