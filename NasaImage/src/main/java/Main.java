import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static String URL;
    public static final ObjectMapper mapper = new ObjectMapper();
    public static void main(String[] args) throws IOException {
        System.out.println("Enter your API key URL from \"https://api.nasa.gov/\":");
        Scanner console = new Scanner(System.in);
        URL = console.nextLine();
        console.close();
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();

        CloseableHttpResponse response = httpClient.execute(new HttpGet(URL));

        NasaObject nasaObject = mapper.readValue(response.getEntity().getContent(), NasaObject.class);
        System.out.println(nasaObject);

        CloseableHttpResponse picResponse = httpClient.execute(new HttpGet(nasaObject.getUrl()));

        String[] arr = nasaObject.getUrl().split("/");
        String file = arr[arr.length-1];

        HttpEntity entity = picResponse.getEntity();
        if (entity != null) {
            try (FileOutputStream output = new FileOutputStream(file)){
                entity.writeTo(output);
            }
        }
    }
}
