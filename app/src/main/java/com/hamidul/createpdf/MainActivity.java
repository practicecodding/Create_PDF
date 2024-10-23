package com.hamidul.createpdf;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final static int REQUEST_CODE_STORAGE_PERMISSION = 1235;
    Button btnCreatePdf;
    Toast toast;
    int pageWidth = 1200;
    String[] informationArray = new String[]{"Name","Company Name","Address","Phone","Email"};
    Product product;
    ArrayList<Product> products;
    Bitmap bitmap, scaledBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCreatePdf = findViewById(R.id.btnCreatePdf);
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.idc_logo);
        scaledBitmap = Bitmap.createScaledBitmap(bitmap,30,19,false);

        products = new ArrayList<>();
        products.add(new Product("Corn Flakes 250g",150,3));
        products.add(new Product("Corn Flakes 250g pp",250,3));
        products.add(new Product("Real Almond Corn Flakes 345g",550,2));
        products.add(new Product("Chocos 250g",999,2));
        products.add(new Product("Pringles Sour Cream Onion 134g",350,6));
        products.add(new Product("Pringles Cheesy Cheese 134g",350,6));

        btnCreatePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
                    setToast("Allow Permission");
                } else {

                    // Permission already granted

                    generateInvoice();

                }
            }
        });


    }


    private void generateInvoice() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint logo = new Paint();
        Paint invoice = new Paint();
        Paint titlePaint = new Paint();
        Paint grayPaint = new Paint();

        // Load Times New Roman font from assets
        //Typeface roboto = ResourcesCompat.getFont(this,R.font.roboto);

        // Set color for gray underline
        grayPaint.setColor(Color.LTGRAY);
        grayPaint.setStrokeWidth(0.5f);

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(842, 595, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        logo.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));
        logo.setTextSize(5);
        logo.setColor(Color.parseColor("#9A000000"));

        canvas.drawBitmap(scaledBitmap,30,10,paint);

        canvas.drawText("International Distribution",62,19,logo);
        canvas.drawText("Company Bangladesh",61,24,logo);
        canvas.drawText("PLC.",60,29,logo);


        invoice.setTextSize(10);
        invoice.setFakeBoldText(true);
        //titlePaint.setTypeface(roboto);
        titlePaint.setTextScaleX(1.1f);
        titlePaint.setTextSize(8);
        titlePaint.setFakeBoldText(true);
        paint.setTextScaleX(1.1f);
        //paint.setTypeface(roboto);
        paint.setTextSize(8);

        int y = 40;

        // Title of Invoice
        invoice.setTextAlign(Paint.Align.CENTER);
        invoice.setTextScaleX(1.1f);
        canvas.drawText("Delivery Challan", 210, y, invoice);

        y += 18;
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Cash & Carry-Kellogg",210,y,paint);
        y+=12;
        canvas.drawText("16/1 Lake Circus, Kalabagan Kalabagan Dhaka",210,y,paint);
        y+=12;
        canvas.drawText("Cell Number : 01964400647",210,y,paint);

        y += 30;
        titlePaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Outlet Name : ",20,y,titlePaint);
        canvas.drawText("Business Name : ",295,y,titlePaint);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Popular Pharmacy",titlePaint.measureText("Outlet Name : ")+20,y,paint);
        canvas.drawText("Kellogg's",titlePaint.measureText("Business Name : ")+295,y,paint);

        y+=12;
        canvas.drawText("Address : ",20,y,titlePaint);
        canvas.drawText("Order Number : ",295,y,titlePaint);
        canvas.drawText("Dhanmondi Road 2",titlePaint.measureText("Address : ")+20,y,paint);
        canvas.drawText("SO9293653",titlePaint.measureText("Order Number : ")+295,y,paint);

        y+=12;
        canvas.drawText("Outlet Code : ",20,y,titlePaint);
        canvas.drawText("Order Date : ",295,y,titlePaint);
        canvas.drawText("1202050210019",titlePaint.measureText("Outlet Code : ")+20,y,paint);
        canvas.drawText("2024-10-20",titlePaint.measureText("Order Date : ")+295,y,paint);

        y+=12;
        canvas.drawText("Route Name : ",20,y,titlePaint);
        canvas.drawText("Delivery Date : ",295,y,titlePaint);
        canvas.drawText("Mirpur Road",titlePaint.measureText("Route Name : ")+20,y,paint);
        canvas.drawText(" ",titlePaint.measureText("Delivery Date : ")+295,y,paint);

        y+=12;
        canvas.drawText("SO Name : ",20,y,titlePaint);
        canvas.drawText("Md. Hamidul Sarder",titlePaint.measureText("SO Name : ")+20,y,paint);

        y+=40;
        canvas.drawLine(20, y-4, 421, y-4, grayPaint); // Draw gray underline from left to right

        y +=8;
        // Table headers
        canvas.drawText("UNIT", 120, y, titlePaint);
        canvas.drawText("ORDER", 180, y, titlePaint);
        y += 10;
        canvas.drawText("SKU NAME", 20, y, titlePaint);
        canvas.drawText("PRICE", 120, y, titlePaint);
        canvas.drawText("QTY", 180, y, titlePaint);
        canvas.drawText("Total", 240, y, titlePaint);

        y +=8;

        canvas.drawLine(20, y-2, 421, y-2, grayPaint); // Draw gray underline from left to right

        y += 8;

        int leftMargin = 20;
        int productNameWidth = 100; // Maximum width for product name before wrapping
        int lineHeight = 8;

        // Print each product line
        for (Product product : products) {
            String productName = product.getName();
            double productPrice = product.getPrice();
            int productQuantity = product.getQuantity();
            double totalPrice = productPrice * productQuantity;

            // Draw price, quantity, and total price aligned to their respective positions
            canvas.drawText(String.valueOf(productPrice), 120, y+2, paint);
            canvas.drawText(String.valueOf(productQuantity), 180, y+2, paint);
            canvas.drawText(String.valueOf(totalPrice), 240, y+2, paint);

            // Measure product name and split if necessary
            float textWidth = paint.measureText(productName);
            if (textWidth > productNameWidth) {
                // Split product name into multiple lines
                String[] words = productName.split(" ");
                StringBuilder line = new StringBuilder();
                for (String word : words) {
                    if (paint.measureText(line + word) > productNameWidth) {
                        // Draw the current line if it exceeds the width
                        canvas.drawText(line.toString(), leftMargin, y+2, paint);
                        y += lineHeight;
                        line = new StringBuilder(); // Reset for the next line
                    }
                    line.append(word).append(" ");
                }
                // Draw remaining text in the last line
                canvas.drawText(line.toString(), leftMargin, y+2, paint);
            } else {
                // Draw product name in one line if it fits
                canvas.drawText(productName, leftMargin, y+2, paint);
            }

            // Draw gray underline below each product row
            y += lineHeight;
            canvas.drawLine(20, y, 421, y, grayPaint); // Draw gray underline from left to right

            y += lineHeight; // Move to the next row
        }
//
        pdfDocument.finishPage(page);
//
        // Save PDF to storage
        File file = new File(getExternalFilesDir(null), "invoice.pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "Invoice saved as PDF", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating PDF", Toast.LENGTH_SHORT).show();
        }

        pdfDocument.close();
    }


    public class Product {
        private String name;
        private double price;
        private int quantity;

        public Product(String name, double price, int quantity) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public String getName() { return name; }
        public double getPrice() { return price; }
        public int getQuantity() { return quantity; }
    }

    private void getInvoice() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint();
        Paint grayPaint = new Paint();

        // Set color for gray borders and underline
        grayPaint.setColor(Color.GRAY);
        grayPaint.setStrokeWidth(0.5f);
        grayPaint.setStyle(Paint.Style.STROKE); // Only draw the border (outline)

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        titlePaint.setTextSize(15);
        titlePaint.setFakeBoldText(true);

        int y = 25;

        // Title of Invoice
        canvas.drawText("Invoice", 120, y, titlePaint);

        y += 20;

        // Table headers
        paint.setTextSize(12);
        int leftMargin = 10;
        int headerHeight = 20; // Height for the header row

        // Draw headers
        canvas.drawText("Product", leftMargin, y, paint);
        canvas.drawText("Price", 120, y, paint);
        canvas.drawText("Qty", 180, y, paint);
        canvas.drawText("Total", 240, y, paint);

        y+=20;

        // Draw gray border around the title row (header)
        int top = y - headerHeight; // Top border just above the text
        int bottom = y + 10; // Bottom border slightly below the text
        canvas.drawRect(10, top, 290, bottom, grayPaint); // Rectangle for the header row

        y += 20;

        // Print each product line
        int productNameWidth = 100; // Maximum width for product name before wrapping
        int lineHeight = 20;

        for (Product product : products) {
            String productName = product.getName();
            double productPrice = product.getPrice();
            int productQuantity = product.getQuantity();
            double totalPrice = productPrice * productQuantity;

            // Measure product name and split if necessary
            float textWidth = paint.measureText(productName);
            if (textWidth > productNameWidth) {
                // Split product name into multiple lines
                String[] words = productName.split(" ");
                StringBuilder line = new StringBuilder();
                for (String word : words) {
                    if (paint.measureText(line + word) > productNameWidth) {
                        // Draw the current line if it exceeds the width
                        canvas.drawText(line.toString(), leftMargin, y, paint);
                        y += lineHeight;
                        line = new StringBuilder(); // Reset for the next line
                    }
                    line.append(word).append(" ");
                }
                // Draw remaining text in the last line
                canvas.drawText(line.toString(), leftMargin, y, paint);
            } else {
                // Draw product name in one line if it fits
                canvas.drawText(productName, leftMargin, y, paint);
            }

            // Draw price, quantity, and total price aligned to their respective positions
            canvas.drawText(String.valueOf(productPrice), 120, y, paint);
            canvas.drawText(String.valueOf(productQuantity), 180, y, paint);
            canvas.drawText(String.valueOf(totalPrice), 240, y, paint);

            // Draw gray underline below each product row
            y += lineHeight;
            canvas.drawLine(10, y, 290, y, grayPaint); // Draw gray underline from left to right

            y += lineHeight; // Move to the next row
        }

        pdfDocument.finishPage(page);

        // Save PDF to storage
        File file = new File(getExternalFilesDir(null), "invoice.pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "Invoice saved as PDF", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating PDF", Toast.LENGTH_SHORT).show();
        }

        pdfDocument.close();
    }


    /*private void customPDF() {

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
    }*/



    private void setToast(String text){
        if (toast!=null) toast.cancel();
        toast = Toast.makeText(MainActivity.this,text,Toast.LENGTH_SHORT);
        toast.show();
    }





}