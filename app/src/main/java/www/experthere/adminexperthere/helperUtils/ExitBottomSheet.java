package www.experthere.adminexperthere.helperUtils;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import www.experthere.adminexperthere.R;


public class ExitBottomSheet extends BottomSheetDialogFragment {


    boolean fullAppClose;


    public ExitBottomSheet(boolean closeActivity) {
        this.fullAppClose = closeActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_back, container, false);

        Button noBtn = view.findViewById(R.id.noBtn);
        Button yesBtn = view.findViewById(R.id.yesBtn);

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        });


        return view;
    }


    private void exit() {




        if (fullAppClose){

            requireActivity().finish();

        }



    }

    private void dismissDialog() {
        dismiss();
    }



    }
