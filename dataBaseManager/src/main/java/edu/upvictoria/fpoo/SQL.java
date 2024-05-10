package edu.upvictoria.fpoo;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SQL {

    private String path = null;
    private final List<String> reservedWords = new ArrayList<>();

    public void reader(String sentence)  {

        List<String> words;

        addReservedWords();
        //System.out.println(sentence);

        Pattern usePattern = Pattern.compile("^USE\\s+(.+)",Pattern.CASE_INSENSITIVE);
        Matcher tokenMatcher = usePattern.matcher(sentence);

        if (tokenMatcher.matches()) {
            usePath(tokenMatcher.group(1));
            return;
        }

        words = parseSentence(sentence);
        //System.out.println(words);

        if (words.isEmpty()) {
            System.out.println("No sentence found");
            return;
        }else if (path == null){
            System.out.println("Path is null");
            return;
        }

        if (words.size() < 3 &&
                (words.get(0).toUpperCase().equals("SHOW")
                        && words.get(1).toUpperCase().equals("TABLES"))) {
           showTables();
           return;
        }

        String isSemicolon = words.get(words.size() - 1);
        if (!isSemicolon.equals(";")) {
            System.out.println("No semicolon");
            return;
        }

        if (words.get(0).toUpperCase().equals("CREATE") && words.get(1).toUpperCase().equals("TABLE") ) {
            createTable(words);
        }

        if (words.get(0).toUpperCase().equals("INSERT") && words.get(1).toUpperCase().equals("INTO") ) {
            insertINTO(words);
        }

        if (words.get(0).toUpperCase().equals("DROP") && words.get(1).toUpperCase().equals("TABLE") ) {
            dropTable(words);
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

        Pattern tokenPattern = Pattern.compile("\\s+\\$\\w+\\$|\\w+|\\*|\\(|\\)|,|;");
        Matcher tokenMatcher = tokenPattern.matcher(sentence);


        while (tokenMatcher.find()) {
            tokens.add(tokenMatcher.group());
        }

        return tokens;
    }

    private void makeCSV(String nombre) {

        try {
            File archivo = new File(path + "/" + nombre + ".csv");

            if (!archivo.exists()) {
                archivo.createNewFile();
            }else{
                System.out.println("Tabla " + nombre + " ya existe");
                return;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
        }

    }

    private void showTables() {
        File carpeta = new File(path);
        boolean archivosEncontrados = false;

        File[] archivos = carpeta.listFiles();

        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isFile() && archivo.getName().endsWith(".csv")) {
                    archivosEncontrados = true;
                    String fileName = archivo.getName().replaceFirst("[.][^.]+$", "");
                    System.out.println("-" + fileName);
                }
            }
        }

        if (!archivosEncontrados) {
            System.out.println("No tables yet");
        }

    }

    private void createTable(List<String> words) {
        String csvName;

        if (!words.get(3).equals("(")) {
            System.out.println("Bad Sentence: Missing parenthesis");
            return;
        }

        int endIndex = -1;
        for (int i = 3; i < words.size(); i++) {
            if (words.get(i).equals(")") && words.get(i+1).equals(";")) {
                endIndex = i;
                break;
            }
        }

        if (endIndex == -1) {
            System.out.println("Bad Sentence: Missing closing parenthesis");
            return;
        }

        csvName = words.get(2).toLowerCase();

        if (reservedWords.contains(csvName.toUpperCase())) {
            System.out.println("Can't create table: Name is a reserved word");
            return;
        }

        makeCSV(csvName);

        StringBuilder columnNamesBuilder = new StringBuilder();
        boolean isFirstColumn = true;
        boolean foundFirstParenthesis = false;
        for (int i = 3; i < endIndex; i++) {
            String word = words.get(i);
            if (word.equals("(")) {
                foundFirstParenthesis = true;
            } else if (word.equals(",")) {
                if (foundFirstParenthesis) {

                    if (!isFirstColumn) {
                        columnNamesBuilder.append(", ");
                    }
                    String columnName = words.get(i+1);
                    columnNamesBuilder.append(columnName);
                    isFirstColumn = false;
                }
            } else if (foundFirstParenthesis && isFirstColumn) {
                columnNamesBuilder.append(word);
                isFirstColumn = false;
            }
        }

        String columnNames = columnNamesBuilder.toString();

        String[] columnNamesArray = columnNames.split(", ");
        for (String columnName : columnNamesArray) {
            if (reservedWords.contains(columnName.toUpperCase())) {
                System.out.println("Error: The Column '" + columnName + "' is a reserved word.");
                return;
            }
        }

        if (writeToCSV(columnNames,csvName,true)){
            System.out.println("Table " + csvName + " created");
        }

    }

    private boolean writeToCSV(String columnNames, String csvName, boolean isCreated) {

        if (isCreated){
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path + "/" + csvName + ".csv",true))) {
                writer.append(columnNames);
                return true;
            } catch (IOException e) {
                System.err.println("Error writing to CSV file: " + e.getMessage());
                return false;
            }
        }else {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path + "/" + csvName + ".csv", true))) {
                writer.newLine();
                writer.write(columnNames);
                return true;
            } catch (IOException e) {
                System.err.println("Error appending line to CSV file: " + e.getMessage());
                return false;
            }
        }

    }

    private void dropTable(List<String> words) {

        if (words.size()>4){
            System.out.println("Error: Bad Syntax");
            return;
        }
        String nombre = words.get(2).toLowerCase();

        try {
            File archivo = new File(path + "/" + nombre + ".csv");

            if (!archivo.exists()) {
                System.out.println("Table does not exist");
            }else{
                archivo.delete();
                System.out.println("Table deleted correctly");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
        }

    } // use /home/wallez/Documents/iti-271215-poo-practica-1-ElWalleZ/dataBaseManager/Tablas
    // insert into snicker values("valor","valor2",123);

    private void insertINTO(List<String> words) {

        if (!words.get(3).toUpperCase().equals("VALUES") || !words.get(4).equals("(")) {
            System.out.println("Error: Bad Syntax");
            return;
        }

        String csvName = words.get(2).toLowerCase();

        File carpeta = new File(path + "/" +csvName + ".csv");

        if (!carpeta.exists()) {
            System.out.println("Table " + csvName + " does not exist");
            return;
        }

        int valuesStartIndex = 5;
        int valuesEndIndex = words.size() - 2;
        List<String> valores = words.subList(valuesStartIndex, valuesEndIndex);

        StringBuilder concatenatedValues = new StringBuilder();
        for (String valor : valores) {
            concatenatedValues.append(valor);
        }

        int countedValues = countValues(concatenatedValues.toString());

        int numExpected = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(carpeta))) {
            String primeraLinea = reader.readLine();
            String[] columnas = primeraLinea.split(",");
            numExpected = columnas.length;
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            return;
        }

        if (countedValues != numExpected) {
            System.out.println("Error: Number of values provided does not match the number of columns.");
            return;
        }

        writeToCSV(concatenatedValues.toString(),csvName,false);
    }

    private int countValues(String concatenatedValues) {
        int count = 0;

        Pattern pattern = Pattern.compile("\"[^\"]*\"|[^,]+");
        Matcher matcher = pattern.matcher(concatenatedValues);

        while (matcher.find()) {
            count++;
        }

        return count;
    }


    private void addReservedWords() {
        reservedWords.add("ABORT");
        reservedWords.add("ALL");
        reservedWords.add("ALLOCATE");
        reservedWords.add("ANALYSE");
        reservedWords.add("ANALYZE");
        reservedWords.add("AND");
        reservedWords.add("ANY");
        reservedWords.add("AS");
        reservedWords.add("ASC");
        reservedWords.add("BETWEEN");
        reservedWords.add("BINARY");
        reservedWords.add("BIT");
        reservedWords.add("BOTH");
        reservedWords.add("CASE");
        reservedWords.add("CAST");
        reservedWords.add("CHAR");
        reservedWords.add("CHARACTER");
        reservedWords.add("CHECK");
        reservedWords.add("CLUSTER");
        reservedWords.add("COALESCE");
        reservedWords.add("COLLATE");
        reservedWords.add("COLLATION");
        reservedWords.add("COLUMN");
        reservedWords.add("CONSTRAINT");
        reservedWords.add("COPY");
        reservedWords.add("CROSS");
        reservedWords.add("CURRENT");
        reservedWords.add("CURRENT_CATALOG");
        reservedWords.add("CURRENT_DATE");
        reservedWords.add("CURRENT_DB");
        reservedWords.add("CURRENT_SCHEMA");
        reservedWords.add("CURRENT_SID");
        reservedWords.add("CURRENT_TIME");
        reservedWords.add("CURRENT_TIMESTAMP");
        reservedWords.add("CURRENT_USER");
        reservedWords.add("CURRENT_USERID");
        reservedWords.add("CURRENT_USEROID");
        reservedWords.add("DEALLOCATE");
        reservedWords.add("DECIMAL");
        reservedWords.add("DEC");
        reservedWords.add("DEFAULT");
        reservedWords.add("DESC");
        reservedWords.add("DISTINCT");
        reservedWords.add("DISTRIBUTE");
        reservedWords.add("DO");
        reservedWords.add("ELSE");
        reservedWords.add("END");
        reservedWords.add("EXCEPT");
        reservedWords.add("EXCLUDE");
        reservedWords.add("EXPRESS");
        reservedWords.add("EXTEND");
        reservedWords.add("EXTERNAL");
        reservedWords.add("EXTRACT");
        reservedWords.add("FALSE");
        reservedWords.add("FIRST");
        reservedWords.add("FLOAT");
        reservedWords.add("FOLLOWING");
        reservedWords.add("FOREIGN");
        reservedWords.add("FOR");
        reservedWords.add("FROM");
        reservedWords.add("FULL");
        reservedWords.add("FUNCTION");
        reservedWords.add("GENSTATS");
        reservedWords.add("GLOBAL");
        reservedWords.add("GROUP");
        reservedWords.add("HAVING");
        reservedWords.add("IDENTIFIER_CASE");
        reservedWords.add("ILIKE");
        reservedWords.add("IN");
        reservedWords.add("INOUT");
        reservedWords.add("INNER");
        reservedWords.add("INTO");
        reservedWords.add("INTERVAL");
        reservedWords.add("JOIN");
        reservedWords.add("LEADING");
        reservedWords.add("LEFT");
        reservedWords.add("LIKE");
        reservedWords.add("LIMIT");
        reservedWords.add("LOCAL");
        reservedWords.add("LOCK");
        reservedWords.add("MINUS");
        reservedWords.add("MOVE");
        reservedWords.add("NATURAL");
        reservedWords.add("NCHAR");
        reservedWords.add("NEW");
        reservedWords.add("NOT");
        reservedWords.add("NOTNULL");
        reservedWords.add("NULL");
        reservedWords.add("NULLS");
        reservedWords.add("NVL");
        reservedWords.add("NVL2");
        reservedWords.add("OFFSET");
        reservedWords.add("OLD");
        reservedWords.add("ON");
        reservedWords.add("ONLINE");
        reservedWords.add("ONLY");
        reservedWords.add("OR");
        reservedWords.add("OTHERS");
        reservedWords.add("OUT");
        reservedWords.add("OUTER");
        reservedWords.add("OVER");
        reservedWords.add("OVERLAPS");
        reservedWords.add("PARTITION");
        reservedWords.add("POSITION");
        reservedWords.add("PRECEDING");
        reservedWords.add("PRECISION");
        reservedWords.add("PRIMARY");
        reservedWords.add("RESET");
        reservedWords.add("REUSE");
        reservedWords.add("RIGHT");
        reservedWords.add("ROWS");
        reservedWords.add("SELECT");
        reservedWords.add("SESSION_USER");
        reservedWords.add("SETOF");
        reservedWords.add("SHOW");
        reservedWords.add("SOME");
        reservedWords.add("TABLE");
        reservedWords.add("THEN");
        reservedWords.add("TIME");
        reservedWords.add("TIMESTAMP");
        reservedWords.add("TO");
        reservedWords.add("TRAILING");
        reservedWords.add("TRANSACTION");
        reservedWords.add("TRIGGER");
        reservedWords.add("TRIM");
        reservedWords.add("TRUE");
        reservedWords.add("UNBOUNDED");
        reservedWords.add("UNION");
        reservedWords.add("UNIQUE");
        reservedWords.add("USER");
        reservedWords.add("USING");
        reservedWords.add("VERBOSE");
        reservedWords.add("VERSION");
        reservedWords.add("VIEW");
        reservedWords.add("WHEN");
        reservedWords.add("WHERE");
        reservedWords.add("WITH");
        reservedWords.add("WRITE");
    }


}
