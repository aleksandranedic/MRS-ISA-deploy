package com.project.team9.dto;

public class AttendanceReportParams {
    public String startDate;
    public String endDate;
    public String level;

    public AttendanceReportParams() {
    }

    public AttendanceReportParams(String startDate, String endDate, String level) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.level = level;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "AttendanceReportParams{" +
                "startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
