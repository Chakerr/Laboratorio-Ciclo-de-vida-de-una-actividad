package unipiloto.edu.stopwatch;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private Button startButton, lapButton, resetButton;
    private TextView lapTimes;
    private boolean running;
    private long pauseOffset;
    private int lapCount = 0;
    private static final int MAX_LAPS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chronometer = findViewById(R.id.chronometer);
        startButton = findViewById(R.id.startButton);
        lapButton = findViewById(R.id.lapButton);
        resetButton = findViewById(R.id.resetButton);
        lapTimes = findViewById(R.id.lapTimes);

        startButton.setOnClickListener(v -> {
            if (!running) {
                chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                chronometer.start();
                running = true;
                startButton.setText("Pausar");
            } else {
                chronometer.stop();
                pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                running = false;
                startButton.setText("Iniciar");
            }
        });

        lapButton.setOnClickListener(v -> {
            if (running && lapCount < MAX_LAPS) {
                long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                String lapText = "Vuelta " + (lapCount + 1) + ": " + (elapsedMillis / 1000) + " segundos\n";
                lapTimes.append(lapText);
                lapCount++;
            }
        });

        resetButton.setOnClickListener(v -> {
            chronometer.setBase(SystemClock.elapsedRealtime());
            pauseOffset = 0;
            lapTimes.setText("");
            lapCount = 0;
            if (running) {
                chronometer.stop();
                running = false;
                startButton.setText("Iniciar");
            }
        });
    }
}
