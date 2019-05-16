package com.example.testingraspconn;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.testingraspconn.websockets.WebSocketConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends Activity {

    private MCP3008 mMCP3008;
    private Handler mHandler;
    private DetectionService detectionService;
    private WebSocketConnection webSocketConnection;
    private final String url = "http://172.30.118.103:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        webSocketConnection = new WebSocketConnection(url);
        webSocketConnection.start();

        detectionService = new DetectionService();

        try {
            mMCP3008 = new MCP3008("BCM25", "BCM18", "BCM24", "BCM23");
            mMCP3008.register();
        } catch( IOException e ) {
            Log.e("MCP3008", "MCP initialization exception occurred: " + e.getMessage());
        }

        mHandler = new Handler();
        mHandler.post(mReadAdcRunnable);
    }

    private Runnable mReadAdcRunnable = new Runnable() {

        private static final long DELAY_MS = 3000L; // 3 seconds

        @Override
        public void run() {
            if (mMCP3008 == null) {
                return;
            }
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            try {
                int sensorValue = mMCP3008.readAdc(0x0);

                DetectedGas detectedGas = detectionService.ConvertPPM(sensorValue);
                Gson gson = new Gson();
                String json = gson.toJson(detectedGas);//  ow.writeValueAsString(detectedGas);
                webSocketConnection.send(json);

                Log.e("MCP3008", "ADC 0 Default: " + sensorValue);
            } catch( IOException e ) {
                Log.e("MCP3008", "Something went wrong while reading from the ADC: " + e.getMessage());
            }

            mHandler.postDelayed(this, DELAY_MS);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if( mMCP3008 != null ) {
            mMCP3008.unregister();
        }

        if( mHandler != null ) {
            mHandler.removeCallbacks(mReadAdcRunnable);
        }
    }
}
