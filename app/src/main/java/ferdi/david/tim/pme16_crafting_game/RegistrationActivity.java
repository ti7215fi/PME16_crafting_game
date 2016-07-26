package ferdi.david.tim.pme16_crafting_game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

public class RegistrationActivity extends AppCompatActivity implements Button.OnClickListener, Validator.ValidationListener {

    ApplicationController app;
    Validator validator;

    Button btnRegister;

    @NotEmpty
    EditText etUsername;

    @NotEmpty
    @Password(min = 6, scheme = Password.Scheme.ANY)
    EditText etPassword;

    @NotEmpty
    @ConfirmPassword
    EditText etPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        app = (ApplicationController) getApplication();

        btnRegister = (Button) findViewById(R.id.btnRegister);
        if(btnRegister != null) {
            btnRegister.setOnClickListener(this);
        }

        validator = new Validator(this);
        validator.setValidationListener(this);

        etUsername = (EditText) findViewById(R.id.username_register);
        etPassword = (EditText) findViewById(R.id.password_register);
        etPasswordConfirm = (EditText) findViewById(R.id.password_confirm_register);
    }

    @Override
    public void onClick(View v) {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        List<DBUser> userList = DBUser.find(DBUser.class, "username = ?", etUsername.getText().toString());
        if(userList.size() == 0) {
            DBUser user = new DBUser(etUsername.getText().toString(), etPassword.getText().toString());
            user.save();
            initInventory(user);
            Toast.makeText(this, "Benutzer erfolgreich gespeichert!", Toast.LENGTH_SHORT).show();
            this.finish();
        } else {
            Toast.makeText(this, "User existiert bereits!!", Toast.LENGTH_SHORT).show();
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

    private void initInventory(DBUser user) {
        if(user != null) {
            for (EResourceType resource : EResourceType.values()) {
                if(resource != EResourceType.NONE) {
                    DBResource newResource = new DBResource(resource, user);
                    newResource.save();
                    }
                }
        }
    }
}
