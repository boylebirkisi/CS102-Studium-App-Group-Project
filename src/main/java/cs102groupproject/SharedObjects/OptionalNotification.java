package cs102groupproject.SharedObjects;

public class OptionalNotification extends Notification{
    private String option1;
    private String option2;	
    private String chosenOption;

    public OptionalNotification(String notificationText, String heading, String id, String png, String option1, String option2)
    {
        super(notificationText, heading, id, png);
        this.option1 = option1;
        this.option2 = option2;
        chosenOption = "";
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
