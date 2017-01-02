package com.example.johno_000.json;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Postdata extends ActionBarActivity {
    protected EditText bsemail, bscompanyName, bsname, bslastnameP, bslastnameM, bsloginname, bscif, bspassword ;
    protected Button btnPost;
    private String email, companyName, name, lastnameP, lastnameM, loginname, cif, password;
    Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postdata);

        bsemail = (EditText) findViewById(R.id.email);
        bscompanyName = (EditText) findViewById(R.id.companyName);
        bsname = (EditText) findViewById(R.id.name);
        bslastnameP = (EditText) findViewById(R.id.lastnameP);
        bslastnameM = (EditText) findViewById(R.id.lastnameM);
        bsloginname = (EditText) findViewById(R.id.loginname);
        bscif = (EditText) findViewById(R.id.cif);
        bspassword = (EditText) findViewById(R.id.password);



        btnPost = (Button) findViewById(R.id.btnPost);

        btnPost.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        email = bsemail.getText().toString();
                        companyName = bscompanyName.getText().toString();
                        name = bsname.getText().toString();
                        lastnameP = bslastnameP.getText().toString();
                        lastnameM = bslastnameM.getText().toString();
                        loginname = bsloginname.getText().toString();
                        cif= bscif.getText().toString();
                        password = bspassword.getText().toString();
                        new HttpAsyncTask().execute("http://216.250.125.239:17956/auth/register");
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_postdata, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static String POST(String url, Person person){
        InputStream inputStream = null;
        String result = "";

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            String json= "";

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("email", person.getEmail());
            jsonObject.accumulate("companyName",person.getCompanyName());
            jsonObject.accumulate("name", person.getName());
            jsonObject.accumulate("lastnameP", person.getLastnameP());
            jsonObject.accumulate("lastnameM", person.getLastnameM());
            jsonObject.accumulate("loginname", person.getLoginname());
            jsonObject.accumulate("cif", person.getCif());
            jsonObject.accumulate("password",person.getPassword());

            json = jsonObject.toString();

            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);
           // httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json;charset=UTF-8");

            HttpResponse httpResponse = httpClient.execute(httpPost);

            inputStream = httpResponse.getEntity().getContent();





            Log.d("respuesta", String.valueOf(inputStream));

            if (inputStream != null){
                result = convertInputStreamToString(inputStream);
                Log.d("RESPUETSA::::::::::.", result);
            }
            else {
                result = "Posting Failed!";
            }

        }
        catch (Exception e){
            Log.d("Inputstream", e.getLocalizedMessage());
        }

        return result;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void,String>{
        @Override
        protected String doInBackground(String... params) {
            person = new Person();
            person.setEmail(email);
            person.setCompanyName(companyName);
            person.setName(name);
            person.setLastnameP(lastnameP);
            person.setLastnameM(lastnameM);
            person.setLoginname(loginname);
            person.setCif(cif);
            person.setPassword(password);
            return POST(params[0],person);
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_SHORT).show();
        }
    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while ((line=bufferedReader.readLine())!= null ){
            result +=line;
        }
        inputStream.close();
        return result;
    }
}
