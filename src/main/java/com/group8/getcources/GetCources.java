package com.group8.getcources;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class GetCources {

    public List<Cource> getCources() {
        String everything = "";
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/group8/getcources/top-academy-parsed.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Cource> list = new LinkedList<>();
        String[] array = everything.split("\n");
        for (int i = 0; i < array.length - 6; i+=6) {
            String URL = array[i];
            String name = array[i+1];
            String length = array[i+2];
            String imageURL = array[i+3];
            String rate = array[i+4];
            String cost = array[i+5];
            Cource c = new Cource(URL, name, length, imageURL, rate, cost);
            list.add(c);
        }

        return list;
    }

}
