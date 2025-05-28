package dev.crypts.voicechatReports.data;


import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;

public class CircularPCMBuffer {
    private final byte[] buffer;
    private int writePos = 0;
    private boolean bufferFull = false;

    public CircularPCMBuffer(int capacity) {
        buffer = new byte[capacity];
    }

    public synchronized void addBytes(byte[] data) {
        for (byte b : data) {
            buffer[writePos++] = b;
            if (writePos >= buffer.length) {
                writePos = 0;
                bufferFull = true;
            }
        }
    }
    public synchronized byte[] getLinearisedBuffer() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        if (!bufferFull) {
            outputStream.write(buffer, 0, writePos);
        } else {
            outputStream.write(buffer, writePos, buffer.length - writePos);
            outputStream.write(buffer, 0, writePos);
        }

        return outputStream.toByteArray();
    }

    // Todo: Remove saving here and just convert to WAV
    public void convertPCMToWav() throws IOException {

        byte[] pcmData = this.getLinearisedBuffer();

        int sampleRate = 48000; // Set in Simple Voice Chat plugin, not sure if I can pull from the lib
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate, 16, 1, 2, sampleRate, false);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pcmData);
        AudioInputStream audioInputStream = new AudioInputStream(byteArrayInputStream, format, pcmData.length / format.getFrameSize());

        File wavFile = new File("output.wav");
        AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, wavFile);
    }

}
