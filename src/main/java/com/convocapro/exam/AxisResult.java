package com.convocapro.exam;

public class AxisResult {
    private final String axisName;
    private final int correct;
    private final int total;

    public AxisResult(String axisName, int correct, int total) {
        this.axisName = axisName;
        this.correct = correct;
        this.total = total;
    }

    public String getAxisName() {
        return axisName;
    }

    public int getCorrect() {
        return correct;
    }

    public int getTotal() {
        return total;
    }

    public double getPercentage() {
        if (total == 0) {
            return 0.0;
        }
        return Math.round(correct * 10000.0 / total) / 100.0;
    }
}
