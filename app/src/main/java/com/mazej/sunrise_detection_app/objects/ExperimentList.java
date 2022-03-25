package com.mazej.sunrise_detection_app.objects;

import java.util.ArrayList;

public class ExperimentList {
    private ArrayList<Experiment> list;

    public ExperimentList() {
        this.list = new ArrayList<>();
    }

    public ArrayList<Experiment> getList() {
        return list;
    }

    public void setList(ArrayList<Experiment> list) {
        this.list = list;
    }

    public void add(Experiment exp) {
        list.add(exp);
    }
}
