package com.summer.scheduler.ui.main.view.calendardialog;

import java.util.Date;

public interface CalendarDialogBoxListener {
    void sendDateInfo(String dateString, int weekNo, Date date);
}
