import java.io.*;
import java.util.*;

class Book {
    private Set<String> excludedWords;
    private Map<String, Set<Integer>> wordIndex;

    public Book() {
        excludedWords = new HashSet<>();
        wordIndex = new TreeMap<>();
    }

    public void readExcludedWords(String excludedWordsFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(excludedWordsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                excludedWords.add(line.trim().toLowerCase());
            }
        }
    }

    public void indexPages(String[] pageFiles) throws IOException {
        int pageIndex = 1;
        for (String pageFile : pageFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(pageFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] words = line.split("\\W+");
                    for (String word : words) {
                        word = word.trim().toLowerCase();
                        if (!excludedWords.contains(word)) {
                            if (!wordIndex.containsKey(word)) {
                                wordIndex.put(word, new HashSet<>());
                            }
                            wordIndex.get(word).add(pageIndex);
                        }
                    }
                }
            }
            pageIndex++;
        }
    }

    public void writeIndex(String outputFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (Map.Entry<String, Set<Integer>> entry : wordIndex.entrySet()) {
                StringBuilder pages = new StringBuilder();
                for (int page : entry.getValue()) {
                    if (pages.length() > 0) {
                        pages.append(",");
                    }
                    pages.append(page);
                }
                writer.write(entry.getKey() + " : " + pages);
                writer.newLine();
            }
        }
    }
}

public class test {
    public static void main(String[] args) {
        Book book = new Book();
        try {
            book.readExcludedWords("exclude-words.txt");
            book.indexPages(new String[] { "Page1.txt", "Page2.txt", "Page3.txt" });
            book.writeIndex("index.txt");
            System.out.println("Index created successfully!");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}

