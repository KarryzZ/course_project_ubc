package ui;

import model.Stufflist;
import network.ReadWebPage;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;
import ui.exception.NoSuchStuffexception;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Gui extends JFrame implements ActionListener {
    private JTextField idfield;
    private JPasswordField passwordfield;

    private ReadWebPage weatherinfo;
    private Stufflist stufflist;

    public Gui() throws IOException {
        super("Grades system");

        stufflist = new Stufflist();
        stufflist.load(Filemanager.stufffile());


        //Set up login GUI
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 120));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        setLayout(new FlowLayout());





        JButton login = new JButton("Stuff Login");
        JButton guest = new JButton("Guest");

        login.setActionCommand("mylogin");
        guest.setActionCommand("myguest");
        login.addActionListener(this);
        guest.addActionListener(this);

        JLabel idlabel = new JLabel("ID:");
        idfield = new JTextField(10);

        //set up getweather on GUI
        JButton getweather = new JButton("Get Weather");
        getweather.setActionCommand("myweather");
        getweather.addActionListener(this);


        JLabel passwordlabel = new JLabel("Password:");
        passwordfield = new JPasswordField(10);


        add(idlabel);
        add(idfield);
        add(passwordlabel);
        add(passwordfield);

        weatherinfo = new ReadWebPage();


        add(login);
        add(guest);

        add(getweather);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        music();



    }

    //MODEFIES: ActionEvent e
    //EFFECTS:this is the method that runs when Swing registers an action on an element
    //for which this class is an ActionListener
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("mylogin")) {
            playSound("./data/Mouse_click.wav");
            myloginopearation();
        } else if (e.getActionCommand().equals("myguest")) {
            playSound("./data/Mouse_click.wav");
            try {
                new Guestoperation();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            playSound("./data/Mouse_click.wav");
            JOptionPane.showMessageDialog(null,weatherinfo.printdata(),
                    "Weather",JOptionPane.INFORMATION_MESSAGE,null);
        }
    }

    private void myloginopearation() {
        try {
            if (idfield.getText().equals("") || passwordfield.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "The id or password can not be empty！",
                        "Warnings",JOptionPane.ERROR_MESSAGE,null);
            }
            login(Integer.parseInt(idfield.getText()),passwordfield.getText());
        } catch (IOException | NoSuchStuffexception ex) {
            JOptionPane.showMessageDialog(null, "The ID or Password are not correct",
                    "Warnings",JOptionPane.ERROR_MESSAGE,null);
        }
    }

    private void login(int id, String password) throws IOException, NumberFormatException, NoSuchStuffexception {
//        System.out.println("please enter the ID and password(saparate with a space):");
//        String coursewithname = scanner.nextLine().toLowerCase();
//        String[] input = coursewithname.split(" ");
        if (stufflist.isCorrect(id, password)) {
            new Stuffoperation();
        } else {
            throw new NoSuchStuffexception();
        }
    }


    //Play the BGM music source: Stackoverflow.
    private static void music() {
        AudioPlayer mgp = AudioPlayer.player;
        AudioStream bgm;
//        AudioData md;

        ContinuousAudioDataStream loop = null;

        try {
            InputStream bgmusic = new FileInputStream("./data/ChillingMusic.wav");
            bgm = new AudioStream(bgmusic);
            AudioPlayer.player.start(bgm);
//            md = bgm.getData();
//            loop = new ContinuousAudioDataStream(md);

        } catch (IOException e) {
            System.out.print(e.toString());
        }
        mgp.start(loop);
    }

    //EFFECTS: Click with sounds  source: Stackoverflow.
    private void playSound(String soundName) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        new Gui();
    }
}