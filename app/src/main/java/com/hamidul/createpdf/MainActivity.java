package com.hamidul.createpdf;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    final static int REQUEST_CODE_STORAGE_PERMISSION = 1235;
    Button btnCreatePdf;
    Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreatePdf = findViewById(R.id.btnCreatePdf);

        btnCreatePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
                    setToast("Allow Permission");
                } else {
                    convertXMLtoPDF(); // Permission already granted
                }
            }
        });


    }

    private void convertXMLtoPDF() {

        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.pdf_layout,null);
        DisplayMetrics displayMetrics = new DisplayMetrics();

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.R){
            this.getDisplay().getRealMetrics(displayMetrics);
        }else {
            this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        }

        view.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, View.MeasureSpec.EXACTLY));

        view.layout(0,0, displayMetrics.widthPixels, displayMetrics.widthPixels);

        PdfDocument document = new PdfDocument();
        int viewWidth = view.getMeasuredWidth();
        int viewHeight = view.getMeasuredHeight();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(viewWidth,viewHeight,1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        view.draw(canvas);

        document.finishPage(page);

        // Get app-specific directory in external storage
        File directory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (directory != null && !directory.exists()) {
            directory.mkdirs();  // Create directory if it doesn't exist
        }

        // Define the file path
        String targetPdf = directory.getPath() + "/XMLtoPDF.pdf";
        File filePath = new File(targetPdf);

        try {
            document.writeTo(new FileOutputStream(filePath));
            setToast("PDF created successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error in PDF creation: " + e.toString(), Toast.LENGTH_SHORT).show();
        }

        // Close the document
        document.close();

    }

    private void createPdfChatGpt() {
        // Create a new PdfDocument
        PdfDocument pdfDocument = new PdfDocument();

        // Create a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(210, 297, 1).create(); //A4 Paper Size

        // Start a page
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        // Prepare content (Canvas)
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setTextSize(16);
        paint.setColor(android.graphics.Color.BLACK);

        // Draw text on the page
        canvas.drawText("Hello World !!", 50, 50, paint);

        // Finish the page
        pdfDocument.finishPage(page);

        // Get app-specific directory in external storage
        File directory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (directory != null && !directory.exists()) {
            directory.mkdirs();  // Create directory if it doesn't exist
        }

        // Define the file path
        String targetPdf = directory.getPath() + "/Hello.pdf";
        File filePath = new File(targetPdf);

        try {
            pdfDocument.writeTo(new FileOutputStream(filePath));
            setToast("PDF created successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error in PDF creation: " + e.toString(), Toast.LENGTH_SHORT).show();
        }

        // Close the document
        pdfDocument.close();
    }

    private void createPdf(){
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(250,400,2).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        canvas.drawText("Hello World !!",40,50,paint);
        pdfDocument.finishPage(page);

        File directory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (directory != null && !directory.exists()) {
            directory.mkdirs();  // Create directory if it doesn't exist
        }

        // Define the file path
        String targetPdf = directory.getPath() + "/first.pdf";
        File filePath = new File(targetPdf);

        try {
            pdfDocument.writeTo(new FileOutputStream(filePath));
            setToast("Successfully Downloaded");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        pdfDocument.close();
        setToast("Successfully Downloaded");

    }

    private void setToast(String text){
        if (toast!=null) toast.cancel();
        toast = Toast.makeText(MainActivity.this,text,Toast.LENGTH_SHORT);
        toast.show();
    }


}