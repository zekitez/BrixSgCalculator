package com.zekitez.brixsgcalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
// import android.util.Log;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.text.DecimalFormat;

// Lincoln equation   https://www.homebrewtalk.com/threads/how-do-you-convert-brix-to-sg.415358/   ajdelange
// Brewers Friend     https://www.brewersfriend.com/brix-converter/
// Winning HomeBrew   https://winning-homebrew.com/specific-gravity-to-brix.html
// EU CEE 2676/90     https://eur-lex.europa.eu/legal-content/en/ALL/?uri=CELEX%3A31990R2676

// https://www.wijngildepeelenmaas.nl/wine-alcohol.html
// https://nl.quora.com/Hoe-kun-je-de-volumetoename-berekenen-van-1-liter-water-nadat-er-450-gram-bietsuiker-in-werd-opgelost-Ik-deed-een-praktijktest-en-het-resultaat-was-een-volumetoename-van-300-ml

public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity";

    final String INPUTS_AS_PREFS = "InputValues";

    public GlobalFunctions globalFunctions;

    private TextView textSg, textBrix;
    private EditText inputBrix, inputSg, inputVolumeJuice, inputDesVolAlcohol;
    private RadioButton radioButtonUseBrix, radioButtonUseSg;
    private final FromToBrixSg tableBrixSgAlc = new FromToBrixSg();

    private float sugarBrix = 0, sugarSg = 0, volume = 0, desiredAlcohol = 0;
    private String convMethod = "eu", convTitle = "", solvMethod = "1:1", solvTitle = "" ;
    private int sw_sugar = 1587;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        globalFunctions = (GlobalFunctions) getApplicationContext();
        getPrefs(false);
        setContentView(R.layout.activity_main);

        textBrix = findViewById(R.id.textBrix);
        textSg = findViewById(R.id.textSg);

        inputBrix = findViewById(R.id.inputBrix);
        inputSg = findViewById(R.id.inputSg);
        inputVolumeJuice = findViewById(R.id.inputVolumeJuice);
        inputDesVolAlcohol = findViewById(R.id.inputDesVolAlcohol);
        RadioGroup radioGroupBrixSg = findViewById(R.id.RadioGroupBrixSg);
        radioButtonUseBrix = findViewById(R.id.radioButtonUseBrix);
        radioButtonUseSg = findViewById(R.id.radioButtonUseSg);

        final EditText.OnEditorActionListener inputBrixListener = (v, actionId, event) -> {
            inputBrixCalculations();
            return false;
        };

        final EditText.OnEditorActionListener inputSgListener = (arg0, actionId, event) -> {
            inputSgCalculations();
            return false;
        };

        inputBrix.setOnEditorActionListener(inputBrixListener);
        inputSg.setOnEditorActionListener(inputSgListener);

        radioGroupBrixSg.setOnCheckedChangeListener((group, checkedId) -> {
//            Log.d(TAG,"onCheckedChanged");
            updateFields();
        });

        inputVolumeJuice.setOnEditorActionListener((arg0, actionId, event) -> {
            if (inputVolumeJuice.getText().length() > 0) {
                try {
                    volume = Float.parseFloat(inputVolumeJuice.getText().toString());
                    updateFields();
                    inputVolumeJuice.setTextColor(getResources().getColor(R.color.black));
                } catch (Exception e) {
                    e.printStackTrace();
                    inputVolumeJuice.setTextColor(getResources().getColor(R.color.red));
                }
            }
            return false;
        });

        inputDesVolAlcohol.setOnEditorActionListener((arg0, actionId, event) -> {
            if (inputDesVolAlcohol.getText().length() > 0) {
                try {
                    desiredAlcohol = Float.parseFloat(inputDesVolAlcohol.getText().toString());
                    updateFields();
                    inputDesVolAlcohol.setTextColor(getResources().getColor(R.color.black));
                } catch (Exception e) {
                    e.printStackTrace();
                    inputDesVolAlcohol.setTextColor(getResources().getColor(R.color.red));
                }
            }
            return false;
        });

    }

    private void inputBrixCalculations() {
        if (inputBrix.getText().length() > 0) {
            try {
                float brix = Float.parseFloat(inputBrix.getText().toString());
                float sgLe = (float) ((668.0 - Math.sqrt(668 * 668 - 820 * (463 + brix))) / 410.0);  // Lincoln equation
                float sgBf = (float) ((brix / (258.6 - ((brix / 258.2) * 227.1))) + 1.0);            // BrewersFriend
                float sgWh = tableBrixSgAlc.findSgByBrix(brix);                                      // Winning HomeBrew
                float sgEu = tableBrixSgAlc.findEuSgByBrix(brix);
                float sgAverage = (sgLe + sgBf + sgWh + sgEu) / 4.0f;
                float alcohol = Math.max((float) (0.6 * brix - 1.0), 0.0f);

                Log.d(TAG,"BRIX " + brix+" " +sgLe+" " +sgBf+" " +sgWh+" " + sgEu+" " +sgAverage+" " + alcohol );

                if (convMethod.equals(getResources().getString(R.string.pref_conv_le_value))) {
                    ((TextView) findViewById(R.id.textBrix2Sg)).setText(formatFloat(sgLe, 3));

                } else if (convMethod.equals(getResources().getString(R.string.pref_conv_wh_value))) {
                    ((TextView) findViewById(R.id.textBrix2Sg)).setText(formatFloat(sgWh, 3));

                } else if (convMethod.equals(getResources().getString(R.string.pref_conv_bf_value))) {
                    ((TextView) findViewById(R.id.textBrix2Sg)).setText(formatFloat(sgBf, 3));

                } else if (convMethod.equals(getResources().getString(R.string.pref_conv_eu_value))){
                    ((TextView) findViewById(R.id.textBrix2Sg)).setText(formatFloat(sgEu, 3));
                } else {
                    ((TextView) findViewById(R.id.textBrix2Sg)).setText(formatFloat(sgAverage, 3));
                }

                ((TextView) findViewById(R.id.textBrix2VolAlc)).setText(formatFloat(alcohol, 1));
                sugarBrix = tableBrixSgAlc.findEuSugarByBrix(brix);
                ((TextView) findViewById(R.id.textEuBrix2Sugar)).setText(formatFloat(sugarBrix, 1));
                ((TextView) findViewById(R.id.textEuBrix2VolAlc)).setText(formatFloat(tableBrixSgAlc.findEuAlcByBrix(brix), 1));
                updateFields();
                inputBrix.setTextColor(getResources().getColor(R.color.black));
                // Log.d(" Brix", "="+brix+" sg="+sg+" abv="+abv);
            } catch (Exception e) {
                e.printStackTrace();
                inputBrix.setTextColor(getResources().getColor(R.color.red));
            }
        }
    }


    private void inputSgCalculations() {
        if (inputSg.getText().length() > 0) {
            try {
                float sg = Float.parseFloat(inputSg.getText().toString());
                float brixLe = (float) ((463.0 - 205 * sg) * (sg - 1.0));                                          // Lincoln equation
                float brixBf = (float) ((((182.4601 * sg - 775.6821) * sg + 1262.7794) * sg - 669.5622));  // BrewersFriend
                float brixWh = tableBrixSgAlc.findBrixBySg(sg);                                            // Winning HomeBrew
                float brixEu = tableBrixSgAlc.findEuBrixBySg(sg);
                float brixAverage = (brixLe + brixBf + brixWh + brixEu) / 4.0f;
                float alcohol = Math.max((float) (0.6 * brixAverage - 1.0), 0.0f);  // Why '-1' ?
                //float abv = (float) (0.6 * (brix+brixBf+brixHb)/3.0);
                Log.d(TAG,"SG " + sg+" " +brixLe+" " +brixBf+" " +brixWh+" " + brixEu+" " +brixAverage+" " + alcohol );

                if (convMethod.equals(getResources().getString(R.string.pref_conv_le_value))) {
                    ((TextView) findViewById(R.id.textSg2Brix)).setText(formatFloat(brixLe, 1));

                } else if (convMethod.equals(getResources().getString(R.string.pref_conv_wh_value))) {
                    ((TextView) findViewById(R.id.textSg2Brix)).setText(formatFloat(brixWh, 1));

                } else if (convMethod.equals(getResources().getString(R.string.pref_conv_bf_value))) {
                    ((TextView) findViewById(R.id.textSg2Brix)).setText(formatFloat(brixBf, 1));

                } else if (convMethod.equals(getResources().getString(R.string.pref_conv_eu_value))){
                    ((TextView) findViewById(R.id.textSg2Brix)).setText(formatFloat(brixEu, 1));

                } else {
                    ((TextView) findViewById(R.id.textSg2Brix)).setText(formatFloat(brixAverage, 1));
                }

                ((TextView) findViewById(R.id.textSg2VolAlc)).setText(formatFloat(alcohol, 1));
                sugarSg = tableBrixSgAlc.findEuSugarBySg(sg);
                ((TextView) findViewById(R.id.textEuSg2Sugar)).setText(formatFloat(sugarSg, 1));
                ((TextView) findViewById(R.id.textEuSg2VolAlc)).setText(formatFloat(tableBrixSgAlc.findEuAlcBySg(sg), 1));
                updateFields();
                inputSg.setTextColor(getResources().getColor(R.color.black));
                // Log.d(" Sg", "="+sg+" Brix="+brix+" abv="+abv);
            } catch (Exception e) {
                e.printStackTrace();
                inputSg.setTextColor(getResources().getColor(R.color.red));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPrefs(true);
        String txt = getString(R.string.txt_brix) + " (" + convTitle + ")";
        textBrix.setText(txt);
        txt = getString(R.string.txt_sg) + " (" + convTitle + ")";
        textSg.setText(txt);
        loadSavedInputs();
        inputBrixCalculations();
        inputSgCalculations();
        updateFields();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveInputs();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
        Process.killProcess(Process.myPid());
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Exit app")
//                .setMessage(R.string.txt_are_you_sure_exit)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                    MainActivity.super.onBackPressed();
                }).create().show();
    }

    private void updateFields(){
        float desiredSugarGrL = tableBrixSgAlc.findEuSugarByAlc(desiredAlcohol);
        float deltaSugarGrL = 0, startSugarGrL = 0;
        ((TextView) findViewById(R.id.textReqSugar)).setText(formatFloat(desiredSugarGrL,2));
        ((TextView) findViewById(R.id.textReqBrix)).setText(formatFloat(tableBrixSgAlc.findEuBrixByAlc(desiredAlcohol),2));
        ((TextView) findViewById(R.id.textReqSg)).setText(formatFloat(tableBrixSgAlc.findEuSgByAlc(desiredAlcohol),4));

        if (radioButtonUseBrix.isChecked() ) {
            deltaSugarGrL = Math.max(desiredSugarGrL - sugarBrix, 0.0f);
            startSugarGrL = sugarBrix;

        } else if (radioButtonUseSg.isChecked() ) {
            deltaSugarGrL = Math.max(desiredSugarGrL - sugarSg, 0.0f);
            startSugarGrL = sugarSg;
        }
//        Log.d(TAG,"updateFields vol="+ volume + " sugar:" + deltaSugarGrL);

        ((TextView) findViewById(R.id.textMissingSugar)).setText(formatFloat(deltaSugarGrL, 1));
        if (volume > 0 && deltaSugarGrL > 0) {
            float addSugarGr = calculateSugarToAdd(volume, startSugarGrL, desiredSugarGrL);
            ((TextView) findViewById(R.id.textMissingSugarGram)).setText(formatFloat(addSugarGr, 1));

            ((TextView) findViewById(R.id.textSugarInWaterLtr)).setText(solvTitle);

            if (solvMethod.equals(getResources().getString(R.string.pref_solv_121_value))) {
                ((TextView) findViewById(R.id.textSugarInWaterGram)).setText(formatFloat(1000 * addSugarGr/(sw_sugar), 0));
                ((TextView) findViewById(R.id.textTotalVolume)).setText(formatFloat(volume + 2 * addSugarGr/sw_sugar, 3));

            } else {  // none
                ((TextView) findViewById(R.id.textSugarInWaterGram)).setText(formatFloat(0, 0));
                ((TextView) findViewById(R.id.textTotalVolume)).setText(formatFloat(volume + addSugarGr/sw_sugar, 3));
            }
        }
    }


    private float calculateSugarToAdd(float startVolumeL, float startSugarGrL, float desiredSugarGrL){

//        Log.d(TAG,"calculateSugarToAdd  " + startVolumeL + " " + startSugarGrL + " " + desiredSugarGrL );
        if (startSugarGrL >= desiredSugarGrL) return 0;

        float startSugarGr = startSugarGrL * startVolumeL;
        float deltaSugarGr = startVolumeL * (desiredSugarGrL - startSugarGrL);
        float deltaSugarVolumeL = deltaSugarGr / sw_sugar;

        float totalSugarGr = startSugarGr + deltaSugarGr;

        float totalVolumeL;
        if (solvMethod.equals(getResources().getString(R.string.pref_solv_121_value))) {
            totalVolumeL = startVolumeL + deltaSugarVolumeL * 2.0f;           // Dissolve in water 1:1
        } else {
            totalVolumeL = startVolumeL + deltaSugarVolumeL;                  // Dissolve in the juice
        }

        float calculatedSugarGrl = totalSugarGr / totalVolumeL;

        if ((desiredSugarGrL - calculatedSugarGrl) > 0.10f ){
            deltaSugarGr = deltaSugarGr + calculateSugarToAdd(totalVolumeL, calculatedSugarGrl, desiredSugarGrL);
        }
//        Log.d(TAG,"calculateSugarToAdd  " + deltaSugarGr + " " + calculatedSugarGrl);
        return deltaSugarGr;
    }

    //==============================

    public String formatFloat(float value, int digits){
        DecimalFormat decimalFormat;
        StringBuilder pattern = new StringBuilder("0");
        if (digits > 0) {
            pattern.append(".0");
        }
        while ( digits > 1 ){
            pattern.append("0");
            digits = digits - 1 ;
        }
        decimalFormat = new DecimalFormat(pattern.toString());
        return decimalFormat.format(value);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_prefs) {
            Intent intent = new Intent(this, PrefsActivity.class);
            startActivity(intent);
            return (true);

        } else if (item.getItemId() == R.id.action_disclaimer){
            new AcceptDeclineDialog(MainActivity.this, R.layout.disclaimer, globalFunctions.getDisclaimerTxt());

        } else if (item.getItemId() == R.id.action_privacy_policy){
            new AcceptDeclineDialog(MainActivity.this, R.layout.privacy_policy, globalFunctions.getPrivicyPolicyTxt());
        }
        return (super.onOptionsItemSelected(item));
    }

    private void getPrefs(boolean askAcceptance) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        sw_sugar = Integer.parseInt(prefs.getString(getString(R.string.key_sw_sugar), "1587"));
        convMethod = prefs.getString(getString(R.string.key_convMethod), getString(R.string.pref_conv_eu_value));
        solvMethod = prefs.getString(getString(R.string.key_solvMethod), getString(R.string.pref_solv_121_value));

        if (convMethod.equals(getResources().getString(R.string.pref_conv_le_value))) {
            convTitle = getResources().getString(R.string.pref_conv_label_le);

        } else if(convMethod.equals(getResources().getString(R.string.pref_conv_wh_value))) {
            convTitle = getResources().getString(R.string.pref_conv_label_wh);

        } else if (convMethod.equals(getResources().getString(R.string.pref_conv_bf_value))) {
            convTitle = getResources().getString(R.string.pref_conv_label_bf);

        } else if (convMethod.equals(getResources().getString(R.string.pref_conv_eu_value))){
            convTitle = getResources().getString(R.string.pref_conv_label_eu);

        } else {
            convTitle = getResources().getString(R.string.pref_conv_label_av);
        }

        if(solvMethod.equals(getResources().getString(R.string.pref_solv_none_value))) {
            solvTitle = getResources().getString(R.string.pref_solv_label_none);

        } else {
            solvTitle = getResources().getString(R.string.pref_solv_label_121);
        }

        if (askAcceptance) {
            boolean value = prefs.getBoolean(getString(R.string.key_privacyPolycyAccepted), false);
            if (value == false) {
                new AcceptDeclineDialog(MainActivity.this, R.layout.privacy_policy, globalFunctions.getPrivicyPolicyTxt());
            }
            value = prefs.getBoolean(getString(R.string.key_disclaimerAccepted), false);
            if (value == false) {
                new AcceptDeclineDialog(MainActivity.this, R.layout.disclaimer, globalFunctions.getDisclaimerTxt());
            }
        }

    }

    private void loadSavedInputs() {
        SharedPreferences inputs = getSharedPreferences(INPUTS_AS_PREFS, 0);

        inputBrix.setText(inputs.getString(getString(R.string.key_InputBrix), "0.0"));
        inputSg.setText(inputs.getString(getString(R.string.key_InputSg), "1.0"));
        inputDesVolAlcohol.setText(inputs.getString(getString(R.string.key_InputDesVolAlcohol), "10.0"));
        inputVolumeJuice.setText(inputs.getString(getString(R.string.key_InputVolumeJuice), "0.0"));

        volume = Float.parseFloat(inputVolumeJuice.getText().toString());
        desiredAlcohol = Float.parseFloat(inputDesVolAlcohol.getText().toString());
    }

    private void saveInputs() {
        SharedPreferences inputs = getSharedPreferences(INPUTS_AS_PREFS, 0);
        SharedPreferences.Editor editor = inputs.edit();

        editor.putString(getString(R.string.key_InputBrix), inputBrix.getText().toString());
        editor.putString(getString(R.string.key_InputSg), inputSg.getText().toString());
        editor.putString(getString(R.string.key_InputDesVolAlcohol), inputDesVolAlcohol.getText().toString());
        editor.putString(getString(R.string.key_InputVolumeJuice), inputVolumeJuice.getText().toString());
        
        editor.commit();
    }

}
