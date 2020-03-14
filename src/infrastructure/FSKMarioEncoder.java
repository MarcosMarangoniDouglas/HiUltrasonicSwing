package infrastructure;

import java.lang.*;
import java.util.*;

public class FSKMarioEncoder {


    private OnEncodeFinishListener onEncodeFinishListener;
    public void setOnEncodeFinishListener(OnEncodeFinishListener onEncodeFinishListener) {
        this.onEncodeFinishListener = onEncodeFinishListener;
    }
    public interface OnEncodeFinishListener {
        void onEncodeFinish(double[] modulatedData);
    }

    class EncoderThread extends Thread {
        @Override
        public void run() {
            super.run();
            modulate();
        }
    }

    private int[] data;
    private double sampleRate;
    private double symbolSize; // Size of the symbol converted to digital means (sample rate)
    private double samplePeriod;
    private int numberOfCarriers;
    private int[] frequencies;
    private int fs;                    //Start Frequency
    private ArrayList<Double> modulated;
    private ArrayList<SignalGenerator> carriers;

    int[] s_0 = {0, 0, 0, 0}; // Frequency carrier 1
    int[] s_1 = {0, 0, 0, 1}; // Frequency carrier 2
    int[] s_2 = {0, 0, 1, 0}; // ...
    int[] s_3 = {0, 0, 1, 1};
    int[] s_4 = {0, 1, 0, 0};
    int[] s_5 = {0, 1, 0, 1};
    int[] s_6 = {0, 1, 1, 0};
    int[] s_7 = {0, 1, 1, 1};
    int[] s_8 = {1, 0, 0, 0};
    int[] s_9 = {1, 0, 0, 1};
    int[] s_10 = {1, 0, 1, 0};
    int[] s_11 = {1, 0, 1, 1};
    int[] s_12 = {1, 1, 0, 0};
    int[] s_13 = {1, 1, 0, 1};
    int[] s_14 = {1, 1, 1, 0};
    int[] s_15 = {1, 1, 1, 1};

    public FSKMarioEncoder(double sampleRate) {
        this.sampleRate = sampleRate;
        this.symbolSize = 0.5;
        this.samplePeriod = 1 / sampleRate;
        this.numberOfCarriers = 16;
        this.fs = 12000; //8000;//12000;
        initFrequencies();
        initCarriers();
    }

    public FSKMarioEncoder(String data, double sampleRate) {
        String tmpData = data;
        while (tmpData.length() != 5) {
            tmpData += " ";
        }
        StringHandler stringHandler = new StringHandler(tmpData);
        this.data = stringHandler.getB();
        this.sampleRate = sampleRate;
        this.symbolSize = 0.5;
        this.samplePeriod = 1 / sampleRate;
        this.numberOfCarriers = 16;
        this.fs = 17000; //8000;//12000;
        initFrequencies();
        initCarriers();
    }

    public void initFrequencies() {
        frequencies = new int[numberOfCarriers];
        frequencies[0] = fs;
        for (int i = 1; i < numberOfCarriers; i++) {
            frequencies[i] = frequencies[i - 1] + 125;
        }
    }

    public void initCarriers() {
        carriers = new ArrayList<SignalGenerator>();
        for (int i = 0; i < numberOfCarriers; i++) {
            SignalGenerator s = new SignalGenerator(symbolSize, frequencies[i], 1.0 / 44100.0);
            carriers.add(s);
        }
    }

    public void startModulation() {
        EncoderThread encoderThread = new EncoderThread();
        encoderThread.start();
    }

    public void modulate() {
        int temp[] = new int[4];
        modulated = new ArrayList<Double>();
        modulated.addAll(carriers.get(0).generateSync()); //Adding the synchronization signal.
        for (int i = 0; i < data.length - 3; i += 4) {
            temp[0] = data[i];
            temp[1] = data[i + 1];
            temp[2] = data[i + 2];
            temp[3] = data[i + 3];
            map(temp);
        }

        double[] tmpModulated = new double[modulated.size()];
        for (int i = 0; i < modulated.size(); i++) {
            tmpModulated[i] = modulated.get(i);
        }

        if(onEncodeFinishListener != null) {
            onEncodeFinishListener.onEncodeFinish(tmpModulated);
        }
    }

    public void map(int[] temp) {
        if (Arrays.equals(temp, s_0)) {
            modulated.addAll(carriers.get(0).generate());
        } else if (Arrays.equals(temp, s_1)) {
            modulated.addAll(carriers.get(1).generate());
        } else if (Arrays.equals(temp, s_2)) {
            modulated.addAll(carriers.get(2).generate());
        } else if (Arrays.equals(temp, s_3)) {
            modulated.addAll(carriers.get(3).generate());
        } else if (Arrays.equals(temp, s_4)) {
            modulated.addAll(carriers.get(4).generate());
        } else if (Arrays.equals(temp, s_5)) {
            modulated.addAll(carriers.get(5).generate());
        } else if (Arrays.equals(temp, s_6)) {
            modulated.addAll(carriers.get(6).generate());
        } else if (Arrays.equals(temp, s_7)) {
            modulated.addAll(carriers.get(7).generate());
        } else if (Arrays.equals(temp, s_8)) {
            modulated.addAll(carriers.get(8).generate());
        } else if (Arrays.equals(temp, s_9)) {
            modulated.addAll(carriers.get(9).generate());
        } else if (Arrays.equals(temp, s_10)) {
            modulated.addAll(carriers.get(10).generate());
        } else if (Arrays.equals(temp, s_11)) {
            modulated.addAll(carriers.get(11).generate());
        } else if (Arrays.equals(temp, s_12)) {
            modulated.addAll(carriers.get(12).generate());
        } else if (Arrays.equals(temp, s_13)) {
            modulated.addAll(carriers.get(13).generate());
        } else if (Arrays.equals(temp, s_14)) {
            modulated.addAll(carriers.get(14).generate());
        } else if (Arrays.equals(temp, s_15)) {
            modulated.addAll(carriers.get(15).generate());
        }
    }

    public void setData(String data) {
        String tmpData = data;
        while (tmpData.length() != 5) {
            tmpData += " ";
        }
        StringHandler stringHandler = new StringHandler(tmpData);
        this.data = stringHandler.getB();
    }

    public double getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(double sample_rate) {
        this.sampleRate = sample_rate;
    }

    public double getSymbolSize() {
        return symbolSize;
    }

    public double getSamplePeriod() {
        return samplePeriod;
    }

    public int getNumberOfCarriers() {
        return numberOfCarriers;
    }

    public int[] getFrequencies() {
        return frequencies;
    }

    public int getFs() {
        return fs;
    }

    public ArrayList<Double> getModulated() {
        return modulated;
    }

    public ArrayList<SignalGenerator> getCarriers() {
        return carriers;
    }
}

