import com.fasterxml.jackson.databind.ObjectMapper;
import dto.DetectResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;

public class Detect {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String subscriptionKey = "986190d8e9fc4573a42aebbc40df9b95";
    private static final String endpoint = "https://fedexfacefindsimilar.cognitiveservices.azure.com";

    public static String addKnownUser(String url) {
        try {
            return recognizeUser(url);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return "UNKONWN";
    }

    public static String recognizeUser(String url) throws URISyntaxException, IOException {
        HttpClient httpclient = HttpClientBuilder.create().build();

        URIBuilder builder = new URIBuilder(endpoint + "/face/v1.0/detect");

        // Request parameters. All of them are optional.
        builder.setParameter("detectionModel", "detection_03");
        builder.setParameter("returnFaceId", "true");

        // Prepare the URI for the REST API call.
        HttpPost request = new HttpPost(builder.build());

        // Request headers.
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

        // Request body.
        request.setEntity(new StringEntity(url));

        // Execute the REST API call and get the response entity.
        HttpResponse response = httpclient.execute(request);
        HttpEntity entity = response.getEntity();

        String jsonString = EntityUtils.toString(entity).trim();

        DetectResponse[] result = objectMapper.readValue(jsonString, DetectResponse[].class);

        return result[0].getFaceId();
    }
}
