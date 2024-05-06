package edu.upvictoria.fpoo;

import java.io.FileReader;
import java.io.BufferedReader;

public class CSV {

    private BufferedReader lector;
    private  String linea;
    private  String partes[] = null;

    public void leerArchivo(String nombreArchivo) {

        try {
            lector = new BufferedReader(new FileReader(nombreArchivo));
            while ((linea = lector.readLine()) != null) {
                partes = linea.split(",");
                imprimirLinea();
                System.out.println();
            }
            lector.close();
            linea = null;
            partes = null;
        }catch ( Exception e){

            e.printStackTrace();
        }
    }

    public void imprimirLinea() {
        for (int i = 0; i < partes.length; i++) {
            System.out.println(partes[i]);
        }
    }
}
