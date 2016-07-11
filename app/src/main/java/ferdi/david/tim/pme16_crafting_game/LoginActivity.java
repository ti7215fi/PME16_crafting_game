package ferdi.david.tim.pme16_crafting_game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements Validator.ValidationListener{

    private ApplicationController app;
    private static final String     LOG_TAG = LoginActivity.class.getSimpleName();

    private Validator validator;

    // UI references
    private Button  btnLogin;
    private Button.OnClickListener loginOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            validator.validate();
        }
    };

    private Button  btnRegister;
    private Button.OnClickListener registerOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(i);
        }
    };

    @NotEmpty
    private EditText etUsername;

    @NotEmpty
    @Password(min = 6, scheme = Password.Scheme.ANY)
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        app = (ApplicationController) getApplication();

        btnLogin = (Button) findViewById(R.id.btnSignUp);
        if(btnLogin != null) {
            btnLogin.setOnClickListener(loginOnClickListener);
        }

        btnRegister = (Button) findViewById(R.id.btnSignIn);
        if(btnRegister != null) {
            btnRegister.setOnClickListener(registerOnClickListener);
        }

        validator = new Validator(this);
        validator.setValidationListener(this);

        etUsername = (EditText) findViewById(R.id.username_login);
        etPassword = (EditText) findViewById(R.id.password_login);
    }

    @Override
    public void onValidationSucceeded() {
        List<DBUser> userList = DBUser.find(DBUser.class,"username = ?", etUsername.getText().toString());
        if(userList.size() == 1 && userList.get(0).getPassword().equals(etPassword.getText().toString())) {
           app.setUser(userList.get(0));
           Toast.makeText(this, "Erfolgreich eingeloggt!", Toast.LENGTH_SHORT).show();
            this.finish();
        } else {
            Toast.makeText(this, "Benutzer existiert nicht oder das Passwort ist falsch!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
