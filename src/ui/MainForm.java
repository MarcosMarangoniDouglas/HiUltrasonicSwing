package ui;

import com.sun.media.sound.WaveFileWriter;
import infrastructure.*;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;

public class MainForm extends JFrame
        implements FSKEncoder.OnEncodeFinishListener {
    private JPanel mainPanel;
    private JLabel lblAppName;
    private JTextField txtInput;
    private JButton btnGenerate;
    private JLabel lblEncodeStatus;

    private JTextField txtFSKBaud;
    private JTextField txtFSKLowFreq;
    private JTextField txtFSKHighFreq;
    private JTextField txtChirpBaudRate;
    private JTextField txtChirpLowFreq;
    private JTextField txtChirpHighFreq;

    FSKEncoder fskEncoder;
    FSKConfig fskConfig;
    CustomModemMode customModemMode;
    JsonFileHandler jsonFileHandler;

    public MainForm() {
        fskEncoder = new FSKEncoder();
        fskEncoder.setOnEncodeFinishListener(this);
        jsonFileHandler = new JsonFileHandler("./Inputs/modem_inputs.json");

        customModemMode = jsonFileHandler.read();
        if(customModemMode != null) {
            txtInput.setText(customModemMode.input);
            txtFSKBaud.setText(customModemMode.modemBaudRate + "");
            txtFSKHighFreq.setText(customModemMode.modemFreqHigh + "");
            txtFSKLowFreq.setText(customModemMode.modemFreqLow + "");
            txtChirpBaudRate.setText(customModemMode.modemChirpBaudRate + "");
            txtChirpHighFreq.setText(customModemMode.modemChirpFreqHigh + "");
            txtChirpLowFreq.setText(customModemMode.modemChirpFreqLow + "");
        }

        add(mainPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("HiUltrasonic App");
        this.setSize(400, 500);

        btnGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblEncodeStatus.setText("Encoding text into a wave...");
                StringBuilder input = new StringBuilder(txtInput.getText());
                while (input.length() < 5) {
                    input.append(" ");
                }
                StringHandler stringHandler = new StringHandler(input.toString());

                FSKConfig tmpFskConfig = validateCustomModemMode();
                if(tmpFskConfig != null) {
                    fskConfig = tmpFskConfig;
                } else return;
                jsonFileHandler.write(customModemMode);
                /**
                 * MARIO ENCODER PART
                 */
                fskEncoder.setData(stringHandler.getB());
                fskEncoder.setFskConfig(fskConfig);
                fskEncoder.startModulation();
            }
        });
    }

    private FSKConfig validateCustomModemMode() {
        try {
            int fskBaudRate = Integer.parseInt(txtFSKBaud.getText());
            int fskHighFreq = Integer.parseInt(txtFSKHighFreq.getText());
            int fskLowFreq = Integer.parseInt(txtFSKLowFreq.getText());
            int chirpBaudRate = Integer.parseInt(txtChirpBaudRate.getText());
            int chirpHighFreq = Integer.parseInt(txtChirpHighFreq.getText());
            int chirpLowFreq = Integer.parseInt(txtChirpLowFreq.getText());
            String input = txtInput.getText();

            customModemMode = new CustomModemMode();
            customModemMode.input = input;
            customModemMode.modemBaudRate = fskBaudRate;
            customModemMode.modemFreqHigh = fskHighFreq;
            customModemMode.modemFreqLow = fskLowFreq;
            customModemMode.modemChirpBaudRate = chirpBaudRate;
            customModemMode.modemChirpFreqHigh = chirpHighFreq;
            customModemMode.modemChirpFreqLow = chirpLowFreq;


            FSKConfig fskConfig = new FSKConfig(
                    FSKConfig.SAMPLE_RATE_44100,
                    FSKConfig.PCM_16BIT,
                    FSKConfig.CHANNELS_MONO,
                    customModemMode);
            return fskConfig;
        } catch (Exception ex) {
            lblEncodeStatus.setText("One of the inputs are not valid");
            return null;
        }
    }

    @Override
    public void onEncodeFinish(double[] modulatedData) {
        // Apply a volume to the modulated data
        double volume = 1.0;
        for (int i = 0; i < modulatedData.length; i++) {
            modulatedData[i] = modulatedData[i] * volume;
        }

        // Apply the 16 bit PCM and split the int into two bytes
        final byte[] byteBuffer = new byte[modulatedData.length * 2];
        int bufferIndex = 0;
        for (int i = 0; i < byteBuffer.length; i++) {
            final int x = (int) (modulatedData[bufferIndex++] * 32767.0);
            byteBuffer[i] = (byte) x;
            i++;
            // Get the last 8 bits by doing unsigned shifting
            byteBuffer[i] = (byte) (x >>> 8);
        }

        int bits = 16; // PCM16
        int channels = 1; // Mono
        AudioFormat format;
        format = new AudioFormat(44100.0f, bits, channels, true, false);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteBuffer);

        // Write the file
        try {
            File folder = new File("Sounds");
            if(!folder.exists()) {
                folder.mkdir();
            }
            File modulatedSound = new File(String.format("./Sounds/FSK_%d_%d_%d_Chirp_%d_%d_%d.wav",
                    customModemMode.modemBaudRate,customModemMode.modemFreqLow, customModemMode.modemFreqHigh,
                    customModemMode.modemChirpBaudRate, customModemMode.modemChirpFreqLow, customModemMode.modemChirpFreqHigh));
            Files.deleteIfExists(modulatedSound.toPath());
            AudioInputStream audioInputStream;
            audioInputStream = new AudioInputStream(inputStream, format, modulatedData.length);
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, modulatedSound);
            audioInputStream.reset();
            audioInputStream.close();
            lblEncodeStatus.setText("Encoding finished");
        } catch (IOException e) {
            lblEncodeStatus.setText("Error");
            e.printStackTrace();
        }

    }
}
