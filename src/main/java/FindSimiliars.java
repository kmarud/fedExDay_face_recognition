import com.fasterxml.jackson.databind.ObjectMapper;
import dto.FindSimiliarResposne;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.stream.Collectors;

public class FindSimiliars {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String subscriptionKey = "986190d8e9fc4573a42aebbc40df9b95";
    private static final String endpoint = "https://fedexfacefindsimilar.cognitiveservices.azure.com";
    private static final String body =
            "{" +
                    "\"faceId\":\"%s\", " +
                    "\"faceIds\":%s, " +
                    "\"maxNumOfCandidatesReturned\": 10," +
                    "\"mode\": \"matchPerson\"" +
                    "}";

    public static FindSimiliarResposne[] findSimilars(String faceId, Set<String> knownFaces) throws URISyntaxException, IOException {
        HttpClient httpclient = HttpClientBuilder.create().build();

        URIBuilder builder = new URIBuilder(endpoint + "/face/v1.0/findsimilars");

        // Prepare the URI for the REST API call.
        URI uri = builder.build();
        HttpPost request = new HttpPost(uri);

        // Request headers.
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

        // Request body.
        String jsonBody = String.format(body, faceId, knownFaces.stream().map(FindSimiliars::addQuotation).collect(Collectors.toList()));
        request.setEntity(new StringEntity(jsonBody));

        // Execute the REST API call and get the response JSON.
        String resultJson = EntityUtils.toString(httpclient.execute(request).getEntity()).trim();

        // Convert JSON to array
        return objectMapper.readValue(resultJson, FindSimiliarResposne[].class);
    }

    private static String addQuotation(String s) {
        return "\"" + s + "\"";
    }
}
