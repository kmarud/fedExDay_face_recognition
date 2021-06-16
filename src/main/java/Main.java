import dto.FindSimiliarResposne;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static Map<String, String> knownFaces;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        knownFaces = Arrays.stream(KnownUsers.values())
                .collect(Collectors.toMap(x -> Detect.addKnownUser(toJson(x.getUrlToFace())), KnownUsers::getName));

        while (true) {
            try {
                foo();
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public static void foo() throws IOException, URISyntaxException {
        System.out.println("Who shall I recognise? Enter URL to the photo with face and let me do the rest :)");

        String faceId = Detect.recognizeUser(toJson(scanner.nextLine()));

        FindSimiliarResposne[] response = FindSimiliars.findSimilars(faceId, knownFaces.keySet());

        if (response.length > 0) {
            Arrays.stream(response)
                    .sorted(Comparator.comparingDouble(FindSimiliarResposne::getConfidence).reversed())
                    .limit(1)
                    .forEach(Main::print);
        } else {
            System.out.println("I don't know this face :( Teach me it! Enter a person's name:");
            String name = scanner.nextLine();
            knownFaces.put(faceId, name);
            System.out.println(name + "... Ok, I'll remember :) Let's try again then!\n");
        }
    }

    public static String toJson(String url) {
        return "{\"url\":\"" + url + "\"}";
    }

    public static void print(FindSimiliarResposne response) {
        System.out.printf("Your user is probably %s with confidence %.2f %% %n",
                knownFaces.get(response.getFaceId()),
                (response.getConfidence() * 100));
    }
}