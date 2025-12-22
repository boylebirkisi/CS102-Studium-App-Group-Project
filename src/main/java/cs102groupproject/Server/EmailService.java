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

public class EmailService {
    // 10 minute expiry duration
    private final static int EXPIRY_DURATION = 10 * 60 * 1000; 
    // Connection to DB Manager
    private static DBManager db = new DBManager();
    // Email Credentials
    private static final String SENDER_EMAIL = "studiumm.app@gmail.com";
    private static final String APP_PASSWORD = "byzz xiqz zcxs pqrl"; 
    

    public static void main(String[] args) {
        String recipientEmail = "deryilmaz06@gmail.com";
        String verificationCode = generateRandomCode(6);
        String emailSubject = "Studium Account Verification Code";
        String emailBody = "Hello,\n\nYour Studium verification code is: " + verificationCode + "\n\nThis code will expire in 10 minutes.";

        System.out.println("Generated Code: " + verificationCode);
        sendMail(recipientEmail, emailSubject, emailBody);
    }

    /**
     * Generates a random 6-digit verification code.
     */
    private static String generateRandomCode(int length) {
        Random random = new Random();
        int min = (int) Math.pow(10, length - 1); 
        int max = (int) Math.pow(10, length) - 1;  
        int code = random.nextInt((max - min) + 1) + min;
        return String.valueOf(code);
    }

    /**
     * Core method to set up and send the email.
     */
    private static boolean sendMail(String recipient, String subject, String body) {

        // Set up connection properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        // Enable TLS/STARTTLS security
        props.put("mail.smtp.starttls.enable", "true"); 
        // SMTP server for Gmail
        props.put("mail.smtp.host", "smtp.gmail.com"); 
        // Port for TLS/STARTTLS
        props.put("mail.smtp.port", "587");           

        // 3. Create a mail session with an Authenticator 
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // Provides the username and password to the server
                return new PasswordAuthentication(SENDER_EMAIL, APP_PASSWORD);
            }
        });

        try {
            // Construct the email content (MimeMessage) 
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(
                Message.RecipientType.TO,
                // Convert recipient string to address object
                InternetAddress.parse(recipient) 
            );
            message.setSubject(subject);
            message.setText(body);

            // Send the message (Transport)
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
    private static long calculateExpiryTime() {
        return System.currentTimeMillis() + EXPIRY_DURATION;
    }

    public VerificationCode createAndStoreVerificationCode(String email) {
        VerificationCode code = new VerificationCode(email, generateRandomCode(6), calculateExpiryTime());
        int codeID = db.insertVerificationCode(email, code.getStoredCode());
        if (codeID == -1) {
            throw new RuntimeException("Failed to store verification code in the database.");
        }
        return code;
    }
}