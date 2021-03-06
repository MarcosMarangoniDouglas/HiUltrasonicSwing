package infrastructure;


import java.io.IOException;

public class FSKConfig {

    public static final int SAMPLE_RATE_44100 = 44100; // LCMx2
    public static final int SAMPLE_RATE_22050 = 22050; // LEAST COMMON MULTIPLE
    // OF 2450, 1225, 630,
    // 315 and 126
    public static final int SAMPLE_RATE_29400 = 29400; // DEFAULT for 1225; 24
    // samples per bit

    public static final int PCM_8BIT = 8;
    public static final int PCM_16BIT = 16;

    public static final int CHANNELS_MONO = 1;
    public static final int CHANNELS_STEREO = 2;

    //  High frequency
    public static final int SOFT_MODEM_MODE_1 = 1;
    public static final int SOFT_MODEM_MODE_1_BAUD_RATE = 2;
    public static final int SOFT_MODEM_MODE_1_LOW_FREQ = 12000;
    public static final int SOFT_MODEM_MODE_1_HIGH_FREQ = 14000;
    public static final int SOFT_MODEM_MODE_1_CHIRP_BAUD_RATE = 2;
    public static final int SOFT_MODEM_MODE_1_CHIRP_LOW_FREQ = 15000;
    public static final int SOFT_MODEM_MODE_1_CHIRP_HIGH_FREQ = 17000;

    // Low frequency
    public static final int SOFT_MODEM_MODE_2 = 2;
    public static final int SOFT_MODEM_MODE_2_BAUD_RATE = 2;
    public static final int SOFT_MODEM_MODE_2_LOW_FREQ = 500;
    public static final int SOFT_MODEM_MODE_2_HIGH_FREQ = 1000;
    public static final int SOFT_MODEM_MODE_2_CHIRP_BAUD_RATE = 2;
    public static final int SOFT_MODEM_MODE_2_CHIRP_LOW_FREQ = 100;
    public static final int SOFT_MODEM_MODE_2_CHIRP_HIGH_FREQ = 500;

    // /

    public int sampleRate;
    public int pcmFormat;
    public int channels;
    public int modemMode;

    // /

    public int samplesPerBit;

    public int modemBaudRate;
    public int modemChirpBaudRate;
    public int modemFreqLow;
    public int modemFreqHigh;
    public int modemChirpFreqLow;
    public int modemChirpFreqHigh;

    public FSKConfig(int sampleRate, int pcmFormat, int channels,
                     int modemMode) {
        this.sampleRate = sampleRate;
        this.pcmFormat = pcmFormat;
        this.channels = channels;
        this.modemMode = modemMode;

        switch (modemMode) {
            case SOFT_MODEM_MODE_1:

                this.modemBaudRate = SOFT_MODEM_MODE_1_BAUD_RATE;
                this.modemFreqLow = SOFT_MODEM_MODE_1_LOW_FREQ;
                this.modemFreqHigh = SOFT_MODEM_MODE_1_HIGH_FREQ;
                this.modemChirpFreqLow = SOFT_MODEM_MODE_1_CHIRP_LOW_FREQ;
                this.modemChirpFreqHigh = SOFT_MODEM_MODE_1_CHIRP_HIGH_FREQ;
                this.modemChirpBaudRate = SOFT_MODEM_MODE_1_CHIRP_BAUD_RATE;

                break;

            case SOFT_MODEM_MODE_2:

                this.modemBaudRate = SOFT_MODEM_MODE_2_BAUD_RATE;
                this.modemFreqLow = SOFT_MODEM_MODE_2_LOW_FREQ;
                this.modemFreqHigh = SOFT_MODEM_MODE_2_HIGH_FREQ;
                this.modemChirpFreqLow = SOFT_MODEM_MODE_2_CHIRP_LOW_FREQ;
                this.modemChirpFreqHigh = SOFT_MODEM_MODE_2_CHIRP_HIGH_FREQ;
                this.modemChirpBaudRate = SOFT_MODEM_MODE_2_CHIRP_BAUD_RATE;

                break;

        }

        this.samplesPerBit = this.sampleRate / this.modemBaudRate;

    }

    public FSKConfig(int sampleRate, int pcmFormat, int channels,
                     CustomModemMode modemMode) throws IOException {
        this.sampleRate = sampleRate;
        this.pcmFormat = pcmFormat;
        this.channels = channels;

        this.modemBaudRate = modemMode.modemBaudRate;
        this.modemFreqLow = modemMode.modemFreqLow;
        this.modemFreqHigh = modemMode.modemFreqHigh;
        this.modemChirpFreqLow = modemMode.modemChirpFreqLow;
        this.modemChirpFreqHigh = modemMode.modemChirpFreqHigh;
        this.modemChirpBaudRate = modemMode.modemChirpBaudRate;

        this.samplesPerBit = this.sampleRate / this.modemBaudRate;

    }

}

