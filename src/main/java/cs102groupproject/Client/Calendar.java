package cs102groupproject.Client;

import java.time.LocalDate;

import cs102groupproject.SharedObjects.DayCell;

public class Calendar {
	private int currentYear;
	private int currentMonth;
	private DayCell[] days;
	private String userId;

    public Calendar(String userId) {
        currentYear = LocalDate.now().getYear();
        currentMonth = LocalDate.now().getMonthValue();
        this.userId = userId;
        int monthLength = 30; // in case monthLength is not set
        for (int i = 1; i < 13; i++) {
            if (i == currentMonth) {
                monthLength = LocalDate.of(currentYear, currentMonth, 1).lengthOfMonth();
                break;
            }
        }
        days = new DayCell[monthLength];
        for (int i = 0; i < days.length; i++) {
            LocalDate date = LocalDate.of(currentYear, currentMonth, i + 1);
            days[i] = new DayCell(date);
        }
    }

    public DayCell[] getDays() {return days;}
    public DayCell getDay(int day) {return days[day - 1];}

    public void setDay(DayCell dayCell) 
    {
        int day = dayCell.getDate().getDayOfMonth();
        days[day - 1] = dayCell;
    }

    public int getCurrentYear() {return currentYear;}
    public void setCurrentYear(int year) {this.currentYear = year;}
    public int getCurrentMonth() {return currentMonth;}
    public void setCurrentMonth(int month) {this.currentMonth = month;}
    public String getUserId() {return userId;}
}
