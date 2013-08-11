package no.burkow.crypt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ShareActionProvider;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class crypt extends Activity {

    private ShareActionProvider mShareActionProvider;
    private static final String TAG = "Crypt";
    private TextView cipher;
    private TextView fromText;
    private TextView toText;
    private Button applyCipher;
    private Encryption encryption;
    private Intent shareIntent;
    private Switch toggleCrypt;
    private Boolean cryptState;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crypt_main);

        context = getApplicationContext();

        cipher = (TextView) findViewById(R.id.twCipher);
        fromText = (TextView) findViewById(R.id.twFromText);
        toText = (TextView) findViewById(R.id.twToText);
        applyCipher = (Button) findViewById(R.id.btnCipher);
        toggleCrypt = (Switch) findViewById(R.id.toggleCrypt);

        cryptState = false;

        toggleCrypt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cryptState = b;
                if (cryptState) {
                    toText.setHint("ciphertext");
                    fromText.setHint("plaintext");
                } else {
                    fromText.setHint("ciphertext");
                    toText.setHint("plaintext");
                }
            }
        });

        applyCipher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {;
                    String password = cipher.getText().toString();
                    encryption = new Encryption(password);
                    stringChanged();
                    Toast.makeText(context, "Password changed.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Intent receiveIntent = getIntent();
        String action = receiveIntent.getAction();
        String type = getIntent().getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String sharedText = receiveIntent.getStringExtra(Intent.EXTRA_TEXT);
                fromText.setText(sharedText);
            }
        }

        fromText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                stringChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void stringChanged() {
        try {
            if (encryption == null) {
                Toast.makeText(context, "Remember to set a passphrase for encryption / decryption.", Toast.LENGTH_LONG).show();
            } else {
                String result;
                if (cryptState) {
                    result = encryption.encrypt(fromText.getText().toString());
                } else {
                    try {
                        result = encryption.decrypt(fromText.getText().toString());
                    } catch (Exception e) {
                        result = "Invalid" + ((cryptState)? "plaintext." : "ciphertext.");
                        e.printStackTrace();
                    }
                }

                toText.setText(result);
                shareIntent.putExtra(Intent.EXTRA_TEXT, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.crypt, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();
        mShareActionProvider.setShareIntent(createShareIntent());
        return true;
    }

    private Intent createShareIntent() {
        shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, toText.getText());
        shareIntent.setType("text/plain");
        return shareIntent;
    }
    
}
