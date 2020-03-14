package ui;

import infrastructure.FSKConfig;
import infrastructure.FSKEncoder;
import infrastructure.FSKMarioEncoder;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.ByteBuffer;

public class MainForm extends JFrame implements FSKMarioEncoder.OnEncodeFinishListener {
    private JPanel mainPanel;
    private JLabel lblAppName;
    private JTextField txtInput;
    private JButton btnGenerate;
    private JLabel lblEncodeStatus;

    FSKMarioEncoder fskMarioEncoder;

    public MainForm() {
        fskMarioEncoder = new FSKMarioEncoder(44100);
        fskMarioEncoder.setOnEncodeFinishListener(this);

        add(mainPanel);
        this.setTitle("HiUltrasonic App");
        this.setSize(400, 500);
        btnGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblEncodeStatus.setText("Encoding text into a wave...");
                String input = txtInput.getText();
                fskMarioEncoder.setData(input);
                fskMarioEncoder.startModulation();
            }
        });
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
            File modulatedSound = new File("./modulated_sound.wav");
            AudioInputStream audioInputStream;
            audioInputStream = new AudioInputStream(inputStream, format, modulatedData.length);
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, modulatedSound);
            lblEncodeStatus.setText("Encoding finished");
        } catch (IOException e) {
            lblEncodeStatus.setText("Error");
            e.printStackTrace();
        }

    }
}