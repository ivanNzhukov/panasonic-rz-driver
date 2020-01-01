package projector;

import org.apache.commons.codec.digest.DigestUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static javax.swing.LayoutStyle.ComponentPlacement.UNRELATED;

public class Main extends JFrame {


    private final String constBytes = "00";
    private final String NTCONTROL = "NTCONTROL";
    private final String POWER_GET = "POWER_GET";
    private final String POWER_ON = "POWER_ON";
    private final String POWER_OFF = "POWER_OFF";
    private final String SHUTTER_GET = "SHUTTER_GET";
    private final String DEFAULT_LOGIN = "dispadmin";
    private final String DEFAULT_PORT = "default 1024";
    private final String SHUTTER_OPEN = "SHUTTER_OPEN";
    private final String DEFAULT_PASSWORD = "@Panasonic";
    private final String SHUTTER_CLOSE = "SHUTTER_CLOSE";
    private final String DEFAULT_ADDRESS = "default 192.168.0.8";
    private final int lengthForCheckSecure = (NTCONTROL + " ").length();
    private final int lengthForGetSecureValue = (NTCONTROL + " 1 ").length();
    private final int lengthForGetNotSecureValue = (NTCONTROL + " 0").length();

    private int port;
    private JPanel panel;
    private String login;
    private JLabel output;
    private String address;
    private char[] password;
    private boolean isSecure;
    private JTextField hostField;
    private JTextField portField;
    private JTextField loginField;
    private JPasswordField passField;

    private Map<String, Command> map = new HashMap<>();
    private static ExecutorService service = Executors.newFixedThreadPool(5);

    private Main() {

        initUI();
    }

    public static void main(String[] args) {

        service.execute(() -> {

            Main ex = new Main();
            ex.setVisible(true);
        });
        /*EventQueue.invokeLater(() -> {

            Main ex = new Main();
            ex.setVisible(true);
        });*/
    }

    private void initUI() {

        JLabel lbl1 = new JLabel("project address");
        JLabel lbl2 = new JLabel("port");
        JLabel lbl3 = new JLabel("login");
        JLabel lbl4 = new JLabel("password");

        hostField = new JTextField(15);
        hostField.setText(DEFAULT_ADDRESS);
        hostField.setToolTipText(DEFAULT_ADDRESS);

        portField = new JTextField(15);
        portField.setText(DEFAULT_PORT);
        portField.setToolTipText(DEFAULT_PORT);

        loginField = new JTextField(15);
        loginField.setText(DEFAULT_LOGIN);

        passField = new JPasswordField();
        passField.setText(DEFAULT_PASSWORD);

        JButton submitButton = new JButton("Connect to projector");
        submitButton.addActionListener(e -> {
            login = loginField.getText();
            try {
                password = passField.getPassword();
            } catch (Exception err) {
                password = null;
            }
            try {
                address = hostField.getText();
            } catch (NumberFormatException err) {
                showDialog("Please, write the correct address with Number or Letters. For localhost it would be 127.0.0.1 or localhost", err);
                return;
            }
            try {
                port = Integer.valueOf(portField.getText());
            } catch (NumberFormatException err) {
                showDialog("Please, write correct port with Number. Default port is 1024", err);
                return;
            }
            makeWindow();
        });


        createLayout(lbl1, hostField, lbl2, portField, lbl3, loginField, lbl4, passField, submitButton);
        hostField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (hostField.getText().equals(DEFAULT_ADDRESS)) {
                    hostField.setText("");
                }
            }
        });

        portField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (portField.getText().equals(DEFAULT_PORT)) {
                    portField.setText("");
                }
            }
        });
        portField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (portField.getText().equals(DEFAULT_PORT)) {
                    portField.setText("");
                }
            }
        });

        setTitle("Projector");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        createBufferStrategy(1);
        pack();
    }

    private void makeWindow() {
        closePanel();
        JPanel innerPanel = new JPanel();
        panel = new JPanel();
        output = new JLabel("output: ");

        map.put(SHUTTER_GET, new Command("SHUTTER", SHUTTER_GET, null, null, "QSH", null, null));
        map.put(SHUTTER_OPEN, new Command("SHUTTER", SHUTTER_OPEN, null, "OSH:0", null, "0", "OPEN"));
        map.put(SHUTTER_CLOSE, new Command("SHUTTER", SHUTTER_CLOSE, null, "QSH:1", null, "1", "CLOSE"));
        map.put(POWER_GET, new Command("POWER", POWER_GET, null, null, "QPW", null, null));
        map.put(POWER_ON, new Command("POWER", POWER_ON, null, "PON", null, "001", "ON"));
        map.put(POWER_OFF, new Command("POWER", POWER_OFF, null, "POF", null, "000", "OFF"));

        JButton shutterGet = new JButton(SHUTTER_GET);
        JButton shutterOpen = new JButton(SHUTTER_OPEN);
        JButton shutterClose = new JButton(SHUTTER_CLOSE);
        JButton powerGet = new JButton(POWER_GET);
        JButton powerOn = new JButton(POWER_ON);
        JButton powerOff = new JButton(POWER_OFF);

        shutterGet.setActionCommand(SHUTTER_GET);
        shutterOpen.setActionCommand(SHUTTER_OPEN);
        shutterClose.setActionCommand(SHUTTER_CLOSE);
        powerGet.setActionCommand(POWER_GET);
        powerOn.setActionCommand(POWER_ON);
        powerOff.setActionCommand(POWER_OFF);

        shutterGet.addActionListener(buttonListener());
        shutterOpen.addActionListener(buttonListener());
        shutterClose.addActionListener(buttonListener());
        powerGet.addActionListener(buttonListener());
        powerOn.addActionListener(buttonListener());
        powerOff.addActionListener(buttonListener());

        GridLayout innerLayout = new GridLayout(2, 3);

        innerPanel.setLayout(innerLayout);

        innerPanel.add(shutterGet);
        innerPanel.add(shutterOpen);
        innerPanel.add(shutterClose);
        innerPanel.add(powerGet);
        innerPanel.add(powerOn);
        innerPanel.add(powerOff);

        GroupLayout outerLayout = new GroupLayout(panel);
        outerLayout.setVerticalGroup(outerLayout.createSequentialGroup()
                .addGroup(outerLayout.createParallelGroup(GroupLayout.Alignment.LEADING))
                .addComponent(innerPanel).addGap(100)
                .addGroup(outerLayout.createParallelGroup(GroupLayout.Alignment.LEADING))
                .addComponent(output)
        );
        outerLayout.setHorizontalGroup(outerLayout.createParallelGroup()
                .addComponent(innerPanel)
                .addComponent(output));

        panel.setLayout(outerLayout);

        add(panel);
        setVisible(true);
        setSize(600, 600);
    }

    private ActionListener buttonListener() {
        return e -> {
            switch (e.getActionCommand()) {
                case SHUTTER_GET:
                    service.execute(() -> startSocket(address, port, map.get(SHUTTER_GET)));
                    break;
                case SHUTTER_OPEN:
                    service.execute(() -> startSocket(address, port, map.get(SHUTTER_OPEN)));
                    break;
                case SHUTTER_CLOSE:
                    service.execute(() -> startSocket(address, port, map.get(SHUTTER_CLOSE)));
                    break;
                case POWER_GET:
                    service.execute(() -> startSocket(address, port, map.get(POWER_GET)));
                    break;
                case POWER_ON:
                    service.execute(() -> startSocket(address, port, map.get(POWER_ON)));
                    break;
                case POWER_OFF:
                    service.execute(() -> startSocket(address, port, map.get(POWER_OFF)));
                    break;
            }
        };

    }

    private void closePanel() {
        panel.setVisible(false);
        dispose();
    }

    private void createLayout(Component... arg) {

        Container pane = getContentPane();
        panel = new JPanel();
        GroupLayout gl = new GroupLayout(panel);
        panel.setLayout(gl);
        panel.setVisible(true);
        pane.add(panel);

        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);

        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addGap(50)
                .addGroup(gl.createParallelGroup()
                        .addComponent(arg[0])
                        .addComponent(arg[1])
                        .addComponent(arg[2])
                        .addComponent(arg[3]).addGap(50)
                        .addComponent(arg[4])
                        .addComponent(arg[5])
                        .addComponent(arg[6])
                        .addComponent(arg[7])
                        .addComponent(arg[8]))
                .addGap(50)
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
                .addGap(50)
                .addGroup(gl.createSequentialGroup()
                        .addComponent(arg[0])
                        .addComponent(arg[1], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(arg[2])
                        .addComponent(arg[3], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(UNRELATED)
                        .addComponent(arg[4])
                        .addComponent(arg[5], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(arg[6])
                        .addComponent(arg[7], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(arg[8]))
                .addGap(50)
        );
    }

    private String md5Apache(String st) {
        return DigestUtils.md5Hex(st);
    }

    private void showDialog(String request, Exception e) {
        JOptionPane.showMessageDialog(this,
                request,
                e.getClass().getSimpleName(),
                JOptionPane.ERROR_MESSAGE);
    }

    private String getRandomValue(String line) {

        String random;
        isSecure = line.substring(lengthForCheckSecure).startsWith("1");

        if (isSecure) {
            random = line.substring(lengthForGetSecureValue);
        } else {
            random = line.substring(lengthForGetNotSecureValue);
        }
        return random;
    }

    private void startSocket(String hostName, int portNumber, Command command) {

        String input;

        try (
                Socket echoSocket = new Socket(hostName, portNumber);
                PrintWriter toServer = new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader fromServer = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        ) {
            input = fromServer.readLine();
            System.out.println(input);
            output.setText(input);

            String random = getRandomValue(input);

            String pass = new String(password);
            System.out.println("random = " + random);
            System.out.println("password = " + pass);

            String task;
            String hash = "";
            if (command.getCallback() == null) {
                task = command.getQuery();
            } else {
                task = command.getControl();
            }

            if (isSecure) {
                hash = md5Apache(login + ":" + pass + ":" + random);
                task = hash + constBytes + task;
            } else {
                task = constBytes + task;
            }

            System.out.println("Task = " + task);
            toServer.println(task);

            input = fromServer.readLine();
            String answer = input;
            for (Map.Entry<String, Command> com : map.entrySet()) {
                Command c = com.getValue();
                if (com.getKey().equals(command.getFunction()) && !c.getParametr().equals(command.getParametr())) {
                    if ((constBytes + c.getCallback()).equals(input)) {
                        answer = c.getHumanReadeable();
                        return;
                    } else if (c.getCallback() == null) {
                        answer = "Command sent";
                        return;
                    } else if (c.getCallback().substring(1, 3).equals("ERR")) {
                        for (Command.Errors value : Command.Errors.values()) {
                            if (c.getCallback().equals(value.name())) {
                                answer = value.getDescription();
                                return;
                            }
                        }
                    }
                }
            }
            System.out.println("Input = " + input);
            System.out.println("Answer = " + answer);

            output.setText(answer);

        } catch (UnknownHostException e) {
            showDialog("Don't know about host " + hostName, e);
            System.exit(1);
        } catch (IOException e) {
            showDialog("Couldn't get I/O for the connection to " + hostName, e);
            System.exit(1);
        }
    }
}
