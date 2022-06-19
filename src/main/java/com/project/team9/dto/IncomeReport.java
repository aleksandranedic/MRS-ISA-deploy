package com.project.team9.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class IncomeReport {
    List<String> dates;
    List<Integer> incomes;

    public IncomeReport() {
        dates = new ArrayList<>();
        incomes = new ArrayList<>();
    }

    public IncomeReport(ArrayList<String> dates, ArrayList<Integer> incomes) {
        this.dates = dates;
        this.incomes = incomes;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(ArrayList<String> dates) {
        this.dates = dates;
    }

    public List<Integer> getIncomes() {
        return incomes;
    }

    public void setIncomes(ArrayList<Integer> incomes) {
        this.incomes = incomes;
    }
    public void addIncome(int value){
        incomes.add(value);
    }
    public void addDate(String value){
        dates.add(value);
    }

    public IncomeReport sort(boolean twoDates){
        for (int i=0; i < dates.size()-1; i++){
            int min_idx = i;

            for (int j=i+1; j<dates.size(); j++){
                if (dateIsBefore(twoDates, dates.get(min_idx), dates.get(j))){
                    min_idx = j;
                }
            }

            String tempDate = dates.get(i);
            dates.set(i, dates.get(min_idx));
            dates.set(min_idx, tempDate);
            int tempIncome = incomes.get(i);
            incomes.set(i, incomes.get(min_idx));
            incomes.set(min_idx, tempIncome);
        }
        return this;
    }
    public boolean dateIsBefore(boolean twoDates, String dates1Str, String dates2Str){
        String date1Str;
        String date2Str;
        DateTimeFormatter formatter;
        if (twoDates){
            formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. ");
            date1Str = dates1Str.split("-")[0];
            date2Str = dates2Str.split("-")[0];
        } else{
            formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
            date1Str = dates1Str;
            date2Str = dates2Str;
        }
        LocalDateTime date1 = LocalDate.parse(date1Str, formatter).atStartOfDay();
        LocalDateTime date2 = LocalDate.parse(date2Str, formatter).atStartOfDay();
        return date2.isBefore(date1);
    }
}
