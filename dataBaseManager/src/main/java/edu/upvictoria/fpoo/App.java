package edu.upvictoria.fpoo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App
{
    public static void main( String[] args )
    {
        CSV tabla = new CSV();
        String entrada = entradaTexto();
        System.out.println(entrada);

        tabla.leerArchivo("/home/wallez/Documents/iti-271215-poo-practica-1-ElWalleZ/dataBaseManager/Tablas/prueba.csv");

    }

    public static String entradaTexto() {
        String line = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            line = br.readLine();
        } catch (IOException e) {
            System.err.println("Ha ocurrido un error al leer el entrada");
        }

        return line;
    }


}

