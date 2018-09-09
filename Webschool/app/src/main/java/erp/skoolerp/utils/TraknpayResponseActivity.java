package erp.skoolerp.utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import erp.skoolerp.R;
import erp.skoolerp.Wschool;

public class TraknpayResponseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traknpay_response);

        Bundle extras = getIntent().getExtras();
        String transactionId = "";
        String responseMessage = "";
        if (extras != null) {
            transactionId = extras.getString("transactionId");
            responseMessage = extras.getString("responseMessage");
        }

        TextView responseMessageView = (TextView) findViewById(R.id.responseMessageView);
        Button backButton = (Button) findViewById(R.id.backButton);

        backButton.setTypeface(Wschool.tf3);
        responseMessageView.setText(responseMessage);
        TextView transactionIdView = (TextView) findViewById(R.id.transactionIdView);
        transactionIdView.setText("Transaction ID : " + transactionId);
    }


    /**
     * Called when the user clicks the Make Another Payment Button
     */
    public void onBackButtonClicked(View view) {
        Wschool.from_pay = true;
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Wschool.from_pay = true;
        finish();
    }

    /**
     * Called when the user clicks the android Back Button
     */
}
