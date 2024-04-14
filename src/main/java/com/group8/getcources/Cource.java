package com.group8.getcources;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Cource {

    public String URL;
    public String name;
    public String length;
    public String image;
    public String rate;
    public String cost;

    public Cource(String URL, String name, String length, String ImageURL, String rate, String cost) {
        this.URL = URL;
        this.name = name;
        this.length = length;
        this.rate = rate;
        this.image = ImageURL;
        this.cost = cost;
    }
}