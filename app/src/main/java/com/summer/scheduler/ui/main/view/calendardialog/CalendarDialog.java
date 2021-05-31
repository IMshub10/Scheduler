package com.summer.scheduler.ui.main.view.calendardialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.summer.scheduler.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarDialog extends AppCompatDialogFragment {

    String dateString = "";
    int weekNo;
    private CalendarDialogBoxListener listener;
    @NonNull
    @Override


    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater  = getActivity().getLayoutInflater();
        View view =inflater.inflate(R.layout.component_scheduler_calendar,null);

        CalendarView calendarView = view.findViewById(R.id.calendarDialog);
        calendarView.setFirstDayOfWeek(Calendar.SUNDAY);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                Calendar cal = Calendar.getInstance();
                cal.set(i,i1,i2);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                dateString = sdf.format(cal.getTime());
                weekNo = cal.get(Calendar.WEEK_OF_YEAR);
                listener.sendDateInfo(dateString,weekNo,cal.getTime());

                dismiss();

            }
        });

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            listener = (CalendarDialogBoxListener) getTargetFragment();
        }
        catch(ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement CalendarDialogBoxListener");
        }
    }
}
