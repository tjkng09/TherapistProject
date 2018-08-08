import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.Date;

public class TherapistGUI implements ActionListener
{
private String[] reply = { 
                         "It's OK with me",
                         "Your guess is as good as mine",
                         "What would your Dad say about that?",
                         "It's unlikely",
                         "Probably sooner or later",
                         "That's for you to decide",
                         "Not today!",
                         "Absolutely!", 
                         "Ask your Mother",
                         "In your dreams",
                         "It's not looking good...",
                         "I don't think so.",
                         "Are you kidding?",
                         "Not in this lifetime!",
                         "That would be swell",
                         "Why not?"
                         };

private String      newLine           = System.getProperty("line.separator");
private JFrame      window            = new JFrame("Talk to the Therapist");
private JTextArea   textArea          = new JTextArea(10, 40);
private JScrollPane scrollPane        = new JScrollPane(textArea);
private JTextField  questionTextField = new JTextField(24); 
private JTextField  errorTextField    = new JTextField(48);
private FileWriter     fw = new FileWriter("TherapistLog.txt", true); 
private BufferedWriter bw = new BufferedWriter(fw);

public static void main(String[] args) throws IOException // load our program!
  {
  new TherapistGUI();
  }

public TherapistGUI() throws IOException // the "constructor" method
  {
  // Build the GUI screen
  window.getContentPane().add(questionTextField, "North");
  window.getContentPane().add(scrollPane, "Center");
  window.getContentPane().add(errorTextField,"South");

  window.setSize(1000,300); // across,down
  window.setVisible(true);
  window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  textArea.setText("Welcome to the On-Line Therapy System!");
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
  String answer = reply[(int)(Math.random() * reply.length)];
  String logLine = "Your Question was: "          + question
                 + " Answer from therapist was: " + answer;
  textArea.append(newLine + logLine); 
  textArea.setCaretPosition(textArea.getDocument().getLength()); // scroll to bottom
  try {
      // need to re-open log file after previous close.
      bw = new BufferedWriter(new FileWriter("TherapistLog.txt", true));
      bw.write(newLine + logLine);
      bw.close();
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
  questionTextField.setEnabled(true); // enable text field for events 
  }  
}
