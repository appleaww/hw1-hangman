package academy.game;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.*;

public class Dictionary {
    private Map<String, Map<String, List<String>>> words;
    private final Yaml yaml;

    public Dictionary() {
        this.yaml = new Yaml();
        this.words = new HashMap<>();
    }

    public void load(String resourcePath) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new RuntimeException("Ошибка в чтении файла");
            }

            Map<String, Object> data = yaml.load(inputStream);

            @SuppressWarnings("unchecked") Map<String, Map<String, List<String>>> rawCategories = (Map<String, Map<String, List<String>>>) data.get("categories");

            this.words = rawCategories;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки словаря из ресурса: " + resourcePath, e);
        }
    }

    public String getRandomWord(String category, String difficulty) {
        if (!words.containsKey(category)) {
            throw new IllegalArgumentException("Категория '" + category + "' не найдена. Доступные категории: " + words.keySet());
        }
        List<String> certainWords = words.get(category).get(difficulty);
        Random random = new Random();
        return certainWords.get(random.nextInt(certainWords.size()));
    }

    public Set<String> getCategories() {
        return words.keySet();
    }



}
