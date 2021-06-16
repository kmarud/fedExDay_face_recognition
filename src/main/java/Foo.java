import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class Foo {
    private static final String subscriptionKey = "986190d8e9fc4573a42aebbc40df9b95";
    private static final String endpoint = "https://fedexfacefindsimilar.cognitiveservices.azure.com";

    private static final String imageWithUnknownFace =
//            "{\"url\":\"http://ocdn.eu/images/pulscms/ODU7MDA_/9a730a9c48bef81424937f7fabb2cc6e.jpg\"}"; // Andrzej
//            "{\"url\":\"https://images1.westend61.de/0001229168pw/portrait-of-smiling-bald-man-wearing-glasses-KNSF06205.jpg\"}";
            "{\"url\":\"https://www.wykop.pl/cdn/c3397993/link_YqoPw5UPCQTp8FIH65EjeT00YPOQ6Uqn,w300h223.jpg\"}"; // Strange man from the Stock



    public static String toJson(String url){
        return  "{\"url\":\"" + url + "\"}";
    }
    public static void main(String[] args) {

        try {

            Map<String, String> map = new HashMap<>();
            map.put(addKnownUser(toJson("https://upload.wikimedia.org/wikipedia/commons/8/8b/Prezydent_Rzeczypospolitej_Polskiej_Andrzej_Duda.jpg")), "Andrzej Duda");
            map.put(addKnownUser(toJson("https://www.prezydent.pl/gfx/prezydent/userfiles3/images/wydarzenia/2020/08_sierpien/06_galeria_podsumowanie/1_kolejna_czesc/446_andrzej_duda_5_lat_prezydentury_20180613_2018-06-13_makow_mazowiecki_js__9329.jpg")), "Andrzej Duda");
            map.put(addKnownUser(toJson("https://cdn.shopify.com/s/files/1/1045/8368/files/Bald-man-wearing-black-glasses-frame.jpg")), "Bald man with glasses");
            map.put(addKnownUser(toJson("https://i.pinimg.com/originals/0a/58/69/0a58691f4bad41c65af87dd58a999ecd.jpg")), "Bald actor"); //Stanley Tucci?
            map.put(addKnownUser(toJson("https://assets.weforum.org/sf_account/image/-Iz2VwsxPVkx3GQrc5m3oWD1mI2d4yD2liRq60jnV04.jpg")), "Angela Merkel");
            map.put(addKnownUser(toJson("https://images.westend61.de/0001229178pw/portrait-of-bald-man-with-beard-wearing-glasses-KNSF06215.jpg")), "Bald man");
            map.put(addKnownUser(toJson("https://www.rmf.fm/_files/Short_foto/625/0e4fb6e75a40f3ac328f8fe2e841898e.jpg")), "Strange man from the Stock");
//
//            Map<String, String> ... key - faceId, value - name
//            addKnownUser("link do pieknej twarzy Andrzejka", "Andrzejek");
//            addKnownUser("link do twarzy Briney", "Piekna Britnej");

            String faceId = recognizeUser(imageWithUnknownFace);
            System.out.println("faceId: " + faceId);

            FindSimiliarResposne[] resposne = FindSimiliars.findSimilars(faceId, map.keySet());
            Arrays.stream(resposne).sorted(Comparator.comparingDouble(FindSimiliarResposne::getConfidence).reversed()).limit(1)

                    .forEach(x ->
                    System.out.println("Your user is probably "+ map.get(x.getFaceId()) + ", with confidence " + x.getConfidence()));
//            );

//            if( map.containsKey(resposne.getFaceId())){
//
//            }
//            else {
//                System.out.println("User not found");
//            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }


    public static String addKnownUser(String url) throws IOException, URISyntaxException {
        return recognizeUser(url);
    }
    public static String recognizeUser(String url) throws URISyntaxException, IOException {
        HttpClient httpclient = HttpClientBuilder.create().build();

        URIBuilder builder = new URIBuilder(endpoint + "/face/v1.0/detect");

        // Request parameters. All of them are optional.
        builder.setParameter("detectionModel", "detection_03");
        builder.setParameter("returnFaceId", "true");

        // Prepare the URI for the REST API call.
        URI uri = builder.build();
        HttpPost request = new HttpPost(uri);

        // Request headers.
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

        // Request body.
        StringEntity reqEntity = new StringEntity(url);
        request.setEntity(reqEntity);

        // Execute the REST API call and get the response entity.
        HttpResponse response = httpclient.execute(request);
        HttpEntity entity = response.getEntity();

        String jsonString = EntityUtils.toString(entity).trim();

        ObjectMapper objectMapper = new ObjectMapper();
        Bar[] result = objectMapper.readValue(jsonString, Bar[].class);

        return result[0].getFaceId();
    }

}