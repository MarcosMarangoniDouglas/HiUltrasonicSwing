package infrastructure;

import java.util.ArrayList;

public class SignalGenerator {
    private double symbolSize;
    private double f;
    private double stepSize;
    private double sampleRate;
    private ArrayList<Double> data;
    private ArrayList<Double> sync;

    public SignalGenerator(double symbolSize, double f, double stepSize) {
        this.symbolSize = symbolSize;
        this.f = f;
        this.stepSize = stepSize;
        this.sampleRate = 1.0 / stepSize;
    }
    public double getSymbolSize() {
        return symbolSize;
    }
    public void setSymbolSize(double symbolSize) {
        this.symbolSize = symbolSize;
    }
    public double getF() {
        return f;
    }
    public void setF(double f) {
        this.f = f;
    }
    public double getStepSize() {
        return stepSize;
    }
    public void setStepSize(double stepSize) {
        this.stepSize = stepSize;
    }
    public ArrayList<Double> getData() {
        return data;
    }
    public ArrayList<Double> getSync() {
        return sync;
    }
    public ArrayList<Double> generate() {
        try {
            data = new ArrayList<Double>();
            double rad = 0;
            // Why not sampleRate * stepSize ??
            for (int i = 0; i < symbolSize / stepSize; i++) {
                rad = (2 * Math.PI * f * i * stepSize);
                data.add(Math.cos(rad));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Function that generates the chirp signal (A signal that fluctuates over time)
     * x=cos(2*pi*(k/2*t+f0).*t+phase);
     * k=(f1-f0)/T;
     * k = rate of change
     * t = actual time
     * f1 = final frequency
     * f0 = initial frequency
     * T = time duration
     * phase is optional
     * @return The chirp signal
     */
    public ArrayList<Double> generateSync() {
        sync = new ArrayList<Double>();
        double rad = 0;
//        ATENUEI O SINAL DE INICIO
//        double k = ((16000 - 6000) / symbolSize);
        double k = ((3000) / symbolSize);
        for (int i = 0; i < symbolSize * sampleRate; i++) {
            rad = (2 * Math.PI * ((k / 2) * i * stepSize + 14000) * i * stepSize);
            sync.add(Math.cos(rad));
        }
        return sync;
    }
}
