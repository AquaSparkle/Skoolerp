package erp.skoolerp.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import erp.skoolerp.R;
import erp.skoolerp.Wschool;
import erp.skoolerp.utils.TraknpayResponseActivity;

/**
 * Created by shalu on 28/10/17.
 */

public class PayuMoneyRequest extends Activity {

    String amount, orderId;
    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payumoney_request);

        if(getIntent()!=null){
            amount = getIntent().getStringExtra("amount");
            orderId = getIntent().getStringExtra("order_id");

            PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

            double value = 0;
            try {
                value = Double.parseDouble(amount);

            } catch (Exception e) {
                e.printStackTrace();
            }

            String name = "Test_name";
            if(!Wschool.name.isEmpty() && !Wschool.name.equals(" ")){
                name = Wschool.name;
            }
            String email = "test@example.com";
            if(!Wschool.email.isEmpty() && !Wschool.email.equals(" ")){
                email = Wschool.email;
            }
            String phone = "9900123456";
            if(!Wschool.phone.isEmpty() && !Wschool.phone.equals(" ")){
                phone = Wschool.phone;
            }

            String productName = "Fees";

            builder.setAmount(value)
                    .setTxnId(orderId)
                    .setPhone(phone)
                    .setProductName(productName)
                    .setFirstName(name)
                    .setEmail(email)
                    .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")
                    .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")
                    .setUdf1("")
                    .setUdf2("")
                    .setUdf3("")
                    .setUdf4("")
                    .setUdf5("")
                    .setUdf6("")
                    .setUdf7("")
                    .setUdf8("")
                    .setUdf9("")
                    .setUdf10("")
                    .setIsDebug(false)
                    .setKey("bcp3gmcZ")
                    .setMerchantId("5403648");

            try {
                mPaymentParams = builder.build();

                mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,PayuMoneyRequest.this, R.style.AppTheme_default, true);

            } catch (Exception e) {
                // some exception occurred
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private PayUmoneySdkInitializer.PaymentParam calculateServerSideHashAndInitiatePayment1(final PayUmoneySdkInitializer.PaymentParam paymentParam) {

        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = paymentParam.getParams();
        stringBuilder.append(params.get(PayUmoneyConstants.KEY) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.TXNID) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF1) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF2) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF3) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF4) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF5) + "||||||");
        stringBuilder.append("V1Y06EoERJ");

        String hash = hashCal(stringBuilder.toString());
        paymentParam.setMerchantHash(hash);

        return paymentParam;
    }

    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                final String payuResponse = transactionResponse.getPayuResponse();
                JSONObject jsonResponse = null;
                String mihPayuId = null;
                try {
                    jsonResponse = new JSONObject(payuResponse);
                    mihPayuId = jsonResponse.getJSONObject("result").getString("mihpayid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //Success Transaction
                    saveResponse(payuResponse, "payumoneysuccess", mihPayuId, "Payment Success");
                } else {
                    //Failure Transaction
                    saveResponse(payuResponse, "payumoneyfailure", mihPayuId, "Payment Failure");
                }

            } else if (resultModel != null && resultModel.getError() != null) {
                Log.d("ERROR !@####$$$%%%%", "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d("NULLL !@####$$$%%%%", "Both objects are null!");
            }
        }
    }

    private void saveResponse(final String payuResponse, final String url, final String mihPayuId, final String message) {

        final Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        if (Wschool.sharedPreferences.getString("login", "0").equals("guardian")) {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }

        params.put("sendarray", payuResponse);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Wschool.base_URL + url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject jo = new JSONObject(response);
                            System.out.println("Volley_Resp response saving__ "+response );
                            String status = jo.getString("sts");
                            if (status.equals("1"))
                            {
                                Intent intent = new Intent(getApplicationContext(), TraknpayResponseActivity.class);
                                intent.putExtra("transactionId", mihPayuId);
                                intent.putExtra("responseMessage", message);
                                Toast.makeText(PayuMoneyRequest.this, "Transaction response saved.", Toast.LENGTH_LONG).show();
                                startActivity(intent);
                                finish();

                            } else{

                                Toast.makeText(PayuMoneyRequest.this, "Transaction response not saved.", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(PayuMoneyRequest.this);
        requestQueue.add(stringRequest);
    }
}
