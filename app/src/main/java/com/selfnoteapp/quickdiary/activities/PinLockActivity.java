package com.selfnoteapp.quickdiary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.selfnoteapp.quickdiary.R;
import com.selfnoteapp.quickdiary.utils.SecurityUtils;
import com.selfnoteapp.quickdiary.utils.UiUtils;

public class PinLockActivity extends AppCompatActivity {
    private SwitchMaterial enablePinSwitch;
    private android.view.View pinSetupLayout, verificationLayout, settingsLayout;
    private TextInputLayout pinInputLayout, confirmPinInputLayout;
    private TextInputEditText pinEditText, confirmPinEditText, pinEditTextSettings;
    private MaterialButton savePinButton;
    private View pinDot1, pinDot2, pinDot3, pinDot4;
    private TextView errorText;
    private boolean isVerifying = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_lock);
        
        enablePinSwitch = findViewById(R.id.enablePinSwitch);
        pinSetupLayout = findViewById(R.id.pinSetupLayout);
        verificationLayout = findViewById(R.id.verificationLayout);
        settingsLayout = findViewById(R.id.settingsLayout);
        pinInputLayout = findViewById(R.id.pinInputLayout);
        confirmPinInputLayout = findViewById(R.id.confirmPinInputLayout);
        pinEditText = findViewById(R.id.pinEditText);
        pinEditTextSettings = findViewById(R.id.pinEditTextSettings);
        confirmPinEditText = findViewById(R.id.confirmPinEditText);
        savePinButton = findViewById(R.id.savePinButton);
        pinDot1 = findViewById(R.id.pinDot1);
        pinDot2 = findViewById(R.id.pinDot2);
        pinDot3 = findViewById(R.id.pinDot3);
        pinDot4 = findViewById(R.id.pinDot4);
        errorText = findViewById(R.id.errorText);
        
        isVerifying = getIntent().getBooleanExtra("verify", false);
        
        if (isVerifying) {
            setupVerification();
        } else {
            setupSettings();
        }
    }
    
    private void setupVerification() {
        verificationLayout.setVisibility(View.VISIBLE);
        settingsLayout.setVisibility(View.GONE);
        
        // Make verification layout clickable to focus PIN input
        verificationLayout.setOnClickListener(v -> {
            if (pinEditText != null) {
                pinEditText.requestFocus();
            }
        });
        
        // Show keyboard automatically
        if (pinEditText != null) {
            pinEditText.post(() -> {
                pinEditText.requestFocus();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            });
            
            // Add text watcher to update PIN dots
            pinEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    updatePinDots(s.length());
                    errorText.setVisibility(View.GONE);
                }
                
                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 4) {
                        // Auto-verify when 4 digits are entered
                        verifyPin();
                    }
                }
            });
        }
    }
    
    private void updatePinDots(int length) {
        pinDot1.setBackgroundResource(length >= 1 ? R.drawable.pin_dot_filled : R.drawable.pin_dot_empty);
        pinDot2.setBackgroundResource(length >= 2 ? R.drawable.pin_dot_filled : R.drawable.pin_dot_empty);
        pinDot3.setBackgroundResource(length >= 3 ? R.drawable.pin_dot_filled : R.drawable.pin_dot_empty);
        pinDot4.setBackgroundResource(length >= 4 ? R.drawable.pin_dot_filled : R.drawable.pin_dot_empty);
    }
    
    private void verifyPin() {
        if (pinEditText != null) {
            String pin = pinEditText.getText().toString();
            String hash = UiUtils.getPinHash(this);
            if (hash != null && SecurityUtils.verifyPin(pin, hash)) {
                finish();
            } else {
                errorText.setText(R.string.pin_incorrect);
                errorText.setVisibility(View.VISIBLE);
                pinEditText.setText("");
                updatePinDots(0);
            }
        }
    }
    
    private void setupSettings() {
        verificationLayout.setVisibility(View.GONE);
        settingsLayout.setVisibility(View.VISIBLE);
        
        enablePinSwitch.setChecked(UiUtils.isPinEnabled(this));
        pinSetupLayout.setVisibility(enablePinSwitch.isChecked() ? View.VISIBLE : View.GONE);
        
        enablePinSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                pinSetupLayout.setVisibility(View.VISIBLE);
            } else {
                UiUtils.setPinEnabled(this, false);
                UiUtils.setPinHash(this, null);
                Toast.makeText(this, R.string.pin_disabled, Toast.LENGTH_SHORT).show();
                pinSetupLayout.setVisibility(View.GONE);
            }
        });
        
        savePinButton.setOnClickListener(v -> {
            if (pinEditTextSettings != null && confirmPinEditText != null) {
                String pin = pinEditTextSettings.getText().toString();
                String confirmPin = confirmPinEditText.getText().toString();
                
                if (pin.length() != 4) {
                    Toast.makeText(this, "PIN must be 4 digits", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                if (!pin.equals(confirmPin)) {
                    Toast.makeText(this, R.string.pin_mismatch, Toast.LENGTH_SHORT).show();
                    return;
                }
                
                String hash = SecurityUtils.hashPin(pin);
                if (hash != null) {
                    UiUtils.setPinHash(this, hash);
                    UiUtils.setPinEnabled(this, true);
                    Toast.makeText(this, R.string.pin_set, Toast.LENGTH_SHORT).show();
                    pinEditTextSettings.setText("");
                    confirmPinEditText.setText("");
                } else {
                    Toast.makeText(this, "Error setting PIN. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    
    @Override
    public void onBackPressed() {
        if (isVerifying) {
            // Don't allow back if verifying
            return;
        }
        super.onBackPressed();
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        if (isVerifying) {
            // Don't allow back if verifying
            return false;
        }
        finish();
        return true;
    }
}

