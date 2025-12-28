package cs102groupproject.SharedObjects;
/**
 * Class representing a notification with optional user choices.
 * @author Ali Mersin
 */
public class OptionalNotification extends Notification{
    private String option1;
    private String option2;	
    private String chosenOption;

    public OptionalNotification(String notificationText, String heading, int id, String png, String option1, String option2, Notification.Type type)
    {
        super(notificationText, heading, id, png, type);
        this.option1 = option1;
        this.option2 = option2;
        chosenOption = "";
        setType(type);
    }

    public OptionalNotification(int userID, int referenceId, String notificationText, String heading, String png, String option1, String option2, Notification.Type type)
    {
        super(userID, referenceId, notificationText, heading, png, type);
        this.option1 = option1;
        this.option2 = option2;
        chosenOption = "";
        setType(type);
    }

    public String getOption1() {return option1;}
    public String getOption2() {return option2;}
    public String getChosenOption() {return chosenOption;}
    public void setChosenOption(String option) {chosenOption = option;}

    public void takeAction()
    {
        //TO-DO: implement action based on chosen option
    }
}
