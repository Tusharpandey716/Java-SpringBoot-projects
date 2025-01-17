import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LinkShortener {
    private Map<String, String> shortToLongMap;
    private Map<String, String> longToShortMap;

    public LinkShortener() {
        shortToLongMap = new HashMap<>();
        longToShortMap = new HashMap<>();
    }

    public String shorten(String longUrl) {
        if (longToShortMap.containsKey(longUrl)) {
            return longToShortMap.get(longUrl);
        }

        String shortUrl = generateShortUrl();
        shortToLongMap.put(shortUrl, longUrl);
        longToShortMap.put(longUrl, shortUrl);
        return shortUrl;
    }

    public String expand(String shortUrl) {
        return shortToLongMap.getOrDefault(shortUrl, "Short URL not found");
    }

    private String generateShortUrl() {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder shortUrlBuilder = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 6; i++) {
            int index = rand.nextInt(characters.length());
            shortUrlBuilder.append(characters.charAt(index));
        }
        return shortUrlBuilder.toString();
    }

    public static void main(String[] args) {
        LinkShortener linkShortener = new LinkShortener();

        // Test the functionality
        String longUrl = "https://www.geeksforgeeks.org/courses?source=google&medium=cpc&device=c&keyword=geeksforgeeks&matchtype=e&campaignid=20039445781&adgroup=147845288105&gad_source=1&gclid=CjwKCAiAopuvBhBCEiwAm8jaMeBhZ76oBWjyPZqbs9X31Itc7Bqz5g12ZpflXfCerb8ktPwMi0Cv3hoCFGgQAvD_BwE";
        String shortUrl = linkShortener.shorten(longUrl);
        System.out.println("Shortened URL: " + shortUrl);

        String expandedUrl = linkShortener.expand(shortUrl);
        System.out.println("Expanded URL: " + expandedUrl);
    }
}
