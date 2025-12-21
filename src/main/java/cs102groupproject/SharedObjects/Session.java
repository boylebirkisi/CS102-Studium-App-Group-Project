package cs102groupproject.SharedObjects;

import java.time.LocalDateTime;

public class Session {
    private User owner;
    private String name;
    private String type;
    private int no;
    private String id;
    private int length;
    private int remainingSeconds;
    private int breakLength;
    private int totalSeconds;
    private LocalDateTime startDate;
    private boolean isPaused;
    private boolean inBreak;
    private boolean isCompleted;

    public Session() {
        this.owner = null;
        this.name = "";
        this.type = "";
        this.no = 0;
        this.id = "";
        this.length = 0;
        remainingSeconds = 0;
        totalSeconds = 0;
        this.breakLength = 0;
        isPaused = false;
        inBreak = false;
        isCompleted = false;
        this.startDate = LocalDateTime.now();
    }

    public Session(User owner,String name, String type, int no, String id, int length, int breakLength, LocalDateTime startDate)
    {
        this.owner = owner;
        this.name = name;
        this.type = type;
        this.no = no;
        this.id = id;
        this.length = length;
        remainingSeconds = length * no;
        totalSeconds = length * no;
        this.breakLength = breakLength;
        isPaused = false;
        inBreak = false;
        isCompleted = false;
        this.startDate = startDate;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getType() {return type;}
    public void setType(String type) {this.type = type;}
    public int getNo() {return no;}

    public void setNo(int no)
    {
        this.no = no;
        this.remainingSeconds = this.length * this.no;
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public int getLength() {return length;}

    public void setLength(int length)
    {
        this.length = length;
        this.remainingSeconds = this.length * this.no;
    }

    public int getRemainingSeconds() {return remainingSeconds;}
    public void decreaseRemainingSeconds() {remainingSeconds--;}
    public int getBreakLength() {return breakLength;}
    public void setBreakLength(int breakLength) {this.breakLength = breakLength;}
    public int getTotalSeconds() {return totalSeconds;}
    public boolean getIsPaused() {return isPaused;}
    public boolean getInBreak() {return inBreak;}
    public boolean getIsCompleted() {return isCompleted;}
    public void setIsCompletedTrue() {isCompleted = true;}
    public LocalDateTime getDate() {return startDate;}

    /**
     * Starts the session.
     */
    public void startSession()
    {isPaused = false;}

    /**
     * Pauses the session.
     */
    public void pauseSession()
    {isPaused = true;}

    /**
     * Sets isCompleted to true, ending the session.
     */
    public void endSession()
    {
        isCompleted = true;
        awardCurrencyToUser(owner);
    }

    /**
     * Updated the Session variables, should be called every second.
     */
    public void updateSession()
    {
        if (!isPaused && remainingSeconds > 0)
        {
            decreaseRemainingSeconds();
        }
        if (remainingSeconds % length == 0 && remainingSeconds != 0)
        {
            remainingSeconds = breakLength;
            setNo(no--);
            inBreak = true;
        }
        if (inBreak)
        {
            if (remainingSeconds == 0)
            {
                inBreak = false;
            }
            if (inBreak)
                {remainingSeconds--;}
            else
            {remainingSeconds = length * no;}
        }
        if (remainingSeconds == 0)
        {
            isCompleted = true;
            awardCurrencyToUser(owner);
        }
    }

    /**
     * Gives the user money based on session type and length completed.
     */
    public void awardCurrencyToUser(User user)
    {
        int completedLength;
        if (getInBreak())
            {completedLength = totalSeconds - (getLength() * getNo());}
        else
            {completedLength = totalSeconds - getRemainingSeconds();}
        int currencyEarned = completedLength / 5; // 1 currency for every 5 seconds completed
        user.addCurrency(currencyEarned, type.equals("Group"));
    }
}