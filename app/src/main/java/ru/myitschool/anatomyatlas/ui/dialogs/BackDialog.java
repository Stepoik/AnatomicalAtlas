package ru.myitschool.anatomyatlas.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class BackDialog extends DialogFragment {
    private DialogListener listener;
    public BackDialog(DialogListener listener){
        this.listener = listener;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Вы уверены, что хотите выйти?");
        builder.setPositiveButton("Выйти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null){
                    listener.onDialogPositiveClick();
                }
            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null){
                    listener.onDialogNegativeClick();
                }
            }
        });
        return builder.create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        if (listener != null){
            listener.onDialogDismiss();
        }
        super.onDismiss(dialog);
    }

    public interface DialogListener{
        void onDialogPositiveClick();
        void onDialogNegativeClick();
        void onDialogDismiss();
    }
}
