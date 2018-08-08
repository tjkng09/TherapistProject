import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Date;

public class TherapistClient implements ActionListener
{
private String      serverAddress;
private int         serverPort; 
private String      newLine           = System.getProperty("line.separator");
private JFrame      window            = new JFrame("Therapist Client");
private JTextArea   textArea          = new JTextArea(10, 40);
private JScrollPane scrollPane        = new JScrollPane(textArea);
private JTextField  questionTextField = new JTextField(24); 
private JTextField  errorTextField    = new JTextField(48);
private FileWriter     fw = new FileWriter("TherapistLog.txt", true); 
private BufferedWriter bw = new BufferedWriter(fw);

public static void main(String[] args) throws IOException // load our program!
  {
  if (((args.length == 1) && args[0].equals("?"))
    || (args.length > 2))
	 {
	 System.out.println("Command line parameters may be provided as follows:");
	 System.out.println("#1 (optional) may specify server address.");
	 System.out.println("#2 (optional) may specify server port number.");
	 System.out.println("Defaults are 'localhost' and port 1234");
	 return;
	 }
  if (args.length == 2) // 2 parms provided
	  new TherapistClient(args[0],Integer.parseInt(args[1]));
  if (args.length == 1) // 1 parm provided
	  new TherapistClient(args[0]);
  if (args.length == 0) // no parm provided
	  new TherapistClient();
  }

public TherapistClient() throws IOException 
  {
  this("localhost", 1234);	
  }

public TherapistClient(String hostAddress) throws IOException 
  {
  this(hostAddress, 1234);	
  }

public TherapistClient(String hostAddress, int hostPort) throws IOException // the "constructor" method
  {
  serverAddress = hostAddress;
  serverPort    = hostPort;
  textArea.setText("Connecting to TherapistServer at "
		         + serverAddress 
		         + " on port "
		         + serverPort);

  // Build the GUI screen
  window.getContentPane().add(questionTextField, "North");
  window.getContentPane().add(scrollPane, "Center");
  window.getContentPane().add(errorTextField,"South");

  window.setSize(1000,300); // across,down
  window.setVisible(true);
  window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  textArea.append(newLine + "Welcome to the On-Line Therapy System!");
  textArea.append(newLine + "Close this window at any time to terminate your session.");
  textArea.append(newLine + "Enter a question above to be answered by the therapist.");
  textArea.append(newLine + "(For brevity, please phrase your question such that it can be ");  textArea.append(newLine + "answered with a YES or NO reply.e.g. 'Will I feel better soon?')" + newLine);
  
  textArea.setEditable(false);        // keep cursor out
  errorTextField.setEditable(false);  // keep cursor out
  textArea.setFont(new Font("font type", Font.BOLD, 15));
  questionTextField.setFont(new Font("font type", Font.BOLD, 15));
  errorTextField.setFont(new Font("font type", Font.BOLD, 15));

  bw.write(newLine + "This session started " + new Date());
  bw.close();

  questionTextField.addActionListener(this);
  }

public void actionPerformed(ActionEvent ae) // GUI objects call here!
  {
  errorTextField.setBackground(Color.white); // remove highlight
  errorTextField.setText(""); // clear
  String question = questionTextField.getText().trim();
  if (question.length() == 0)
     {
     errorTextField.setText("A question must be entered to call the therapist.");
     errorTextField.setBackground(Color.yellow); // highlight to notice.
     return; // exit early to ignore blank input
     }
  questionTextField.setEnabled(false); // disable text field for events
  // Connect to the server
  try {
      Socket s = new Socket(serverAddress, serverPort);//TCP/IP,port
      DataInputStream  dis = new DataInputStream(s.getInputStream()); 
      DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
      dos.writeUTF(question); 
      String answer = dis.readUTF(); // wait for reply
      String logLine = "Your Question was: "          + question
                     + " Answer from therapist was: " + answer;
      textArea.append(newLine + logLine); 
      textArea.setCaretPosition(textArea.getDocument().getLength()); // scroll to bottom
  
      // need to re-open log file after previous close.
      bw = new BufferedWriter(new FileWriter("TherapistLog.txt", true));
      bw.write(newLine + logLine);
      bw.close();
      
      questionTextField.setEnabled(true); // re-enable text field for events 
      }
  catch (IOException ioe)
      {
      String errorMessage = "Problem writing the log file. " + ioe;
      errorTextField.setText(errorMessage);
      errorTextField.setBackground(Color.yellow); // highlight to notice.
      System.out.println(errorMessage);
      }
  // get ready for next question
  questionTextField.setText(""); // clear
  questionTextField.requestFocus(); // set cursor in
  }  
}
