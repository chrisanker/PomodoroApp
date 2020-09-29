package PomodoraApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PomodoroTimer implements ActionListener {

    JFrame frame = new JFrame();
    JButton startButton = new JButton("Start");
    JButton pauseButton = new JButton("Pause");
    JButton resetButton = new JButton("Reset");
    JLabel timeLabel = new JLabel();
    JLabel statusLabel = new JLabel();
    int remainingTime = 1500000;
    int seconds = 0;
    int minutes = 25;
    boolean started = false;
    boolean onABreak = false;
    String secondsString = String.format("%02d",seconds);
    String minutesString = String.format("%02d",minutes);

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
                remainingTime = 1500000;
                statusLabel.setText("Work work!");
                timeLabel.setBackground(Color.red);
            }
            else{
                recess();
                System.out.println("Time is up");
            }

        }
    });

    PomodoroTimer(){

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
            timeLabel.setBackground(Color.red);
            start();
        }
        else if (e.getSource()==pauseButton){
            pause();
        }
        else if (e.getSource()==resetButton){
            timeLabel.setOpaque(false);
            timeLabel.repaint();
            reset();
        }
    }

    void start(){
        if(!started){
            started = true;
            statusLabel.setText("Work work!");
            timer.start();
            timeLabel.setOpaque(true);
            if(onABreak){
                timeLabel.setBackground(Color.green);
            }
        }
    }
    void pause() {
        started = false;
        timer.stop();
    }
    void reset() {
        started = false;
        timer.stop();
        remainingTime = 1500000;
        seconds = 0;
        minutes = 25;
        String secondsString = String.format("%02d",seconds);
        String minutesString = String.format("%02d",minutes);
        timeLabel.setText(minutesString + ":" + secondsString);
        statusLabel.setText("");
    }
    void recess(){
        onABreak = true;
        remainingTime = 300000;
        minutes = 5;
        seconds = 0;
        secondsString = String.format("%02d",seconds);
        minutesString = String.format("%02d",minutes);
        timeLabel.setText(minutesString + ":" + secondsString);
        statusLabel.setText("On a break.");
        timeLabel.setBackground(Color.green);
    }
}
