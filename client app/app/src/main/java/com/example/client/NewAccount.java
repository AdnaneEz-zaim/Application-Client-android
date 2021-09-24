package com.example.client;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import androidx.appcompat.app.AppCompatActivity;

public class NewAccount extends AppCompatActivity {
    private RequestQueue queue;
    private MyRequest request;
    private String Vcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        if (intent.hasExtra("Register"))
            Toast.makeText(this,intent.getStringExtra("Register"),Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_new_account);
        final TextInputLayout fname= (TextInputLayout) findViewById(R.id.lfname);
        final TextInputLayout lname= (TextInputLayout) findViewById(R.id.llname);
        final TextInputLayout adrr= (TextInputLayout) findViewById(R.id.ladrr);
        final TextInputLayout email= (TextInputLayout) findViewById(R.id.lemail);
        final TextInputLayout tele= (TextInputLayout) findViewById(R.id.ltele);
        final TextInputLayout pass= (TextInputLayout) findViewById(R.id.lpass);
        final TextInputLayout conf= (TextInputLayout) findViewById(R.id.lconf);


        queue = VolleySinglton.getInstance(this).getRequestQueue();
        request = new MyRequest(this, queue);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;

        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.dialog_confirme_email);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final EditText code=(EditText) dialog.findViewById(R.id.code);
        Button validate=(Button) dialog.findViewById(R.id.validate);
        final TextView errorText=(TextView) dialog.findViewById(R.id.erreur);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentCode= code.getText().toString().trim();
                if (currentCode.equals(Vcode)){
                    errorText.setVisibility(View.GONE);
                    dialog.dismiss();
                }else{
                    errorText.setVisibility(View.VISIBLE);
                }
            }
        });
        Button b = (Button) findViewById(R.id.create);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String _fname=fname.getEditText().getText().toString().trim();
                final String _lname=lname.getEditText().getText().toString().trim();
                final String _adrr=adrr.getEditText().getText().toString().trim();
                final String _email=email.getEditText().getText().toString().trim();
                final String _tele=tele.getEditText().getText().toString().trim();
                final String _pass=pass.getEditText().getText().toString().trim();
                final String _conf=conf.getEditText().getText().toString().trim();
                if (! isValid(_email)){
                    email.setError("email non valide");
                }else {
                    if (_fname.length() > 0 && _lname.length() > 0 && _adrr.length() > 0 && _email.length() > 0 && _tele.length() > 0 && _pass.length() > 0 && _conf.length() > 0)
                        request.verify(_fname, _lname, _adrr, _email, _tele, _pass, _conf, new MyRequest.RegisterCallBack() {
                            @Override
                            public void onSuccess(String message) {
                                sendEmailVerification(_fname,_email);
                                dialog.show();
                                dialog.getWindow().setLayout((6*width)/7, WindowManager.LayoutParams.WRAP_CONTENT);
                                dialog.setCancelable(false);
                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        if (_fname.length()>0 && _lname.length()>0 && _adrr.length()>0 && _email.length()>0 && _tele.length()>0 && _pass.length()>0 && _conf.length()>0)
                                            request.register(_fname, _lname, _adrr, _email, _tele, _pass, _conf, new MyRequest.RegisterCallBack() {
                                                @Override
                                                public void onSuccess(String message) {
                                                    Toast.makeText(getApplicationContext(),"Vous étes bien enregistrer",Toast.LENGTH_LONG).show();
                                                    finish();
                                                }

                                                @Override
                                                public void inputErr(Map<String, String> errors) {
                                                    if (errors.get("lname") != null)
                                                        lname.setError(errors.get("lname"));
                                                    else lname.setErrorEnabled(false);
                                                    if (errors.get("email") != null)
                                                        email.setError(errors.get("email"));
                                                    else email.setErrorEnabled(false);
                                                    if (errors.get("tele") != null)
                                                        tele.setError(errors.get("tele"));
                                                    else tele.setErrorEnabled(false);
                                                    if (errors.get("conf") != null)
                                                        conf.setError(errors.get("conf"));
                                                    else conf.setErrorEnabled(false);
                                                }

                                                @Override
                                                public void onErr(String message) {
                                                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        else
                                            Toast.makeText(getApplicationContext(),"Remplir tous les champs",Toast.LENGTH_LONG).show();

                                    }
                                });
                            }

                            @Override
                            public void inputErr(Map<String, String> errors) {
                                if (errors.get("lname") != null)
                                    lname.setError(errors.get("lname"));
                                else lname.setErrorEnabled(false);
                                if (errors.get("email") != null)
                                    email.setError(errors.get("email"));
                                else email.setErrorEnabled(false);
                                if (errors.get("tele") != null)
                                    tele.setError(errors.get("tele"));
                                else tele.setErrorEnabled(false);
                                if (errors.get("conf") != null)
                                    conf.setError(errors.get("conf"));
                                else conf.setErrorEnabled(false);
                            }

                            @Override
                            public void onErr(String message) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            }
                        });
                    else
                        Toast.makeText(getApplicationContext(), "Remplir tous les champs", Toast.LENGTH_LONG).show();
                }
            }
        });
/* att njrb
        Button b = (Button) findViewById(R.id.create);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String _fname=fname.getEditText().getText().toString().trim();
                final String _lname=lname.getEditText().getText().toString().trim();
                final String _adrr=adrr.getEditText().getText().toString().trim();
                final String _email=email.getEditText().getText().toString().trim();
                final String _tele=tele.getEditText().getText().toString().trim();
                final String _pass=pass.getEditText().getText().toString().trim();
                final String _conf=conf.getEditText().getText().toString().trim();
                if (! isValid(_email)){
                    email.setError("email non valide");
                }else{
                    //try {
                        sendEmailVerification(_fname,_email);
                        Log.i("TAG", "onClgoodick: ");
                        /*
                    } catch (MessagingException e) {
                        e.printStackTrace();

                        Log.i("TAG", "noooooooo: ");
                    }

                         */
/*
                    dialog.show();

                    dialog.getWindow().setLayout((6*width)/7, WindowManager.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(false);
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (_fname.length()>0 && _lname.length()>0 && _adrr.length()>0 && _email.length()>0 && _tele.length()>0 && _pass.length()>0 && _conf.length()>0)
                                request.register(_fname, _lname, _adrr, _email, _tele, _pass, _conf, new MyRequest.RegisterCallBack() {
                                    @Override
                                    public void onSuccess(String message) {
                                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                        intent.putExtra("Register",message);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void inputErr(Map<String, String> errors) {
                                        if (errors.get("lname") != null)
                                            lname.setError(errors.get("lname"));
                                        else lname.setErrorEnabled(false);
                                        if (errors.get("email") != null)
                                            email.setError(errors.get("email"));
                                        else email.setErrorEnabled(false);
                                        if (errors.get("tele") != null)
                                            tele.setError(errors.get("tele"));
                                        else tele.setErrorEnabled(false);
                                        if (errors.get("conf") != null)
                                            conf.setError(errors.get("conf"));
                                        else conf.setErrorEnabled(false);
                                    }

                                    @Override
                                    public void onErr(String message) {
                                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                    }
                                });
                            else
                                Toast.makeText(getApplicationContext(),"Remplir tous les champs",Toast.LENGTH_LONG).show();

                        }
                    });
                }

            }

        });

 */
    }

    private void sendEmailVerification(String name,String email) /*throws MessagingException */ {
        Random random=new Random();
        String code="";
        for (int i=0;i<4;i++){
            int index = random.nextInt(10-1) + 1;
            code+=index;
        }
        String message="Bonjour "+name+"\nVotre code d'acivation est: "+code;
        //Mail(email, "Verifecation", message);

        JavaMail javaMailAPI=new JavaMail(this,email,"Verifecation",message);
        javaMailAPI.execute();

        Vcode=code+"";
    }

    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public void Mail(String to, String subject, String content_message)
            throws MessagingException {

        boolean debug = false;

        try {
            String host = "smtp.gmail.com";
            String from = "binome1920@gmail.com";
            String pass = "adnane_iliass";
            Properties props = System.getProperties();
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.user", from);
            props.put("mail.smtp.password", pass);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            InternetAddress toAddress = new InternetAddress(to);
            message.addRecipient(Message.RecipientType.TO, toAddress);
            message.setSubject(subject);

            message.setContent(content_message, "text/html; charset=\"UTF-8\"");
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            Toast.makeText(this,"Message envoyé",Toast.LENGTH_LONG).show();

        }

        catch (Exception e) {
            Toast.makeText(this,"Message Non envoyé"+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
/*
    public MimeMessage createEmail(String to,
                                          String from,
                                          String subject,
                                          String bodyText)
            throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    public static Message createMessageWithEmail(MimeMessage emailContent)
            throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message() {
            @Override
            public int getSize() throws MessagingException {
                return 0;
            }

            @Override
            public int getLineCount() throws MessagingException {
                return 0;
            }

            @Override
            public String getContentType() throws MessagingException {
                return null;
            }

            @Override
            public boolean isMimeType(String s) throws MessagingException {
                return false;
            }

            @Override
            public String getDisposition() throws MessagingException {
                return null;
            }

            @Override
            public void setDisposition(String s) throws MessagingException {

            }

            @Override
            public String getDescription() throws MessagingException {
                return null;
            }

            @Override
            public void setDescription(String s) throws MessagingException {

            }

            @Override
            public String getFileName() throws MessagingException {
                return null;
            }

            @Override
            public void setFileName(String s) throws MessagingException {

            }

            @Override
            public InputStream getInputStream() throws IOException, MessagingException {
                return null;
            }

            @Override
            public DataHandler getDataHandler() throws MessagingException {
                return null;
            }

            @Override
            public Object getContent() throws IOException, MessagingException {
                return null;
            }

            @Override
            public void setDataHandler(DataHandler dataHandler) throws MessagingException {

            }

            @Override
            public void setContent(Object o, String s) throws MessagingException {

            }

            @Override
            public void setText(String s) throws MessagingException {

            }

            @Override
            public void setContent(Multipart multipart) throws MessagingException {

            }

            @Override
            public void writeTo(OutputStream outputStream) throws IOException, MessagingException {

            }

            @Override
            public String[] getHeader(String s) throws MessagingException {
                return new String[0];
            }

            @Override
            public void setHeader(String s, String s1) throws MessagingException {

            }

            @Override
            public void addHeader(String s, String s1) throws MessagingException {

            }

            @Override
            public void removeHeader(String s) throws MessagingException {

            }

            @Override
            public Enumeration getAllHeaders() throws MessagingException {
                return null;
            }

            @Override
            public Enumeration getMatchingHeaders(String[] strings) throws MessagingException {
                return null;
            }

            @Override
            public Enumeration getNonMatchingHeaders(String[] strings) throws MessagingException {
                return null;
            }

            @Override
            public Address[] getFrom() throws MessagingException {
                return new Address[0];
            }

            @Override
            public void setFrom() throws MessagingException {

            }

            @Override
            public void setFrom(Address address) throws MessagingException {

            }

            @Override
            public void addFrom(Address[] addresses) throws MessagingException {

            }

            @Override
            public Address[] getRecipients(RecipientType recipientType) throws MessagingException {
                return new Address[0];
            }

            @Override
            public void setRecipients(RecipientType recipientType, Address[] addresses) throws MessagingException {

            }

            @Override
            public void addRecipients(RecipientType recipientType, Address[] addresses) throws MessagingException {

            }

            @Override
            public String getSubject() throws MessagingException {
                return null;
            }

            @Override
            public void setSubject(String s) throws MessagingException {

            }

            @Override
            public Date getSentDate() throws MessagingException {
                return null;
            }

            @Override
            public void setSentDate(Date date) throws MessagingException {

            }

            @Override
            public Date getReceivedDate() throws MessagingException {
                return null;
            }

            @Override
            public Flags getFlags() throws MessagingException {
                return null;
            }

            @Override
            public void setFlags(Flags flags, boolean b) throws MessagingException {

            }

            @Override
            public Message reply(boolean b) throws MessagingException {
                return null;
            }

            @Override
            public void saveChanges() throws MessagingException {

            }
        };
        message.setRaw(encodedEmail);
        return message;
    }

 */
}
