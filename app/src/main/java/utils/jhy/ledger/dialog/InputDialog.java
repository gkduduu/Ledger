package utils.jhy.ledger.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Map;

import utils.jhy.ledger.R;
import utils.jhy.ledger.data.Const;
import utils.jhy.ledger.data.FirestoreQuery;
import utils.jhy.ledger.data.MainData;

public class InputDialog extends Dialog {

    TextView DATE;
    EditText MONEY;
    EditText STORE;
    EditText COMMENT;
    EditText CARD;
    EditText USER;
    EditText CATEGORY;
    Button SEND;

    RelativeLayout IN;
    RelativeLayout OUT;
    View INLINE;
    View OUTLINE;

    MainData mMaindata;
    Activity activity;

    boolean isIN = false;


    public InputDialog(@NonNull Activity context) {
        super(context);
        activity = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_input);

        DATE = findViewById(R.id.INPUT_DATE);
        final Calendar ca = Calendar.getInstance();
        int month = (ca.get(Calendar.MONTH) + 1);
        String strMonth = 10 > month ? "0" + month : month + "";
        int day = (ca.get(Calendar.DATE));
        String strDay = 10 > day ? "0" + day : day + "";
        DATE.setText(ca.get(Calendar.YEAR) + "-" + strMonth + "-" + strDay);
        DATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(activity, listener, ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE));
                dialog.show();
            }
        });

        MONEY = findViewById(R.id.INPUT_MONEY);
        STORE = findViewById(R.id.INPUT_STORE);
        COMMENT = findViewById(R.id.INPUT_COMMENT);

        CARD = findViewById(R.id.INPUT_CARD);

        USER = findViewById(R.id.INPUT_USER);
        Log.i("jhy", Build.MODEL);
        if (Build.MODEL.contains("SM-N950N")) {
            USER.setText("하영");
        } else {
            USER.setText("소현");
        }
        CATEGORY = findViewById(R.id.INPUT_CATE);
        SEND = findViewById(R.id.INPUT_SEND);

        SEND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == MONEY.getText() || MONEY.getText().toString().equals("")) {
                    Toast.makeText(activity, "금액을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                SEND.setBackgroundResource(R.color.btnNonactive);
                findViewById(R.id.INPUT_PROGRESS).setVisibility(View.VISIBLE);

                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                mMaindata = new MainData();
                mMaindata.setDate(DATE.getText().toString());
                mMaindata.setMoney(MONEY.getText().toString());
                mMaindata.setStore(STORE.getText().toString());
                mMaindata.setComment(COMMENT.getText().toString());
                mMaindata.setCard(CARD.getText().toString());
                mMaindata.setUser(USER.getText().toString());
                mMaindata.setCategory(CATEGORY.getText().toString());
                if(isIN)
                    mMaindata.setState("IN");
                else
                    mMaindata.setState("OUT");

                FirestoreQuery.insertData(mMaindata, InputDialog.this);
            }
        });

        IN = findViewById(R.id.INPUT_IN);
        OUT = findViewById(R.id.INPUT_OUT);
        INLINE = findViewById(R.id.INPUT_INLINE);
        OUTLINE = findViewById(R.id.INPUT_OUTLINE);
        IN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isIN = true;
                IN.setBackgroundResource(R.color.btnActive);
                OUT.setBackgroundResource(R.color.btnNonactive);
                OUTLINE.setVisibility(View.VISIBLE);
                INLINE.setVisibility(View.GONE);
            }
        });
        OUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isIN = false;
                OUT.setBackgroundResource(R.color.btnActive);
                IN.setBackgroundResource(R.color.btnNonactive);
                INLINE.setVisibility(View.VISIBLE);
                OUTLINE.setVisibility(View.GONE);
            }
        });

        FirestoreQuery.getConstData(Const.FIRESTORE_CATEGORY,this);
    }
    //달력
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear = monthOfYear + 1;
            String month = monthOfYear + "";
            if (monthOfYear < 10) {
                month = "0" + month;
            }
            String day = dayOfMonth + "";
            if (dayOfMonth < 10) {
                day = "0" + day;
            }
            DATE.setText(year + "-" + month + "-" + day);
        }
    };

    public void setCategory(final CharSequence[] category) {
        CATEGORY.setText(category[0]);
        CATEGORY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(Html.fromHtml("<font color='#3388DD'><b>카테고리</b></font>"));
                builder.setItems(category, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CATEGORY.setText(category[which]);
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    public void dialogFinish(boolean isSuccess) {
        if(isSuccess) {
            Toast.makeText(activity, "입력 완료!", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(activity, "오류 발생...", Toast.LENGTH_SHORT).show();
        }
        dismiss();
    }
}
