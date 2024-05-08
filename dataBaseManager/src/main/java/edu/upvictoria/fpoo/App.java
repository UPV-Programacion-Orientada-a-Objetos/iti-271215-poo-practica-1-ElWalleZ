package edu.upvictoria.fpoo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class App
{
    public static void main( String[] args )
    {
        SQL sql = new SQL();
        boolean alive = true;
        do {
            String line = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            try {
                line = br.readLine();
            } catch (IOException e) {
                System.err.println("Ha ocurrido un error al leer el entrada");
            }

            if (line.equals("-m")) { alive = false; }

            sql.reader(line);

        }while (alive);

        System.out.println("Saliste del Programa");

    }
}

