package edu.upvictoria.fpoo;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

public class SQL {

    private String path = null;
    //private File reservedCSV = new File("reserved.csv");
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
        System.out.println(words);

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

    private boolean crearArchivo(String nombre) {

        try {
            File archivo = new File(path + "/" + nombre + ".csv");

            if (!archivo.exists()) {
                archivo.createNewFile();
            }else{
                System.out.println("Tabla " + nombre + " ya existe");
            }

            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
        }

        return false;
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
            System.out.println("Bad Sentence");
            return;
        }

        csvName = words.get(2).toLowerCase();

        if (reservedWords.contains(csvName.toUpperCase())) {
            System.out.println("Can't create table: Name is a reserved word");
            return;
        }

       boolean bol = crearArchivo(csvName);

       if (!bol){
           System.out.println("La Tabla NO pudo ser creada");
           return;
       }

       for (String word : words) {

       }

    }

    //TODO: buscar ( en la lista, que el siguiente i lo tome como el nombre para ingresar y la , que cambia hasta que encuentre un ;


    private void insertINTO(List<String> words) {

        if (!words.get(3).toUpperCase().equals("VALUES") && !words.get(4).equals("(")) {
            System.out.println("Bad Sentence");
            return;
        }

        String csvName = words.get(2).toLowerCase();

        System.out.println(path + "/" +csvName + ".csv");


        File carpeta = new File(path + "/" +csvName + ".csv");

        if (!carpeta.exists()) {
            System.out.println("Table " + csvName + " does not exist");
        }

        //TODO:INSERTAR LOS VALORES, CHECANDO QUE ESTEN TODOS LOS VALORES, SINO NO LO DEJE


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
