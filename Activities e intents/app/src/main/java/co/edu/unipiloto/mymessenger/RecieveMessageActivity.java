package co.edu.unipiloto.mymessenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.ArrayList;

public class RecieveMessageActivity extends Activity {
    private EditText messageView;
    private ArrayList<String> messages;
    private ArrayAdapter<String> adapter;
    private ChatDatabase dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve_message);

        dbHelper = new ChatDatabase(this);
        messageView = findViewById(R.id.message);
        Button sendButton = findViewById(R.id.send_button);
        ListView chatListView = findViewById(R.id.chat_list);

        messages = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messages);
        chatListView.setAdapter(adapter);

        // Cargar mensajes desde SQLite
        loadMessagesFromDatabase();

        // Recibir mensaje del propietario sin duplicarlo
        Intent intent = getIntent();
        String receivedMessage = intent.getStringExtra("message");

        if (receivedMessage != null && !dbHelper.messageExists(receivedMessage, "Propietario")) {
            dbHelper.insertMessage("Propietario", receivedMessage);
            loadMessagesFromDatabase();
        }

        sendButton.setOnClickListener(v -> onSendMessage());

    }

    private void loadMessagesFromDatabase() {
        messages.clear(); // Limpiar lista antes de recargar
        messages.addAll(dbHelper.getAllMessages()); // Obtener mensajes desde SQLite
        adapter.notifyDataSetChanged(); // Notificar cambios al adaptador
    }

    public void onSendMessage() {
        String messageText = messageView.getText().toString().trim();
        if (!messageText.isEmpty()) {
            dbHelper.insertMessage("Cuidador", messageText);
            loadMessagesFromDatabase(); // Recargar mensajes para reflejar el nuevo mensaje
            messageView.setText("");

            // Enviar mensaje de vuelta al propietario
            Intent intent = new Intent(this, CreateMessageActivity.class);
            intent.putExtra("message", messageText);
            startActivity(intent);
        }
    }
}
