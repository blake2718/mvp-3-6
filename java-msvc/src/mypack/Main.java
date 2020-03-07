package src.mypack;

import java.util.Scanner;

import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.Connection;
import javax.jms.Session;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

public class Main {

    // Create variables for the connection to MQ
    private static final String HOST = "localhost"; // Host name or IP address
    private static final int PORT = 1414; // Listener port for your queue manager
    private static final String CHANNEL = "DEV.APP.SVRCONN"; // Channel name
    private static final String QMGR = "QM1"; // Queue manager name
    private static final String APP_USER = "app"; // User name that application uses to connect to MQ
    private static final String APP_PASSWORD = "passw0rd"; // Password that the application uses to connect to MQ
    private static final String QUEUE_NAME = "QR"; // Queue that the application uses to put and get messages to and from

    public static void main(String[] args){

        try{
            JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
            JmsConnectionFactory cf = ff.createConnectionFactory();

            cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, HOST);
            cf.setIntProperty(WMQConstants.WMQ_PORT, PORT);
            cf.setStringProperty(WMQConstants.WMQ_CHANNEL, CHANNEL);
            cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, QMGR);
            cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
            cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "Blakes Custom");
            cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
            cf.setStringProperty(WMQConstants.USERID, APP_USER);
            cf.setStringProperty(WMQConstants.PASSWORD, APP_PASSWORD);

            // creating jms objects
            Connection connection = cf.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("queue:///" + QUEUE_NAME);
            MessageProducer producer = session.createProducer(destination);

            // create consumers
            Destination desRed = session.createQueue("queue:///COLOR.RED");
            Destination desBlue = session.createQueue("queue:///COLOR.BLUE");
            MessageConsumer conRed = session.createConsumer(desRed);
            MessageConsumer conBlue = session.createConsumer(desBlue);

            // initialize interfacing stuff
            Scanner myScanner = new Scanner(System.in);
            System.out.println("Type s to send a message, r to receive a message, or e to end the program");
            String line = myScanner.nextLine();
            String color;
            Message recMessage = null;
            String output = null;

            //start connection
            connection.start();

            while (!line.equals("e")){
                if (line.equals("s")){
                    // prompt
                    System.out.println("Type a message to send: ");
                    line = myScanner.nextLine();

                    // prompt
                    System.out.println("what color is this message?");
                    color = myScanner.nextLine();

                    // build
                    TextMessage message = session.createTextMessage(line);
                    message.setStringProperty("color", color);

                    // send
                    producer.send(message);
                } else if (line.equals("r")){

                    System.out.println("What color message would you like (red or blue)?");
                    color = myScanner.nextLine();
                    if (color.equals("red")){
                        recMessage = conRed.receive(2000);
                    } else if (color.equals("blue")) {
                        recMessage = conBlue.receive(2000);
                    } else {
                        System.out.println("Either red or blue");
                    }
                    if (recMessage != null){
                        output = recMessage.getBody(String.class);
//                        System.out.println(recMessage);
                        System.out.println(output);
                    } else {
                        System.out.println("There are no messages left in that queue");
                    }
                    System.out.println();
                } else {
                    System.out.println("That is not a valid option");
                }
                System.out.println();
                System.out.println("Type s to send a message, r to receive a message, or e to end the program");
                line = myScanner.nextLine();
            }

            System.out.println("Thank you for using MQ!");


        } catch (Exception e){
            System.out.println(e);
        }

    }


}
