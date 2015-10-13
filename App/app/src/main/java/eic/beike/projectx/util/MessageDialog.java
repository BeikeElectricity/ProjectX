package eic.beike.projectx.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import eic.beike.projectx.R;

/**
 * @author Adam
 */
public class MessageDialog extends DialogFragment {

    private String message = "";
    private int messageId;
    private boolean useId = false;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessage(int stringId) {
        this.messageId = stringId;
        this.useId = true;
    }

    /**
     * Interface to facilitate talking back to the activity.
     */
    public interface MessageDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogDismiss(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    MessageDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setCancelable(false);

        if (useId) {
            builder.setMessage(messageId);
        } else {
            builder.setMessage(message);
        }
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    listener.onDialogPositiveClick(MessageDialog.this);
                }
            });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            // Instantiate the listener so we can send events to the host
            this.listener = (MessageDialogListener) activity;

        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        listener.onDialogDismiss(this);
    }
}
