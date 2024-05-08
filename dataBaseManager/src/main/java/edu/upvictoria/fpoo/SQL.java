package edu.upvictoria.fpoo;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

public class SQL {

    private String path = null;
    private List<String> words = null;

    public void reader(String sentence){
        words = parseSentence(sentence);

        System.out.println(words);

        if(words.isEmpty()){
            System.out.println("No sentence found");
        }



    }

    private void usePath(String path) {

        File carpeta = new File(path);

        if (!carpeta.exists()) {
            System.out.println("Path " + path + " does not exist");
        } else {
            System.out.println("Path setted correctly");
            this.path = path;
        }
    }

    private List<String> parseSentence(String sentence) {
        List<String> tokens = new ArrayList<>();

        Pattern usePattern = Pattern.compile("^USE\\s+(.+)$",Pattern.CASE_INSENSITIVE);
        Pattern tokenPattern = Pattern.compile("\\s+\\$\\w+\\$|\\w+|\\*|\\(|\\)|,|;");
        Matcher tokenMatcher = usePattern.matcher(sentence);

        if (tokenMatcher.matches()) {
            tokens.add(tokenMatcher.group(1));
            usePath(tokenMatcher.group(1));
            tokens.clear();
            return tokens;
        }

        tokenMatcher = tokenPattern.matcher(sentence);

        while (tokenMatcher.find()) {
            tokens.add(tokenMatcher.group());
        }

        return tokens;
    }
}
