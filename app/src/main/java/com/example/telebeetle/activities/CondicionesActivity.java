package com.example.telebeetle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityCondicionesBinding;
import com.example.telebeetle.databinding.ActivityRegisterBinding;

public class CondicionesActivity extends AppCompatActivity {

    ActivityCondicionesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCondicionesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.textView35.setText("Términos y Condiciones de Uso de Telebeetle\n" +
                "\n" +
                "Fecha de la última actualización: 18/12/22\n" +
                "\n" +
                "1. Aceptación de los Términos\n" +
                "\n" +
                "Al descargar, instalar o utilizar la aplicación TeleBeetle, aceptas cumplir con estos términos y condiciones de uso. Si no estás de acuerdo con alguno de los términos, por favor, no utilices la aplicación.\n" +
                "\n" +
                "2. Uso de la Aplicación\n" +
                "\n" +
                "2.1 Licencia de Uso: Se concede una licencia limitada, no exclusiva, intransferible y revocable para utilizar la aplicación con fines personales y no comerciales.\n" +
                "\n" +
                "2.2 Restricciones de Uso: No debes copiar, modificar, distribuir, vender o realizar ingeniería inversa de la aplicación. Además, te comprometes a no utilizar la aplicación de manera que infrinja leyes locales, nacionales o internacionales.\n" +
                "\n" +
                "3. Datos del Usuario\n" +
                "\n" +
                "3.1 Privacidad: El uso de la aplicación está sujeto a nuestra Política de Privacidad, que se encuentra disponible telebeetle.com.\n" +
                "\n" +
                "3.2 Registro: Algunas funciones de la aplicación pueden requerir el registro del usuario. Aceptas proporcionar información precisa y mantener la confidencialidad de tus credenciales.\n" +
                "\n" +
                "4. Contenido del Usuario\n" +
                "\n" +
                "4.1 Responsabilidad: Eres responsable de cualquier contenido que publiques o compartas a través de la aplicación y te comprometes a no publicar contenido que viole derechos de terceros.\n" +
                "\n" +
                "4.2 Eliminación de Contenido: Nos reservamos el derecho de eliminar o modificar cualquier contenido que consideremos, a nuestra entera discreción, que infringe estos términos.\n" +
                "\n" +
                "5. Modificaciones de los Términos\n" +
                "\n" +
                "Nos reservamos el derecho de modificar estos términos en cualquier momento. Las modificaciones entrarán en vigor inmediatamente después de su publicación. Se te notificará sobre las modificaciones a través de la aplicación o por otros medios.\n" +
                "\n" +
                "6. Limitación de Responsabilidad\n" +
                "\n" +
                "En la medida permitida por la ley aplicable, renunciamos a cualquier responsabilidad por daños directos, indirectos, incidentales o consecuentes que puedan surgir del uso de la aplicación.\n" +
                "\n" +
                "7. Ley Aplicable\n" +
                "\n" +
                "Estos términos y condiciones se rigen por las leyes Peruanas y cualquier disputa se someterá a la jurisdicción exclusiva de los tribunales Constitucionales.\n" +
                "\n" +
                "8. Contacto\n" +
                "\n" +
                "Para cualquier pregunta o preocupación relacionada con estos términos y condiciones, puedes contactarnos a través de telebeetle.com.\n" +
                "\n" );




    }
}