package com.example.linkup.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GitHubService {

    private final RestTemplate restTemplate;
    @Value("${github.token}")
    private String gitHubToken;
    private final String repo = "EMamedov94/LinkUp";

    public String uploadFileToGitHub(String path, String fileName, MultipartFile file) {
        try {
            byte[] fileContent = file.getBytes();
            String url = generateUploadUrl(path, fileName);
            String base64Content = Base64.getEncoder().encodeToString(fileContent);

            HttpHeaders headers = buildHeaders();
            String requestBody = createRequestBody(base64Content);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                return generateFileUrl(path, fileName);
            } else {
                throw new RuntimeException("Ошибка при загрузке файла в GitHub");
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке файла", e);
        }
    }

    private String getFileInfoFromGitHub(String url) {
        // GET-запрос для получения информации о файле
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "token " + gitHubToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();  // Возвращаем тело ответа
            } else {
                return null;  // Файл не существует
            }
        } catch (Exception e) {
            return null;  // Ошибка получения информации о файле
        }
    }

    private String extractShaFromResponse(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            return rootNode.path("sha").asText();
        } catch (Exception e) {
            throw new RuntimeException("Error parsing SHA from response", e);
        }
    }

    private String generateUniqueFileName(String originalFileName) {
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        // Генерируем уникальное имя
        return UUID.randomUUID().toString() + fileExtension;
    }

    private String generateUploadUrl(String path, String fileName) {
        return "https://api.github.com/repos/" + repo + "/contents/" + path + "/" + generateUniqueFileName(fileName);
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + gitHubToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private String createRequestBody(String base64Content) {
        return "{ \"message\": \"Upload image\", \"content\": \"" + base64Content + "\" }";
    }

    private String generateFileUrl(String path, String fileName) {
        return "https://github.com/" + repo + "/raw/main/" + path + "/" + generateUniqueFileName(fileName);
    }
}
