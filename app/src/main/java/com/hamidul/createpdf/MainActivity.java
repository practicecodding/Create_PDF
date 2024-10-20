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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
public class MainActivity extends AppCompatActivity {
    final static int REQUEST_CODE_STORAGE_PERMISSION = 1235;
    Button btnCreatePdf;
    Toast toast;
    String[] informationArray = new String[]{"Name","Company Name","Address","Phone","Email"};


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

                    // Permission already granted

                    customPDF();

                }
            }
        });


    }

    private void customPDF() {

        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(250,400,2).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(12.0f);
        canvas.drawText("HR Enterprises",pageInfo.getPageWidth()/2,30,paint);

        paint.setTextSize(6.0f);
        paint.setTextScaleX(1.5f);
        paint.setColor(Color.rgb(122,119,119));
        canvas.drawText("Street No. 15,  Bharat Nagar,  Haryana", pageInfo.getPageWidth()/2, 40,paint);
        paint.setTextScaleX(1f);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(9.0f);
        paint.setColor(Color.rgb(122,119,119));
        canvas.drawText("Customer Information",10,70,paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(8.0f);
        paint.setColor(Color.BLACK);

        int startXPosition = 10;
        int endXPosition = pageInfo.getPageWidth()-10;
        int startYPosition = 100;

        for (int i=0; i<informationArray.length; i++){
            canvas.drawText(informationArray[i],startXPosition,startYPosition,paint);
            canvas.drawLine(startXPosition,startYPosition+3,endXPosition,startYPosition+3,paint);
            startYPosition+=20;
        }

        canvas.drawLine(80,92,80,190,paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawRect(10,200,pageInfo.getPageWidth()-10,300,paint);
        canvas.drawLine(85,200,85,300,paint);
        canvas.drawLine(163,200,163,300,paint);
        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawText("Photo",35,250,paint);
        canvas.drawText("Photo",110,250,paint);
        canvas.drawText("Photo",190,250,paint);

        canvas.drawText("Note:",10,320,paint);
        canvas.drawLine(35,325,pageInfo.getPageWidth()-10,325,paint);
        canvas.drawLine(10,345,pageInfo.getPageWidth()-10,345,paint);
        canvas.drawLine(10,365,pageInfo.getPageWidth()-10,365,paint);

        pdfDocument.finishPage(page);

        File directory = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (directory != null && !directory.exists()) {
            directory.mkdirs();  // Create directory if it doesn't exist
        }

        // Define the file path
        String targetPdf = directory.getPath() + "/custom.pdf";
        File filePath = new File(targetPdf);

        try {
            pdfDocument.writeTo(new FileOutputStream(filePath));
            setToast("Successfully Downloaded");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        pdfDocument.close();


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

    }

    private void setToast(String text){
        if (toast!=null) toast.cancel();
        toast = Toast.makeText(MainActivity.this,text,Toast.LENGTH_SHORT);
        toast.show();
    }


}