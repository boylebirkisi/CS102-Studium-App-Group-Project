package cs102groupproject.Server;

import java.util.Properties;
import java.util.Random;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import cs102groupproject.SharedObjects.VerificationCode;
import io.github.cdimascio.dotenv.Dotenv;
/**
 * EmailService handles the email operations.
 * Author: Delfin Eryılmaz
 * Date: 27/12/2025
 */
public class EmailService {
    // 10 minute expiry duration
    private final static int EXPIRY_DURATION = 10 * 60 * 1000; 
    // Connection to DB Manager
    private static DBManager db = new DBManager();
    // Email Credentials
    private static Dotenv dotenv = Dotenv.load();
    private static final String SENDER_EMAIL = dotenv.get("SENDER_EMAIL");
    private static final String APP_PASSWORD = dotenv.get("APP_PASSWORD"); 
    
    /**
     * Generates a random 6-digit verification code.
     * @param length
     * @return
     */
    public static String generateRandomCode(int length) {
        Random random = new Random();
        int min = (int) Math.pow(10, length - 1); 
        int max = (int) Math.pow(10, length) - 1;  
        int code = random.nextInt((max - min) + 1) + min;
        return String.valueOf(code);
    }

    /**
     * Core method to set up and send the email.
     * @param recipient
     * @param subject
     * @param body
     * @return
     */
    public static boolean sendMail(String recipient, String subject, String body) {

        // Set up connection properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        // Enable TLS/STARTTLS security
        props.put("mail.smtp.starttls.enable", "true"); 
        // SMTP server for Gmail
        props.put("mail.smtp.host", "smtp.gmail.com"); 
        // Port for TLS/STARTTLS
        props.put("mail.smtp.port", "587");           

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // Provides the username and password to the server
                return new PasswordAuthentication(SENDER_EMAIL, APP_PASSWORD);
            }
        });

        try {
            // Creating the email content (MimeMessage) 
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(
                Message.RecipientType.TO,
                // Converting recipient string to address object
                InternetAddress.parse(recipient) 
            );
            message.setSubject(subject);
            message.setText(body);

            // Send the message via Transport
            Transport.send(message);

            System.out.println("Email sent successfully to " + recipient + "!");
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Email failed to send: " + e.getMessage());
        }
    }

    /**
     * Calculates the expiry time for the verificationCode.
     * @return
     */
    public static long calculateExpiryTime() {
        return System.currentTimeMillis() + EXPIRY_DURATION;
    }

    /**
     * Creates and stores the verification code. If a verification code exists its replace it.
     * @param email
     * @return the verification code created.
     */
    public VerificationCode createAndStoreVerificationCode(String email) {
        VerificationCode code = new VerificationCode(email, generateRandomCode(6), calculateExpiryTime());
        boolean isInserted = db.insertVerificationCode(email, code.getStoredCode(), code.getExpiryTime());
        if (!isInserted) {
            throw new RuntimeException("Failed to store verification code in the database.");
        }
        return code;
    }
}