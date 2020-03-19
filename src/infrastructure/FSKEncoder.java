package infrastructure;

import java.lang.*;
import java.util.*;

public class FSKEncoder {
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
            modulate16();
        }
    }
    private int[] data;
    private double[] modulated;
    private int[] frequencies;

    private FSKConfig fskConfig;
    private SignalGenerator signalGenerator;

    public FSKEncoder(FSKConfig fskConfig) {
        this.fskConfig = fskConfig;
        signalGenerator = new SignalGenerator(fskConfig);
        initFrequencies();
    }

    public FSKEncoder(FSKConfig fskConfig, int[] data) {
        this.fskConfig = fskConfig;
        signalGenerator = new SignalGenerator(fskConfig);
        this.data = data;
        initFrequencies();
    }

    private void initFrequencies() {
        int frequencyInterval = (fskConfig.modemFreqHigh - fskConfig.modemFreqLow) / 16;
        frequencies = new int[16];
        for (int i = 0; i < frequencies.length; i++) {
            frequencies[i] = (i * frequencyInterval) + fskConfig.modemFreqLow;
        }
    }

    public void modulate16() {
        double[] chirpSignalStart = signalGenerator.generateChirp();
        modulated = signalGenerator.concatenateSignals(modulated, chirpSignalStart);
        for (int i = 0; i < data.length - 3; i+=4) {
            int[] hexa = Arrays.copyOfRange(data, i, i+4);
            int decimal = hexaToDecimal(hexa);
            double[] signal = signalGenerator.generate(frequencies[decimal]);
            modulated = signalGenerator.concatenateSignals(modulated, signal);
        }
        if(onEncodeFinishListener != null) {
            onEncodeFinishListener.onEncodeFinish(modulated);
        }
    }

    private int hexaToDecimal(int[] hexa) {
        int sum = 0;
        int counter = 0;
        for (int j = hexa.length-1; j >= 0; j--) {
            sum+=(int)Math.pow(2,counter)*hexa[j];
            counter++;
        }
        return sum;
    }

    public void startModulation() {
        EncoderThread encoderThread = new EncoderThread();
        encoderThread.start();
    }

    public void setData(int[] data) {
        this.data = data;
    }

    public double[] getModulated() {
        return modulated;
    }
}

