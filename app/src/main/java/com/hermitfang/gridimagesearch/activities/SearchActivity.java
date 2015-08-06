package com.hermitfang.gridimagesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.hermitfang.gridimagesearch.listeners.EndlessScrollListener;
import com.hermitfang.gridimagesearch.R;
import com.hermitfang.gridimagesearch.adapters.ImageResultsAdapter;
import com.hermitfang.gridimagesearch.model.ImageResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity {
    private static final int imagePerBatch = 8;
    private static final int maxBatch = 64 / imagePerBatch; // Google api returns maximun 64 images

    private int currentBatch;
    private EditText etQeury;
    private GridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter adapterImageResults;

    private String paramImageSize;
    private String paramColorFilter;
    private String paramImageType;
    private String paramSite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // display action bar icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setContentView(R.layout.activity_search);
        setupViews();
        imageResults = new ArrayList<ImageResult>();
        adapterImageResults = new ImageResultsAdapter(this, imageResults);
        currentBatch = 0;

        paramImageSize = "";
        paramColorFilter = "";
        paramImageType = "";
        paramSite = "";

        // Set adapter to GridView
        gvResults.setAdapter(adapterImageResults);
        // Set scroll listener to GridView
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (currentBatch < maxBatch) {
                    loadDataFromApi(++currentBatch);
                } else {
                    Toast.makeText(getApplicationContext(), "No more items", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void loadDataFromApi(int offset) {
        //calling Google API
        //url https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=android&rsz=8
        // additional params:
            // rsz (request size): request size
            // q (query): just query
            // start (start from 0): just start from
            // imgsz(image size): icon|small|medium|large|xlarge|xxlarge|huge
            // imgcolor (image color): black|blue|brown|gray|green|orange|pink|purple|red|teal|white|yellow
            // imgtype (image type): face|photo|clipart|lineart
            // as_sitesearch (site search): somewhere.com

        // String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=" + imagePerBatch;
        String baseUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=" + imagePerBatch;
        // add query
        baseUrl = baseUrl + "&q=" + etQeury.getText().toString();
        // add start from
        baseUrl = baseUrl + "&start=" + (offset * imagePerBatch);
        // add image size if exists
        if (!paramImageSize.equals("")) {
            baseUrl = baseUrl + "&imgsz=" + paramImageSize;
        }
        // add image color filter if exists
        if (!paramColorFilter.equals("")) {
            baseUrl = baseUrl + "&imgcolor=" + paramColorFilter;
        }
        // add image type if exists
        if (!paramImageType.equals("")) {
            baseUrl = baseUrl + "&imgtype=" + paramImageType;
        }
        // add site filter if exists
        if (!paramSite.equals("")) {
            baseUrl = baseUrl + "&as_sitesearch=" + paramSite;
        }

        if (offset == 0) {
            Toast.makeText(this, "Loading API: " + baseUrl, Toast.LENGTH_SHORT).show();
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(baseUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Log.i("DEBUG", response.toString());
                JSONArray imageResultJ = null;
                try {
                    imageResultJ = response.getJSONObject("responseData").getJSONArray("results");
                    if (currentBatch == 0) {
                        imageResults.clear();
                    }
                    // imageResults.addAll(ImageResult.fromJSONArray(imageResultJ));
                    // adapterImageResults.notifyDataSetChanged();
                    adapterImageResults.addAll(ImageResult.fromJSONArray(imageResultJ));

                    if (currentBatch == 0) {
                        loadDataFromApi(++currentBatch); // load more than 15 items to make screen scrollable
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Log.i("INFO:", imageResults.toString());
            }
        });
    }

    private void setupViews() {
        etQeury = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // create intent
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                // get image to display
                ImageResult result = imageResults.get(position);
                // put image to intent
                i.putExtra("result", result);
                // launch new activity
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    public void onSearchMenuAction(MenuItem mi) {
        // create intent
        Intent i = new Intent(SearchActivity.this, SearchFilterActivity.class);
        // send data by extra
        i.putExtra("paramImageSize", paramImageSize);
        i.putExtra("paramColorFilter", paramColorFilter);
        i.putExtra("paramImageType", paramImageType);
        i.putExtra("paramSite", paramSite);
        // launch new activity
        startActivityForResult(i, 200);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == 200 && resultCode == RESULT_OK) {
            paramImageSize = result.getStringExtra("pickedSize");
            paramColorFilter = result.getStringExtra("pickedColor");
            paramImageType = result.getStringExtra("pickedType");
            paramSite = result.getStringExtra("pickedSite");
        }
    }

    public void onImageSearch(View v) {
        currentBatch = 0;
        loadDataFromApi(currentBatch);
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
}
