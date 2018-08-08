import java.io.*;
import java.net.*;

public class TherapistServer
{
public static void main(String[] args) throws IOException
  {
  int serverPort;
  if (args.length == 0)
	  serverPort = 1234;
   else
	  serverPort = Integer.parseInt(args[0]);
  
  String[] reply = { 
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
  ServerSocket ss = new ServerSocket(serverPort);
  System.out.println("TherapistServer is up at "
                    + InetAddress.getLocalHost()
                    +" on port " + ss.getLocalPort());
  while(true)
     {
     try {
         Socket s = ss.accept(); // wait for a client connection
         DataInputStream  dis = new DataInputStream (s.getInputStream());
         DataOutputStream dos = new DataOutputStream(s.getOutputStream());
         String question = dis.readUTF(); // read one question from client
         System.out.println("A patient asks: " + question); // debug trace
         String answer = reply[(int)(Math.random() * reply.length)];
         dos.writeUTF(answer);
         System.out.println("The answer is: " + answer); // debug trace
         dos.close(); // flush
         }  
     catch (IOException e)
         {
         System.out.println(e); 
         }
     }
  }
}
