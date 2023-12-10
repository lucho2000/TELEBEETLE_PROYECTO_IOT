package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.telebeetle.Entity.Donacion;
import com.example.telebeetle.Entity.Usuario;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityDetailDonationBinding;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class DetailDonationActivity extends AppCompatActivity {


    FirebaseDatabase db = FirebaseDatabase.getInstance();

    ActivityDetailDonationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailDonationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        Donacion donacion = (Donacion) intent.getSerializableExtra("donacion");

        binding.textView47.setText("S./ " + donacion.getMonto());

        Picasso.get().load(donacion.getImagenCaptura()).into(binding.imageView9);



        Toolbar toolbar = findViewById(R.id.myToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        binding.button5.setOnClickListener(view -> {

            DatabaseReference myRef = db.getReference("donaciones_por_validar/"+donacion.getKeyDonacion());
            myRef.removeValue();
            //myRef.setValue(false);
            Toast.makeText(DetailDonationActivity.this,"La donacion no ha sido aceptada",Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(DetailDonationActivity.this,ValidarDonacionesActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent1);
            finish();
        });
        binding.button3.setOnClickListener(view -> {


            DatabaseReference donacionesRef = db.getReference("donaciones");
            DatabaseReference donacionPorValidarRef = db.getReference("donaciones_por_validar").child(donacion.getKeyDonacion());
            donacionPorValidarRef.child("accepted").setValue(true);
            donacion.setAccepted(true);
            donacionPorValidarRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dataSnapshot.getRef().removeValue();
                    donacionesRef.child(donacion.getKeyDonacion()).setValue(dataSnapshot.getValue());
                    Toast.makeText(DetailDonationActivity.this, "La donacion ha sido aceptada", Toast.LENGTH_SHORT).show();


                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference usersRef = database.getReference("usuarios");
                    Query query = usersRef.orderByChild("uidUsuario").equalTo(donacion.getUidDonante());

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                // Found the user, you can now work with the userSnapshot
                                Usuario user = userSnapshot.getValue(Usuario.class);


                                if(user.getCondicion().equalsIgnoreCase("Egresado")){
                                    String emailContent = "<html><head>\n" +
                                            "\n" +
                                            "  <meta charset=\"utf-8\">\n" +
                                            "  <meta http-equiv=\"x-ua-compatible\" content=\"ie=edge\">\n" +
                                            "  <title>Email Receipt</title>\n" +
                                            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                                            "  <style type=\"text/css\">\n" +
                                            "  /**\n" +
                                            "   * Google webfonts. Recommended to include the .woff version for cross-client compatibility.\n" +
                                            "   */\n" +
                                            "  @media screen {\n" +
                                            "    @font-face {\n" +
                                            "      font-family: 'Source Sans Pro';\n" +
                                            "      font-style: normal;\n" +
                                            "      font-weight: 400;\n" +
                                            "      src: local('Source Sans Pro Regular'), local('SourceSansPro-Regular'), url(https://fonts.gstatic.com/s/sourcesanspro/v10/ODelI1aHBYDBqgeIAH2zlBM0YzuT7MdOe03otPbuUS0.woff) format('woff');\n" +
                                            "    }\n" +
                                            "\n" +
                                            "    @font-face {\n" +
                                            "      font-family: 'Source Sans Pro';\n" +
                                            "      font-style: normal;\n" +
                                            "      font-weight: 700;\n" +
                                            "      src: local('Source Sans Pro Bold'), local('SourceSansPro-Bold'), url(https://fonts.gstatic.com/s/sourcesanspro/v10/toadOcfmlt9b38dHJxOBGFkQc6VGVFSmCnC_l7QZG60.woff) format('woff');\n" +
                                            "    }\n" +
                                            "  }\n" +
                                            "\n" +
                                            "  /**\n" +
                                            "   * Avoid browser level font resizing.\n" +
                                            "   * 1. Windows Mobile\n" +
                                            "   * 2. iOS / OSX\n" +
                                            "   */\n" +
                                            "  body,\n" +
                                            "  table,\n" +
                                            "  td,\n" +
                                            "  a {\n" +
                                            "    -ms-text-size-adjust: 100%; /* 1 */\n" +
                                            "    -webkit-text-size-adjust: 100%; /* 2 */\n" +
                                            "  }\n" +
                                            "\n" +
                                            "  /**\n" +
                                            "   * Remove extra space added to tables and cells in Outlook.\n" +
                                            "   */\n" +
                                            "  table,\n" +
                                            "  td {\n" +
                                            "    mso-table-rspace: 0pt;\n" +
                                            "    mso-table-lspace: 0pt;\n" +
                                            "  }\n" +
                                            "\n" +
                                            "  /**\n" +
                                            "   * Better fluid images in Internet Explorer.\n" +
                                            "   */\n" +
                                            "  img {\n" +
                                            "    -ms-interpolation-mode: bicubic;\n" +
                                            "  }\n" +
                                            "\n" +
                                            "  /**\n" +
                                            "   * Remove blue links for iOS devices.\n" +
                                            "   */\n" +
                                            "  a[x-apple-data-detectors] {\n" +
                                            "    font-family: inherit !important;\n" +
                                            "    font-size: inherit !important;\n" +
                                            "    font-weight: inherit !important;\n" +
                                            "    line-height: inherit !important;\n" +
                                            "    color: inherit !important;\n" +
                                            "    text-decoration: none !important;\n" +
                                            "  }\n" +
                                            "\n" +
                                            "  /**\n" +
                                            "   * Fix centering issues in Android 4.4.\n" +
                                            "   */\n" +
                                            "  div[style*=\"margin: 16px 0;\"] {\n" +
                                            "    margin: 0 !important;\n" +
                                            "  }\n" +
                                            "\n" +
                                            "  body {\n" +
                                            "    width: 100% !important;\n" +
                                            "    height: 100% !important;\n" +
                                            "    padding: 0 !important;\n" +
                                            "    margin: 0 !important;\n" +
                                            "  }\n" +
                                            "\n" +
                                            "  /**\n" +
                                            "   * Collapse table borders to avoid space between cells.\n" +
                                            "   */\n" +
                                            "  table {\n" +
                                            "    border-collapse: collapse !important;\n" +
                                            "  }\n" +
                                            "\n" +
                                            "  a {\n" +
                                            "    color: #1a82e2;\n" +
                                            "  }\n" +
                                            "\n" +
                                            "  img {\n" +
                                            "    height: auto;\n" +
                                            "    line-height: 100%;\n" +
                                            "    text-decoration: none;\n" +
                                            "    border: 0;\n" +
                                            "    outline: none;\n" +
                                            "  }\n" +
                                            "  </style>\n" +
                                            "\n" +
                                            "</head>\n" +
                                            "<body style=\"background-color: #D2C7BA;\">\n" +
                                            "\n" +
                                            "  <!-- start preheader -->\n" +
                                            "  <div class=\"preheader\" style=\"display: none; max-width: 0; max-height: 0; overflow: hidden; font-size: 1px; line-height: 1px; color: #fff; opacity: 0;\">\n" +
                                            "    A preheader is the short summary text that follows the subject line when an email is viewed in the inbox.\n" +
                                            "  </div>\n" +
                                            "  <!-- end preheader -->\n" +
                                            "\n" +
                                            "  <!-- start body -->\n" +
                                            "  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                                            "\n" +
                                            "    <!-- start logo -->\n" +
                                            "    <tbody><tr>\n" +
                                            "      <td align=\"center\" bgcolor=\"#D2C7BA\">\n" +
                                            "        <!--[if (gte mso 9)|(IE)]>\n" +
                                            "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                                            "        <tr>\n" +
                                            "        <td align=\"center\" valign=\"top\" width=\"600\">\n" +
                                            "        <![endif]-->\n" +
                                            "        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                                            "          <tbody><tr>\n" +
                                            "            <td align=\"center\" valign=\"top\" style=\"padding: 36px 24px;\">\n" +
                                            "              <a href=\"https://sendgrid.com\" target=\"_blank\" style=\"display: inline-block;\">\n" +
                                            "                <img src=\"https://firebasestorage.googleapis.com/v0/b/prueba-iot-654b2.appspot.com/o/telito.png?alt=media&token=80a7b7a4-6bf1-4ed7-b25d-cb64adeadb5b\" alt=\"Logo\" border=\"0\" width=\"48\" style=\"display: block; width: 48px; max-width: 48px; min-width: 48px;\">\n" +
                                            "              </a>\n" +
                                            "            </td>\n" +
                                            "          </tr>\n" +
                                            "        </tbody></table>\n" +
                                            "        <!--[if (gte mso 9)|(IE)]>\n" +
                                            "        </td>\n" +
                                            "        </tr>\n" +
                                            "        </table>\n" +
                                            "        <![endif]-->\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "    <!-- end logo -->\n" +
                                            "\n" +
                                            "    <!-- start hero -->\n" +
                                            "    <tr>\n" +
                                            "      <td align=\"center\" bgcolor=\"#D2C7BA\">\n" +
                                            "        <!--[if (gte mso 9)|(IE)]>\n" +
                                            "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                                            "        <tr>\n" +
                                            "        <td align=\"center\" valign=\"top\" width=\"600\">\n" +
                                            "        <![endif]-->\n" +
                                            "        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                                            "          <tbody><tr>\n" +
                                            "            <td align=\"left\" bgcolor=\"#ffffff\" style=\"padding: 36px 24px 0; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; border-top: 3px solid #d4dadf;\">\n" +
                                            "              <h1 style=\"margin: 0; font-size: 32px; font-weight: 700; letter-spacing: -1px; line-height: 48px;\">Gracias por su donacion!</h1>\n" +
                                            "            </td>\n" +
                                            "          </tr>\n" +
                                            "        </tbody></table>\n" +
                                            "        <!--[if (gte mso 9)|(IE)]>\n" +
                                            "        </td>\n" +
                                            "        </tr>\n" +
                                            "        </table>\n" +
                                            "        <![endif]-->\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "    <!-- end hero -->\n" +
                                            "\n" +
                                            "    <!-- start copy block -->\n" +
                                            "    <tr>\n" +
                                            "      <td align=\"center\" bgcolor=\"#D2C7BA\">\n" +
                                            "        <!--[if (gte mso 9)|(IE)]>\n" +
                                            "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                                            "        <tr>\n" +
                                            "        <td align=\"center\" valign=\"top\" width=\"600\">\n" +
                                            "        <![endif]-->\n" +
                                            "        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                                            "\n" +
                                            "          <!-- start copy -->\n" +
                                            "          <tbody><tr>\n" +
                                            "            <td align=\"left\" bgcolor=\"#ffffff\" style=\"padding: 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;\">\n" +
                                            "              <p style=\"margin: 0;\">Este es un resumen del kit teleco que ha solicitado.</p>\n" +
                                            "            </td>\n" +
                                            "          </tr>\n" +
                                            "          <!-- end copy -->\n" +
                                            "\n" +
                                            "          <!-- start receipt table -->\n" +
                                            "          <tr>\n" +
                                            "            <td align=\"left\" bgcolor=\"#ffffff\" style=\"padding: 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;\">\n" +
                                            "              <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                                            "                <tbody><tr>\n" +
                                            "                  <td align=\"left\" bgcolor=\"#D2C7BA\" width=\"75%\" style=\"padding: 12px;font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;\"><strong>Orden #</strong></td>\n" +
                                            "                  <td align=\"left\" bgcolor=\"#D2C7BA\" width=\"25%\" style=\"padding: 12px;font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;\"><strong>0000224</strong></td>\n" +
                                            "                </tr>\n" +
                                            "                <tr>\n" +
                                            "                  <td align=\"left\" width=\"75%\" style=\"padding: 6px 12px;font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;\">Polo Teleco</td>\n" +
                                            "                  <td align=\"left\" width=\"25%\" style=\"padding: 6px 12px;font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;\">S/50.00</td>\n" +
                                            "                </tr>\n" +
                                            "                <tr>\n" +
                                            "                  <td align=\"left\" width=\"75%\" style=\"padding: 6px 12px;font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;\">Short Teleco</td>\n" +
                                            "                  <td align=\"left\" width=\"25%\" style=\"padding: 6px 12px;font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;\">S/30.00</td>\n" +
                                            "                </tr>\n" +
                                            "                <tr>\n" +
                                            "                  <td align=\"left\" width=\"75%\" style=\"padding: 6px 12px;font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;\">Medias Telecas</td>\n" +
                                            "                  <td align=\"left\" width=\"25%\" style=\"padding: 6px 12px;font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;\">S/10.00</td>\n" +
                                            "                </tr>\n" +
                                            "                <tr>\n" +
                                            "                  <td align=\"left\" width=\"75%\" style=\"padding: 6px 12px;font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;\">Sticker Teleco</td>\n" +
                                            "                  <td align=\"left\" width=\"25%\" style=\"padding: 6px 12px;font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;\">S/10.00</td>\n" +
                                            "                </tr>\n" +
                                            "                <tr>\n" +
                                            "                  <td align=\"left\" width=\"75%\" style=\"padding: 12px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; border-top: 2px dashed #D2C7BA; border-bottom: 2px dashed #D2C7BA;\"><strong>Total</strong></td>\n" +
                                            "                  <td align=\"left\" width=\"25%\" style=\"padding: 12px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; border-top: 2px dashed #D2C7BA; border-bottom: 2px dashed #D2C7BA;\"><strong>S/100.00</strong></td>\n" +
                                            "                </tr>\n" +
                                            "              </tbody></table>\n" +
                                            "            </td>\n" +
                                            "          </tr>\n" +
                                            "          <!-- end reeipt table -->\n" +
                                            "\n" +
                                            "        </tbody></table>\n" +
                                            "        <!--[if (gte mso 9)|(IE)]>\n" +
                                            "        </td>\n" +
                                            "        </tr>\n" +
                                            "        </table>\n" +
                                            "        <![endif]-->\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "    <!-- end copy block -->\n" +
                                            "\n" +
                                            "    <!-- start receipt address block -->\n" +
                                            "    <tr>\n" +
                                            "      <td align=\"center\" bgcolor=\"#D2C7BA\" valign=\"top\" width=\"100%\">\n" +
                                            "        <!--[if (gte mso 9)|(IE)]>\n" +
                                            "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                                            "        <tr>\n" +
                                            "        <td align=\"center\" valign=\"top\" width=\"600\">\n" +
                                            "        <![endif]-->\n" +
                                            "        <table align=\"center\" bgcolor=\"#ffffff\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                                            "          <tbody><tr>\n" +
                                            "            <td align=\"center\" valign=\"top\" style=\"font-size: 0; border-bottom: 3px solid #d4dadf\">\n" +
                                            "              <!--[if (gte mso 9)|(IE)]>\n" +
                                            "              <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                                            "              <tr>\n" +
                                            "              <td align=\"left\" valign=\"top\" width=\"300\">\n" +
                                            "              <![endif]-->\n" +
                                            "              <div style=\"display: inline-block; width: 100%; max-width: 50%; min-width: 240px; vertical-align: top;\">\n" +
                                            "                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 300px;\">\n" +
                                            "                  <tbody><tr>\n" +
                                            "                    <td align=\"left\" valign=\"top\" style=\"padding-bottom: 36px; padding-left: 36px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;\">\n" +
                                            "                      <p><strong>Direccion de Recojo</strong></p>\n" +
                                            "                      <p>Pabellon V<br>Piso 1<br>La Bati</p>\n" +
                                            "                    </td>\n" +
                                            "                  </tr>\n" +
                                            "                </tbody></table>\n" +
                                            "              </div>\n" +
                                            "              <!--[if (gte mso 9)|(IE)]>\n" +
                                            "              </td>\n" +
                                            "              <td align=\"left\" valign=\"top\" width=\"300\">\n" +
                                            "              <![endif]-->\n" +
                                            "              <div style=\"display: inline-block; width: 100%; max-width: 50%; min-width: 240px; vertical-align: top;\">\n" +
                                            "                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 300px;\">\n" +
                                            "                  <tbody><tr>\n" +
                                            "                    <td align=\"left\" valign=\"top\" style=\"padding-bottom: 36px; padding-left: 36px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;\">\n" +
                                            "                      <p><strong>PUCP</strong></p>\n" +
                                            "                      <p>Av. Universitaria 1801<br>San Miguel<br>Lima, Peru</p>\n" +
                                            "                    </td>\n" +
                                            "                  </tr>\n" +
                                            "                </tbody></table>\n" +
                                            "              </div>\n" +
                                            "              <!--[if (gte mso 9)|(IE)]>\n" +
                                            "              </td>\n" +
                                            "              </tr>\n" +
                                            "              </table>\n" +
                                            "              <![endif]-->\n" +
                                            "            </td>\n" +
                                            "          </tr>\n" +
                                            "        </tbody></table>\n" +
                                            "        <!--[if (gte mso 9)|(IE)]>\n" +
                                            "        </td>\n" +
                                            "        </tr>\n" +
                                            "        </table>\n" +
                                            "        <![endif]-->\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "    <!-- end receipt address block -->\n" +
                                            "\n" +
                                            "    <!-- start footer -->\n" +
                                            "    <tr>\n" +
                                            "      <td align=\"center\" bgcolor=\"#D2C7BA\" style=\"padding: 24px;\">\n" +
                                            "        <!--[if (gte mso 9)|(IE)]>\n" +
                                            "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                                            "        <tr>\n" +
                                            "        <td align=\"center\" valign=\"top\" width=\"600\">\n" +
                                            "        <![endif]-->\n" +
                                            "        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n" +
                                            "\n" +
                                            "          <!-- start permission -->\n" +
                                            "          <tbody><tr>\n" +
                                            "            <td align=\"center\" bgcolor=\"#D2C7BA\" style=\"padding: 12px 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 20px; color: #666;\">\n" +
                                            "              <p style=\"margin: 0;\">Telebeetle.com Grupo2</p>\n" +
                                            "            </td>\n" +
                                            "          </tr>\n" +
                                            "          <!-- end permission -->\n" +
                                            "\n" +
                                            "          <!-- start unsubscribe -->\n" +
                                            "          <tr>\n" +
                                            "            <td align=\"center\" bgcolor=\"#D2C7BA\" style=\"padding: 12px 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 20px; color: #666;\">\n" +
                                            "              <p style=\"margin: 0;\">Servicios de IOT TELECOMUNICACIONES</p>\n" +
                                            "              <p style=\"margin: 0;\">20200334-20202073-20195922-20171198</p>\n" +
                                            "            </td>\n" +
                                            "          </tr>\n" +
                                            "          <!-- end unsubscribe -->\n" +
                                            "\n" +
                                            "        </tbody></table>\n" +
                                            "        <!--[if (gte mso 9)|(IE)]>\n" +
                                            "        </td>\n" +
                                            "        </tr>\n" +
                                            "        </table>\n" +
                                            "        <![endif]-->\n" +
                                            "      </td>\n" +
                                            "    </tr>\n" +
                                            "    <!-- end footer -->\n" +
                                            "\n" +
                                            "  </tbody></table>\n" +
                                            "  <!-- end body -->\n" +
                                            "\n" +
                                            "\n" +
                                            "</body></html>";

                                    buttonSendEmail(user.getCorreo(),emailContent);    
                                } else if (user.getCondicion().equalsIgnoreCase("alumno")) {
                                    String emailcontent = "<html><head>\n" +
                                            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                                            "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                                            "    <title>Simple Transactional Email</title>\n" +
                                            "    <style media=\"all\" type=\"text/css\">\n" +
                                            "    /* -------------------------------------\n" +
                                            "    GLOBAL RESETS\n" +
                                            "------------------------------------- */\n" +
                                            "    \n" +
                                            "    body {\n" +
                                            "      font-family: Helvetica, sans-serif;\n" +
                                            "      -webkit-font-smoothing: antialiased;\n" +
                                            "      font-size: 16px;\n" +
                                            "      line-height: 1.3;\n" +
                                            "      -ms-text-size-adjust: 100%;\n" +
                                            "      -webkit-text-size-adjust: 100%;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    table {\n" +
                                            "      border-collapse: separate;\n" +
                                            "      mso-table-lspace: 0pt;\n" +
                                            "      mso-table-rspace: 0pt;\n" +
                                            "      width: 100%;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    table td {\n" +
                                            "      font-family: Helvetica, sans-serif;\n" +
                                            "      font-size: 16px;\n" +
                                            "      vertical-align: top;\n" +
                                            "    }\n" +
                                            "    /* -------------------------------------\n" +
                                            "    BODY & CONTAINER\n" +
                                            "------------------------------------- */\n" +
                                            "    \n" +
                                            "    body {\n" +
                                            "      background-color: #f4f5f6;\n" +
                                            "      margin: 0;\n" +
                                            "      padding: 0;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    .body {\n" +
                                            "      background-color: #f4f5f6;\n" +
                                            "      width: 100%;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    .container {\n" +
                                            "      margin: 0 auto !important;\n" +
                                            "      max-width: 600px;\n" +
                                            "      padding: 0;\n" +
                                            "      padding-top: 24px;\n" +
                                            "      width: 600px;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    .content {\n" +
                                            "      box-sizing: border-box;\n" +
                                            "      display: block;\n" +
                                            "      margin: 0 auto;\n" +
                                            "      max-width: 600px;\n" +
                                            "      padding: 0;\n" +
                                            "    }\n" +
                                            "    /* -------------------------------------\n" +
                                            "    HEADER, FOOTER, MAIN\n" +
                                            "------------------------------------- */\n" +
                                            "    \n" +
                                            "    .main {\n" +
                                            "      background: #ffffff;\n" +
                                            "      border: 1px solid #eaebed;\n" +
                                            "      border-radius: 16px;\n" +
                                            "      width: 100%;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    .wrapper {\n" +
                                            "      box-sizing: border-box;\n" +
                                            "      padding: 24px;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    .footer {\n" +
                                            "      clear: both;\n" +
                                            "      padding-top: 24px;\n" +
                                            "      text-align: center;\n" +
                                            "      width: 100%;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    .footer td,\n" +
                                            "    .footer p,\n" +
                                            "    .footer span,\n" +
                                            "    .footer a {\n" +
                                            "      color: #9a9ea6;\n" +
                                            "      font-size: 16px;\n" +
                                            "      text-align: center;\n" +
                                            "    }\n" +
                                            "    /* -------------------------------------\n" +
                                            "    TYPOGRAPHY\n" +
                                            "------------------------------------- */\n" +
                                            "    \n" +
                                            "    p {\n" +
                                            "      font-family: Helvetica, sans-serif;\n" +
                                            "      font-size: 16px;\n" +
                                            "      font-weight: normal;\n" +
                                            "      margin: 0;\n" +
                                            "      margin-bottom: 16px;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    a {\n" +
                                            "      color: #0867ec;\n" +
                                            "      text-decoration: underline;\n" +
                                            "    }\n" +
                                            "    /* -------------------------------------\n" +
                                            "    BUTTONS\n" +
                                            "------------------------------------- */\n" +
                                            "    \n" +
                                            "    .btn {\n" +
                                            "      box-sizing: border-box;\n" +
                                            "      min-width: 100% !important;\n" +
                                            "      width: 100%;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    .btn > tbody > tr > td {\n" +
                                            "      padding-bottom: 16px;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    .btn table {\n" +
                                            "      width: auto;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    .btn table td {\n" +
                                            "      background-color: #ffffff;\n" +
                                            "      border-radius: 4px;\n" +
                                            "      text-align: center;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    .btn a {\n" +
                                            "      background-color: #ffffff;\n" +
                                            "      border: solid 2px #0867ec;\n" +
                                            "      border-radius: 4px;\n" +
                                            "      box-sizing: border-box;\n" +
                                            "      color: #0867ec;\n" +
                                            "      cursor: pointer;\n" +
                                            "      display: inline-block;\n" +
                                            "      font-size: 16px;\n" +
                                            "      font-weight: bold;\n" +
                                            "      margin: 0;\n" +
                                            "      padding: 12px 24px;\n" +
                                            "      text-decoration: none;\n" +
                                            "      text-transform: capitalize;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    .btn-primary table td {\n" +
                                            "      background-color: #0867ec;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    .btn-primary a {\n" +
                                            "      background-color: #0867ec;\n" +
                                            "      border-color: #0867ec;\n" +
                                            "      color: #ffffff;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    @media all {\n" +
                                            "      .btn-primary table td:hover {\n" +
                                            "        background-color: #ec0867 !important;\n" +
                                            "      }\n" +
                                            "      .btn-primary a:hover {\n" +
                                            "        background-color: #ec0867 !important;\n" +
                                            "        border-color: #ec0867 !important;\n" +
                                            "      }\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    /* -------------------------------------\n" +
                                            "    OTHER STYLES THAT MIGHT BE USEFUL\n" +
                                            "------------------------------------- */\n" +
                                            "    \n" +
                                            "    .last {\n" +
                                            "      margin-bottom: 0;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    .first {\n" +
                                            "      margin-top: 0;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    .align-center {\n" +
                                            "      text-align: center;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    .align-right {\n" +
                                            "      text-align: right;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    .align-left {\n" +
                                            "      text-align: left;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    .text-link {\n" +
                                            "      color: #0867ec !important;\n" +
                                            "      text-decoration: underline !important;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    .clear {\n" +
                                            "      clear: both;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    .mt0 {\n" +
                                            "      margin-top: 0;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    .mb0 {\n" +
                                            "      margin-bottom: 0;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    .preheader {\n" +
                                            "      color: transparent;\n" +
                                            "      display: none;\n" +
                                            "      height: 0;\n" +
                                            "      max-height: 0;\n" +
                                            "      max-width: 0;\n" +
                                            "      opacity: 0;\n" +
                                            "      overflow: hidden;\n" +
                                            "      mso-hide: all;\n" +
                                            "      visibility: hidden;\n" +
                                            "      width: 0;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    .powered-by a {\n" +
                                            "      text-decoration: none;\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    /* -------------------------------------\n" +
                                            "    RESPONSIVE AND MOBILE FRIENDLY STYLES\n" +
                                            "------------------------------------- */\n" +
                                            "    \n" +
                                            "    @media only screen and (max-width: 640px) {\n" +
                                            "      .main p,\n" +
                                            "      .main td,\n" +
                                            "      .main span {\n" +
                                            "        font-size: 16px !important;\n" +
                                            "      }\n" +
                                            "      .wrapper {\n" +
                                            "        padding: 8px !important;\n" +
                                            "      }\n" +
                                            "      .content {\n" +
                                            "        padding: 0 !important;\n" +
                                            "      }\n" +
                                            "      .container {\n" +
                                            "        padding: 0 !important;\n" +
                                            "        padding-top: 8px !important;\n" +
                                            "        width: 100% !important;\n" +
                                            "      }\n" +
                                            "      .main {\n" +
                                            "        border-left-width: 0 !important;\n" +
                                            "        border-radius: 0 !important;\n" +
                                            "        border-right-width: 0 !important;\n" +
                                            "      }\n" +
                                            "      .btn table {\n" +
                                            "        max-width: 100% !important;\n" +
                                            "        width: 100% !important;\n" +
                                            "      }\n" +
                                            "      .btn a {\n" +
                                            "        font-size: 16px !important;\n" +
                                            "        max-width: 100% !important;\n" +
                                            "        width: 100% !important;\n" +
                                            "      }\n" +
                                            "    }\n" +
                                            "    /* -------------------------------------\n" +
                                            "    PRESERVE THESE STYLES IN THE HEAD\n" +
                                            "------------------------------------- */\n" +
                                            "    \n" +
                                            "    @media all {\n" +
                                            "      .ExternalClass {\n" +
                                            "        width: 100%;\n" +
                                            "      }\n" +
                                            "      .ExternalClass,\n" +
                                            "      .ExternalClass p,\n" +
                                            "      .ExternalClass span,\n" +
                                            "      .ExternalClass font,\n" +
                                            "      .ExternalClass td,\n" +
                                            "      .ExternalClass div {\n" +
                                            "        line-height: 100%;\n" +
                                            "      }\n" +
                                            "      .apple-link a {\n" +
                                            "        color: inherit !important;\n" +
                                            "        font-family: inherit !important;\n" +
                                            "        font-size: inherit !important;\n" +
                                            "        font-weight: inherit !important;\n" +
                                            "        line-height: inherit !important;\n" +
                                            "        text-decoration: none !important;\n" +
                                            "      }\n" +
                                            "      #MessageViewBody a {\n" +
                                            "        color: inherit;\n" +
                                            "        text-decoration: none;\n" +
                                            "        font-size: inherit;\n" +
                                            "        font-family: inherit;\n" +
                                            "        font-weight: inherit;\n" +
                                            "        line-height: inherit;\n" +
                                            "      }\n" +
                                            "    }\n" +
                                            "    </style>\n" +
                                            "  </head>\n" +
                                            "  <body>\n" +
                                            "    <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"body\">\n" +
                                            "      <tbody><tr>\n" +
                                            "        <td>&nbsp;</td>\n" +
                                            "        <td class=\"container\">\n" +
                                            "          <div class=\"content\">\n" +
                                            "\n" +
                                            "            <!-- START CENTERED WHITE CONTAINER -->\n" +
                                            "            <span class=\"preheader\">This is preheader text. Some clients will show this text as a preview.</span>\n" +
                                            "            <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"main\">\n" +
                                            "\n" +
                                            "              <!-- START MAIN CONTENT AREA -->\n" +
                                            "              <tbody><tr>\n" +
                                            "                <td class=\"wrapper\">\n" +
                                            "                  <p>Gracias por su donacion!</p>\n" +
                                            "                  <p>De parte de los miembros de la AITEL le agradecemos su apoyo a la Fibra Tóxica</p>\n" +
                                            "                  \n" +
                                            "                  <p>Nos compremetemos a utilizar este apoyo económico para poder apoyar a la facultad.</p>\n" +
                                            "                  <p>Asociación de Estudiantes de las Telecomunicaciones.</p>\n" +
                                            "                </td>\n" +
                                            "              </tr>\n" +
                                            "\n" +
                                            "              <!-- END MAIN CONTENT AREA -->\n" +
                                            "              </tbody></table>\n" +
                                            "\n" +
                                            "            <!-- START FOOTER -->\n" +
                                            "            <div class=\"footer\">\n" +
                                            "              <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                                            "                <tbody><tr>\n" +
                                            "                  <td class=\"content-block\">\n" +
                                            "                    <span class=\"apple-link\">Servicios de IOT.</span>\n" +
                                            "                    <br>PUCP</td>\n" +
                                            "                </tr>\n" +
                                            "                <tr>\n" +
                                            "                  <td class=\"content-block powered-by\">\n" +
                                            "                  San Miguel, Lima, Peru 2023 \n" +
                                            "                  </td>\n" +
                                            "                </tr>\n" +
                                            "              </tbody></table>\n" +
                                            "            </div>\n" +
                                            "\n" +
                                            "            <!-- END FOOTER -->\n" +
                                            "            \n" +
                                            "<!-- END CENTERED WHITE CONTAINER --></div>\n" +
                                            "        </td>\n" +
                                            "        <td>&nbsp;</td>\n" +
                                            "      </tr>\n" +
                                            "    </tbody></table>\n" +
                                            "  \n" +
                                            "</body></html>";
                                    buttonSendEmail(user.getCorreo(),emailcontent);
                                }


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("msg-test","error correo");
                        }
                    });




                    Intent intent1 = new Intent(DetailDonationActivity.this, ValidarDonacionesActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent1);
                    finish();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("msg-test", String.valueOf(databaseError));
                }
            });
        });




    }


    public void buttonSendEmail(String correoaEnviar, String text){

        try {
            String stringSenderEmail = "telesystemclinic@gmail.com";
            String stringReceiverEmail = correoaEnviar;
            String stringPasswordSenderEmail = "qyxm eonv gmql dqak";

            String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));




            mimeMessage.setSubject("Donacion Procesada");
            mimeMessage.setContent(text,"text/html; charset=utf-8");

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                        //Toast.makeText(getContext(),"Correo enviado",Toast.LENGTH_SHORT).show();
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        Log.d("msg-test", String.valueOf(e));
                    }
                }
            });
            thread.start();

        } catch (AddressException e) {
            e.printStackTrace();
            Log.d("msg-test", String.valueOf(e));
        } catch (MessagingException e) {
            e.printStackTrace();
            Log.d("msg-test", String.valueOf(e));
        }
    }



}