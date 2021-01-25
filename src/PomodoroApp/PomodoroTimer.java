package PomodoroApp;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;

public class PomodoroTimer implements ActionListener {

    JFrame frame = new JFrame();
    JButton startButton = new JButton("Start");
    JButton pauseButton = new JButton("Pause");
    JButton resetButton = new JButton("Reset");
    JLabel timeLabel = new JLabel();
    JLabel statusLabel = new JLabel();
    private static final int POMO_TIME_MS = 1500000; // 1500000 = 25 minutes
    private static final int POMO_BREAK_TIME_MS = 300000; // 300000 = 5 minutes
    int remainingTime = POMO_TIME_MS;
    int seconds = 0;
    int minutes = (POMO_TIME_MS / 1000) / 60;
    boolean started = false;
    boolean onABreak = false;
    String secondsString = String.format("%02d",seconds);
    String minutesString = String.format("%02d",minutes);
    boolean isUnix;

    Timer timer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (remainingTime > 0){
                remainingTime -= 1000;
                minutes = (remainingTime/60000) % 60;
                seconds = (remainingTime/1000) % 60;
                secondsString = String.format("%02d",seconds);
                minutesString = String.format("%02d",minutes);
                timeLabel.setText(minutesString + ":" + secondsString);
            }
            else if(onABreak){
                onABreak = false;
                remainingTime = POMO_TIME_MS;
                statusLabel.setText("Work work!");
                timeLabel.setBackground(Color.red);
            }
            else{
                recess();
                System.out.println("Time is up");
            }

        }
    });

    PomodoroTimer(boolean isUnix){
        this.isUnix = isUnix;
        timeLabel.setText(minutesString + ":" + secondsString);
        timeLabel.setBounds(100,100,300,100);
        timeLabel.setFont(new Font("Verdana",Font.PLAIN,35));
        timeLabel.setBorder(BorderFactory.createBevelBorder(1));
        timeLabel.setOpaque(true);
        timeLabel.setHorizontalAlignment(JTextField.CENTER);

        statusLabel.setBounds(150,250,200,100);
        statusLabel.setFont(new Font("Verdana",Font.PLAIN,25));
        statusLabel.setBorder(BorderFactory.createBevelBorder(1));
        statusLabel.setOpaque(true);
        statusLabel.setHorizontalAlignment(JTextField.CENTER);

        startButton.setBounds(100,200,100,50);
        startButton.addActionListener(this);

        pauseButton.setBounds(200,200,100,50);
        pauseButton.addActionListener(this);

        resetButton.setBounds(300,200,100,50);
        resetButton.addActionListener(this);

        frame.add(startButton);
        frame.add(pauseButton);
        frame.add(resetButton);
        frame.add(timeLabel);
        frame.add(statusLabel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(520,420);
        frame.setLayout(null);
        frame.setVisible(true);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==startButton){
            start();
        }
        else if (e.getSource()==pauseButton){
            pause();
        }
        else if (e.getSource()==resetButton){
            reset();
        }
    }

    private void start(){
        if(!started){
            timeLabel.setBackground(Color.red);
            started = true;
            statusLabel.setText("Work work!");
            timer.start();
            timeLabel.setOpaque(true);
            if(onABreak){
                timeLabel.setBackground(Color.green);
                statusLabel.setText("On a break.");
            }
        }
    }
    private void pause() {
        started = false;
        timer.stop();
    }
    private void reset() {
        timeLabel.setOpaque(false);
        timeLabel.repaint();
        started = false;
        onABreak = false;
        timer.stop();
        remainingTime = POMO_TIME_MS;
        seconds = 0;
        minutes = 25;
        String secondsString = String.format("%02d",seconds);
        String minutesString = String.format("%02d",minutes);
        timeLabel.setText(minutesString + ":" + secondsString);
        statusLabel.setText("");
    }
    void recess(){
        onABreak = true;
        File alarmSound = new File("src/PomodoroApp/Ship_Brass_Bell-Mike_Koenig-1458750630.wav");
        if (isUnix) {
            playAlarmSoundLinuxCompatible(alarmSound);
        } else {
            playAlarmSound();
        }
        remainingTime = POMO_BREAK_TIME_MS;
        minutes = (POMO_BREAK_TIME_MS / 1000) / 60;;
        seconds = 0;
        secondsString = String.format("%02d",seconds);
        minutesString = String.format("%02d",minutes);
        timeLabel.setText(minutesString + ":" + secondsString);
        statusLabel.setText("On a break.");
        timeLabel.setBackground(Color.green);
    }

    private void playAlarmSound()
    {
        try
        {
            InputStream inputStream = getClass().getResourceAsStream("Ship_Brass_Bell-Mike_Koenig-1458750630.wav");
            AudioStream audioStream = new AudioStream(inputStream);
            AudioPlayer.player.start(audioStream);
        }
        catch (Exception e)
        {
            System.err.println("There was an Exception thrown: " + e.getMessage());
            System.err.println("caused by:\n" + e.getStackTrace()[0].toString());
        }
    }

    private void playAlarmSoundLinuxCompatible(File file){
        try{
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(file.getPath()));
            DataLine.Info info = new DataLine.Info(Clip.class, inputStream.getFormat());
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(inputStream);
            clip.start();
        }
        catch (Exception e) {
            System.err.println("There was an Exception thrown: " + e.getMessage());
            System.err.println("caused by:\n" + e.getStackTrace()[0].toString());
        }
    }
}
