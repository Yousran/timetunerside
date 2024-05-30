package timetuner;

import timetuner.models.Budget;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.text.NumberFormat;
import java.util.Locale;

public class SelfUtils {
    
    public static int calculateBudget(int totalBudget, List<Budget> budgets) {
        int totalSpent = 0;
        for (Budget budget : budgets) {
            totalSpent += budget.getPrice();
        }
        return totalBudget - totalSpent;
    }

    public static String calculateTimeLeft(String dueDate) {
        try {
            LocalDate endDate = LocalDate.parse(dueDate, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate today = LocalDate.now();
            long daysBetween = ChronoUnit.DAYS.between(today, endDate);
            return daysBetween + " days";
        } catch (Exception e) {
            return "Invalid date";
        }
    }

    public static String intToRupiah(int number) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("id", "ID"));
        
        String formattedNumber = formatter.format(number);
        
        return "Rp. " + formattedNumber;
    }
}
