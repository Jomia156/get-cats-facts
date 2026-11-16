package ru.netology;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    private static final String URL =
            "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // ожидание подключения к серверу
                        .setSocketTimeout(30000)    // ожидание получения данных
                        .setRedirectsEnabled(false) // не следовать редиректам
                        .build())
                .build();

        HttpGet request = new HttpGet(URL);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            ObjectMapper mapper = new ObjectMapper();
            List<CatFact> facts = mapper.readValue(json, new TypeReference<List<CatFact>>() {});

            List<CatFact> filtered = facts.stream()
                    .filter(value -> value.getUpvotes() != null && value.getUpvotes() > 0)
                    .collect(Collectors.toList());

            filtered.forEach(System.out::println);
        } finally {
            httpClient.close();
        }
    }
}