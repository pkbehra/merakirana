package com.example.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.Interface.SelectPaymentListener;
import com.example.merakirana.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SelectPaymentModeBottomSheet extends BottomSheetDialogFragment {

    View view;
    RadioGroup rgPayment;
    RadioButton rbCard,rbCash;
    TextView btnContinue;
    SelectPaymentListener selectPaymentListener;

    String paymentMode="";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.select_payment_bottomsheet, container, false);
        init();
        return view;
    }

    private void init() {
        selectPaymentListener= (SelectPaymentListener) getActivity();

        rgPayment=view.findViewById(R.id.rgPayment);
        rbCard=view.findViewById(R.id.rbCard);
        rbCash=view.findViewById(R.id.rbCash);
        btnContinue=view.findViewById(R.id.btnContinue);
        btnContinue.setClickable(false);

        rgPayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                btnContinue.setBackgroundColor(getActivity().getResources().getColor(R.color.green));
                btnContinue.setClickable(true);

                switch (checkedId){
                    case R.id.rbCard:
                        paymentMode="CARD";
                        break;

                    case R.id.rbCash:
                        paymentMode="CASH";
                        break;
                }
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPaymentListener.selectPaymentMethod(paymentMode);
            }
        });
    }
}
