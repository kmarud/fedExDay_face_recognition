import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class FindSimiliars {
//    curl -v -X POST "https://westus.api.cognitive.microsoft.com/face/v1.0/findsimilars"
//    -H "Content-Type: application/json" -H "Ocp-Apim-Subscription-Key: {subscription key}" --data-ascii "{body}"


    private static final String subscriptionKey = "986190d8e9fc4573a42aebbc40df9b95";
    private static final String endpoint = "https://fedexfacefindsimilar.cognitiveservices.azure.com";

    private static final String body =
            "{\"faceId\":\"%s\", \"faceIds\":%s, \"maxNumOfCandidatesReturned\": 10,\n" +
                    "    \"mode\": \"matchPerson\"}";

    public static FindSimiliarResposne[] findSimilars(String faceId, Set<String> knownFaces) throws URISyntaxException, IOException {
        HttpClient httpclient = HttpClientBuilder.create().build();

        URIBuilder builder = new URIBuilder(endpoint + "/face/v1.0/findsimilars");

        // Request parameters. All of them are optional.
//        builder.setParameter("detectionModel", "detection_03");
//        builder.setParameter("returnFaceId", "true");

        // Prepare the URI for the REST API call.
        URI uri = builder.build();
        HttpPost request = new HttpPost(uri);

        // Request headers.
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

        // Request body.

        String jsonBody = String.format(body, faceId, knownFaces.stream().map(x -> "\"" + x + "\"").collect(Collectors.toList()));
        System.out.println(jsonBody);
        StringEntity reqEntity = new StringEntity(jsonBody);
        request.setEntity(reqEntity);

        // Execute the REST API call and get the response entity.
        HttpResponse response = httpclient.execute(request);
        HttpEntity entity = response.getEntity();

//        if (entity != null) {
        // Format and display the JSON response.
//        System.out.println("REST Response:\n");

        String jsonString = EntityUtils.toString(entity).trim();

        ObjectMapper objectMapper = new ObjectMapper();
        FindSimiliarResposne[] result = objectMapper.readValue(jsonString, FindSimiliarResposne[].class);
        Arrays.stream(result)
                .forEach(
                x ->
                        System.out.println("found user with faceID " + x.getFaceId() + " with confidence: " + x.getConfidence()));


        return result;

//        ObjectMapper objectMapper = new ObjectMapper();
//        Bar[] result = objectMapper.readValue(jsonString, Bar[].class);
//
//        return result[0].getFaceId();
    }
}
