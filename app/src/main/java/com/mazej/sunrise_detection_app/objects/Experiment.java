package com.mazej.sunrise_detection_app.objects;

public class Experiment {

    private String name;
    private String tester;
    private String scheduled_sunrise;
    private String detected_sunrise;

    public Experiment(String name, String tester, String scheduled_sunrise, String detected_sunrise) {
        this.name = name;
        this.tester = tester;
        this.scheduled_sunrise = scheduled_sunrise;
        this.detected_sunrise = detected_sunrise;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTester() {
        return tester;
    }

    public void setTester(String tester) {
        this.tester = tester;
    }

    public String getScheduled_sunrise() {
        return scheduled_sunrise;
    }

    public void setScheduled_sunrise(String scheduled_sunrise) {
        this.scheduled_sunrise = scheduled_sunrise;
    }

    public String getDetected_sunrise() {
        return detected_sunrise;
    }

    public void setDetected_sunrise(String detected_sunrise) {
        this.detected_sunrise = detected_sunrise;
    }
}
