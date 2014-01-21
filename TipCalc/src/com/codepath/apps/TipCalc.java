package com.codepath.apps;

import java.math.BigDecimal;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TipCalc extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tip_calc);
		setUpListners();
		TextView txTipAmount = (TextView)findViewById(R.id.txTipAmount);
		txTipAmount.setText("Tip is: $0.0");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tip_calc, menu);
		return true;
	}
	
	private void setUpListners() {
		Button btnTenPercent = (Button) findViewById(R.id.btnTenPercent);
		Button btnFifteenPercent = (Button) findViewById(R.id.btnFifteenPercent);
		Button btnTwentyPercent = (Button) findViewById(R.id.btnTwentyPercent);
		
		btnTenPercent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				calculateAndDisplayTipAmount(10);
			}
		});
		
		btnFifteenPercent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				calculateAndDisplayTipAmount(15);
			}
		});
		
		btnTwentyPercent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				calculateAndDisplayTipAmount(20);
			}
		});
	}
	
	private void calculateAndDisplayTipAmount(int tipPercentage){
		BigDecimal tipAmount = calculateTipAmount(tipPercentage);
		TextView txTipAmount = (TextView)findViewById(R.id.txTipAmount);
		txTipAmount.setText("Tip is: $"+tipAmount.doubleValue());
	}
	
	private BigDecimal calculateTipAmount(int tipPercentage) {
		BigDecimal tipAmount = BigDecimal.ZERO;
		EditText etTotalAmount = (EditText)findViewById(R.id.etTotalAmount);
		String str = etTotalAmount.getText().toString();
//		Toast.makeText(TipCalc.this, "Total="+str, Toast.LENGTH_SHORT).show();
		if(str != null && str.length()!=0){
			BigDecimal totalAmount = new BigDecimal(str);
			BigDecimal percentage = new BigDecimal(tipPercentage).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
			tipAmount = totalAmount.multiply(percentage).setScale(2, BigDecimal.ROUND_HALF_UP);
	//		Toast.makeText(TipCalc.this, "TipAmount "+tipAmount, Toast.LENGTH_SHORT).show();
		}
		return tipAmount;
	}

}
