package infrastructure;

public class SignalGenerator {
    private FSKConfig fskConfig;

    public SignalGenerator(FSKConfig fskConfig) {
        this.fskConfig = fskConfig;
    }

    public double[] generate(double f) {
        double frame;
        double t;
        int numberOfFrames = fskConfig.sampleRate / fskConfig.modemBaudRate;
        double[] data = new double[numberOfFrames];

        for (int i = 0; i < numberOfFrames; i++) {
            t = i * (1/(double)fskConfig.sampleRate);
            frame = Math.cos(2 * Math.PI * f * t);
            data[i] = frame;
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
     * T = Time it takes to sweep from f0 to f1
     * phase is optional
     * @return The chirp signal
     */
    public double[] generateChirp() {
        double frame;
        double t;
        double f;
        double f0 = fskConfig.modemChirpFreqLow;
        double f1 = fskConfig.modemChirpFreqHigh;

        int numberOfFrames = fskConfig.sampleRate / fskConfig.modemChirpBaudRate;
        double[] chirp = new double[numberOfFrames];

        double T = 1/(double)fskConfig.modemChirpBaudRate;
        double k = (f1 - f0) / T;
        for (int i = 0; i < numberOfFrames; i++) {
            t = i * (1/(double)fskConfig.sampleRate);
            f = (k / 2) * t + f0;
            frame = Math.cos(2 * Math.PI * f * t);
            chirp[i] = frame;
        }
        return chirp;
    }

    public double[] concatenateSignals(double[] signalA, double[] signalB) {
        if (signalA == null) return signalB;
        if (signalB == null) return signalA;

        int aLen = signalA.length;
        int bLen = signalB.length;

        double[] c = new double[aLen + bLen];
        System.arraycopy(signalA, 0, c, 0, aLen);
        System.arraycopy(signalB, 0, c, aLen, bLen);

        return c;
    }
}
