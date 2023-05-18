package com.d3k4y.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.d3k4y.project2.Models.BuyModel;
import com.d3k4y.project2.Models.ProductModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class buy_product extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    TextView b_b_mail,b_s_mail,b_product_id,b_product_name,b_product_price,b_product_qty,b_total_price;
    EditText b_b_name,b_b_qty,b_d_address,b_cc_number,b_ccv_mm,b_ccv_yy,b_ccv_xxx;
    Button cash_on_delivery,pay_with_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_product);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        Bundle b=getIntent().getExtras();

        b_b_mail=findViewById(R.id.b_b_mail);
        b_s_mail=findViewById(R.id.b_s_mail);
        b_product_id=findViewById(R.id.b_product_id);
        b_product_name=findViewById(R.id.b_product_name);
        b_product_price=findViewById(R.id.b_product_price);
        b_product_qty=findViewById(R.id.b_product_qty);
        b_total_price=findViewById(R.id.b_total_price);

        b_b_mail.setText(user.getEmail());
        b_s_mail.setText(b.getString("v2_seller_mail"));
        b_product_id.setText(b.getString("v2_product_id"));
        b_product_name.setText(b.getString("v2_product_name"));
        b_product_price.setText(b.getString("v2_product_price"));
        b_product_qty.setText(b.getString("v2_product_quantity"));

        b_b_name=findViewById(R.id.b_b_name);
        b_b_qty=findViewById(R.id.b_b_qty);
        b_d_address=findViewById(R.id.b_d_address);
        b_cc_number=findViewById(R.id.b_cc_number);
        b_ccv_mm=findViewById(R.id.b_ccv_mm);
        b_ccv_yy=findViewById(R.id.b_ccv_yy);
        b_ccv_xxx=findViewById(R.id.b_ccv_xxx);

        cash_on_delivery=findViewById(R.id.cash_on_delivery);
        pay_with_card=findViewById(R.id.pay_with_card);

        pay_with_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{

                    db.collection("ProductData").document(b_product_id.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String buyer_mail=b_b_mail.getText().toString();
                            String product_id=b_product_id.getText().toString();
                            String billing_name=b_b_name.getText().toString();
                            String buy_qty=b_b_qty.getText().toString();
                            String total_price=b_total_price.getText().toString();
                            String billing_address=b_d_address.getText().toString();
                            String card_number=b_cc_number.getText().toString();
                            String card_exp_month=b_ccv_mm.getText().toString();
                            String card_exp_year=b_ccv_yy.getText().toString();
                            String card_ccv=b_ccv_xxx.getText().toString();

                            Double d_qty=Double.parseDouble(buy_qty);
                            String available=b_product_qty.getText().toString();
                            Double a_qty=Double.parseDouble(available);

                            ProductModel pm=documentSnapshot.toObject(ProductModel.class);
                            Double r_qty=a_qty-d_qty;
                            pm.setQty(String.valueOf(r_qty));
                            db.collection("ProductData").document(product_id).set(pm).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    BuyModel bcm=new BuyModel(buyer_mail,product_id,billing_name,buy_qty,total_price,billing_address,String.valueOf(new Date()),card_number,card_exp_month,card_exp_year,card_ccv);
                                    db.collection("ProductBuyDetails").add(bcm).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(),
                                                    "Bought",
                                                    Toast.LENGTH_SHORT)
                                                    .show();
                                            finish();
                                        }
                                    });
                                }
                            });
                        }
                    });

                }catch (Exception e){

                    Toast.makeText(getApplicationContext(),
                            e.getMessage().toString(),
                            Toast.LENGTH_SHORT)
                            .show();

                }

            }
        });

        cash_on_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{

                    db.collection("ProductData").document(b_product_id.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String buyer_mail=b_b_mail.getText().toString();
                            String seller_mail=b_s_mail.getText().toString();
                            String product_id=b_product_id.getText().toString();
                            String billing_name=b_b_name.getText().toString();
                            String buy_qty=b_b_qty.getText().toString();
                            String total_price=b_total_price.getText().toString();
                            String billing_address=b_d_address.getText().toString();

                            Double d_qty=Double.parseDouble(buy_qty);
                            String available=b_product_qty.getText().toString();
                            Double a_qty=Double.parseDouble(available);

                            ProductModel pm=documentSnapshot.toObject(ProductModel.class);
                            Double r_qty=a_qty-d_qty;
                            pm.setQty(String.valueOf(r_qty));
                            db.collection("ProductData").document(product_id).set(pm).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    BuyModel bcm=new BuyModel(buyer_mail,seller_mail,product_id,billing_name,buy_qty,total_price,billing_address,String.valueOf(new Date()));
                                    db.collection("ProductBuyDetails").add(bcm).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(),
                                                    "Bought",
                                                    Toast.LENGTH_SHORT)
                                                    .show();
                                            finish();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),
                                            e.getMessage().toString(),
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });

                        }
                    });

                }catch (Exception e){

                    Toast.makeText(getApplicationContext(),
                            e.getMessage().toString(),
                            Toast.LENGTH_SHORT)
                            .show();

                }

            }
        });

        b_b_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try{
                        String qty=b_b_qty.getText().toString();
                        Double d_qty=Double.parseDouble(qty);
                        Double d_price=Double.parseDouble(b_product_price.getText().toString());
                        Double total=d_price*d_qty;
                        b_total_price.setText(String.valueOf(total));

                        String available=b_product_qty.getText().toString();
                        Double a_qty=Double.parseDouble(available);

                        if(d_qty>a_qty){
                            Toast.makeText(getApplicationContext(),
                                    "Not available in that amount",
                                    Toast.LENGTH_SHORT)
                                    .show();
                            cash_on_delivery.setClickable(false);
                            pay_with_card.setClickable(false);
                            b_b_qty.setBackgroundResource(R.drawable.edittext_error);
                            b_b_qty.setPadding(10,10,10,10);
                        }else{
                            b_b_qty.setBackgroundResource(R.drawable.edittext_1);
                            b_b_qty.setPadding(10,10,10,10);
                            cash_on_delivery.setClickable(true);
                            pay_with_card.setClickable(true);
                        }

                }catch (Exception e){
                    Log.e("error",e.getMessage().toString());
                    b_total_price.setText("0.00");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}