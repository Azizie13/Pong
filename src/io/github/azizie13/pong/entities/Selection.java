package io.github.azizie13.pong.entities;

import java.awt.*;
import java.util.ArrayList;

public class Selection {

    private final int index;
    private final String text;
    private int optionIndex;

    public Color color;
    private final ArrayList<String> options;

    public Selection(int index, String text, Color color, ArrayList<String> options) {
        this.index = index;
        this.text = text;
        this.color = color;
        this.options = options;

        this.optionIndex = 0;
    }

    public int getIndex() {
        return index;
    }

    public String getText() {
        return text;
    }

    public int getOption() {
        return optionIndex;
    }

    public void setOption(int index){
        optionIndex = index;
    }

    public ArrayList<String> getOptions() {
        return options;
    }
}
