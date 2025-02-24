package co.edu.unipiloto.mymessenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.ArrayList;

public class CreateMessageActivity extends Activity {
    private EditText messageView;
    private ArrayList<String> messages;
    private ArrayAdapter<String> adapter;
    private ChatDatabase dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);

        dbHelper = new ChatDatabase(this);
        messageView = findViewById(R.id.message);
        Button sendButton = findViewById(R.id.send_button);
        Button deleteDbButton = findViewById(R.id.delete_db_button);
        ListView chatListView = findViewById(R.id.chat_list);

        // Cargar historial de mensajes desde SQLite
        messages = dbHelper.getAllMessages();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messages);
        chatListView.setAdapter(adapter);

        sendButton.setOnClickListener(v -> onSendMessage());

        // Botón para eliminar la base de datos (Opcional)
        deleteDbButton.setOnClickListener(v -> {
            this.deleteDatabase("chat.db"); // Borra la base de datos
            messages.clear(); // Limpia la lista de mensajes en la UI
            adapter.notifyDataSetChanged(); // Notifica los cambios a la lista
        });
    }

    public void onSendMessage() {
        String messageText = messageView.getText().toString().trim(); // Se usa trim() para evitar espacios vacíos
        if (!messageText.isEmpty()) {
            dbHelper.insertMessage("Propietario", messageText);
            messages.add("Propietario: " + messageText);
            adapter.notifyDataSetChanged();
            messageView.setText("");

            // Enviar mensaje a la otra actividad
            Intent intent = new Intent(this, RecieveMessageActivity.class);
            intent.putExtra("message", messageText);
            startActivity(intent);
        }
    }
}
