package com.hermitfang.gridimagesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.hermitfang.gridimagesearch.R;
import com.hermitfang.gridimagesearch.listeners.CustomOnItemSelectedListener;


public class SearchFilterActivity extends ActionBarActivity {

    private Spinner spImageSize;
    private Spinner spColorFilter;
    private Spinner spImageType;
    private EditText etSiteFilter;

    // private final String[] paths = {"small", "medium", "large", "extra-large"};
    public static String pickedSize;
    public static String pickedColor;
    public static String pickedType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter);

        spImageSize = (Spinner)findViewById(R.id.spImageSize);
        spColorFilter = (Spinner)findViewById(R.id.spColorFilter);
        spImageType = (Spinner)findViewById(R.id.spImageType);
        etSiteFilter = (EditText)findViewById(R.id.etSiteFilter);

        // get data from Intent
        String imageSize = getIntent().getStringExtra("paramImageSize");
        String colorFilter = getIntent().getStringExtra("paramColorFilter");
        String imageType = getIntent().getStringExtra("paramImageType");
        String siteFilter = getIntent().getStringExtra("paramSite");

        // init filter values
        ArrayAdapter adapter;
        int pos;
        // set image size
        adapter = (ArrayAdapter) spImageSize.getAdapter();
        pos = adapter.getPosition(imageSize);
        spImageSize.setSelection(pos);
        // set color filter
        adapter = (ArrayAdapter) spColorFilter.getAdapter();
        pos = adapter.getPosition(colorFilter);
        spColorFilter.setSelection(pos);
        // set image type
        adapter = (ArrayAdapter) spImageType.getAdapter();
        pos = adapter.getPosition(imageType);
        spImageType.setSelection(pos);
        // set site filter
        etSiteFilter.setText(siteFilter);

        /* Init Spinners by code (for reference)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spImageSize.setAdapter(adapter);
        */

        // set listeners
        spImageSize.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        spColorFilter.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        spImageType.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_filter, menu);
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

    public void onSaveClick(View v) {
        // Toast.makeText(this, pickedSize, Toast.LENGTH_SHORT).show();
        Intent result = new Intent();
        result.putExtra("pickedSize", pickedSize);
        result.putExtra("pickedColor", pickedColor);
        result.putExtra("pickedType", pickedType);
        result.putExtra("pickedSite", etSiteFilter.getText().toString());

        setResult(RESULT_OK, result);
        finish();
    }
}
