package com.example.ki3.testapp1;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.example.ki3.testapp1.model.DialogListener;

public class TicketDialog extends Dialog implements View.OnClickListener{

    private Context context;
    private String itemnum, id;
    private DialogListener dialogListener;

    private TextView tvYes;
    private TextView tvCancel;

    public void setDialogListener(DialogListener dialogListener){
        this.dialogListener = dialogListener;
    }

    public TicketDialog(@NonNull Context context, String itemnum, String id) {
        super(context);
        this.context = context;
        this.itemnum = itemnum;
        this.id = id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_useticket);

        tvYes = (TextView) findViewById(R.id.TxV_ticketdialog_yes);
        tvCancel = (TextView) findViewById(R.id.TxV_ticketdialog_cancle);

        tvYes.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.TxV_ticketdialog_cancle:
                dismiss();
                break;
            case R.id.TxV_ticketdialog_yes:
                dialogListener.onPositiveClicked();
                dismiss();
                break;
        }

    }
}
