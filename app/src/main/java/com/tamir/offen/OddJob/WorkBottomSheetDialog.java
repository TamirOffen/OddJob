package com.tamir.offen.OddJob;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WorkBottomSheetDialog extends BottomSheetDialogFragment {

    private TextView workPreviewText, descPreviewText;
    private BottomSheetListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        workPreviewText = view.findViewById(R.id.workPreviewText);
        workPreviewText.setText(listener.getJobTitle());

        descPreviewText = view.findViewById(R.id.descTextBottomSheet);
        descPreviewText.setText(listener.getJobDesc());

        return view;
    }

    public interface BottomSheetListener {
        // returns the clicked pin's title
        String getJobTitle();
        String getJobDesc();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement BottomSheetListener");
        }
    }
}
