package com.example.easybill;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewBill extends AppCompatActivity {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    Button AddItemButton, GenerateBillButton;
    ListView NewBillList;
    private File pdfFile;
    private List<String> selectedItemsList;
    private List<String> selectedItemsQuantity;
    private List<String> selectedItemsPrices;
    private List<String> selectedItemsDiscount;
    private List<String> selectedItemsTotal;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bill);

        AddItemButton = findViewById(R.id.addInBillButton);
        GenerateBillButton = findViewById(R.id.generateBillButton);
        NewBillList = findViewById(R.id.listViewNewBill);

        // Create an adapter for the selected items ListView
        selectedItemsList = new ArrayList<>();
        selectedItemsQuantity = new ArrayList<>();
        selectedItemsPrices = new ArrayList<>();
        selectedItemsDiscount = new ArrayList<>();
        selectedItemsTotal = new ArrayList<>();

        adapter = new ArrayAdapter<String>(this, R.layout.newbill_list, R.id.itemName, selectedItemsList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView itemNameTextView = view.findViewById(R.id.itemName);
                TextView itemQuantityTextView = view.findViewById(R.id.itemQuantity);
                TextView itemPriceTextView = view.findViewById(R.id.itemPrice);
                TextView itemDiscountTextView = view.findViewById(R.id.itemDiscount);
                TextView itemTotalTextView = view.findViewById(R.id.itemTotal);

                String itemName = selectedItemsList.get(position);
                String itemQuantity = selectedItemsQuantity.get(position);
                String itemPrice = selectedItemsPrices.get(position);
                String itemDiscount = selectedItemsDiscount.get(position);
                String itemTotal = selectedItemsTotal.get(position);

                itemNameTextView.setText(itemName);
                itemQuantityTextView.setText(itemQuantity);
                itemPriceTextView.setText(itemPrice);
                itemDiscountTextView.setText(itemDiscount);
                itemTotalTextView.setText(itemTotal);

                return view;
            }
        };
        NewBillList.setAdapter(adapter);
        NewBillList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Show a dialog or confirmation message before deleting
                showDeleteConfirmationDialog(position);
                return true;
            }
        });


        AddItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewBill.this, TotalItems.class);
                startActivityForResult(intent,1);
            }
        });

        GenerateBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPDF();
            }
        });
    }

    private void createPDF() {
        // Get the directory where you want to save the PDF file
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        // Create a unique file name with timestamp
        String timeStamp = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        }
        String fileName = "EasyBill_" + timeStamp + ".pdf";
        pdfFile = new File(directory, fileName);

        // Get the absolute file path
        String filePath = pdfFile.getAbsolutePath();

        // Initialize the PDF document
        Document document = new Document();
        document.setMargins(10,10,50,40);

        String companyName = getResources().getString(R.string.name);

        try {

            // Create a PdfWriter to write the document to a file
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));


            // Open the document
            document.open();

            //header
            String headerName = getString(R.string.name);
            document.add(new Paragraph(headerName, new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD)));



            // Create a table for the bill items
            PdfPTable table = new PdfPTable(5); // 4 columns for item name, quantity, price, and total
            float[] columnWidths = {3f, 1f, 1f, 1f, 1f}; // Adjust the values as per your requirements
            table.setWidths(columnWidths);
            table.normalizeHeadersFooters();
            table.setPaddingTop(10f);

            // Set table headers
            Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            PdfPCell itemCell = new PdfPCell(new Phrase("Item", boldFont));
            PdfPCell quantityCell = new PdfPCell(new Phrase("Quantity", boldFont));
            PdfPCell priceCell = new PdfPCell(new Phrase("Price", boldFont));
            PdfPCell discountCell = new PdfPCell(new Phrase("Discount", boldFont));
            PdfPCell totalCell = new PdfPCell(new Phrase("Total", boldFont));

            table.addCell(itemCell);
            table.addCell(quantityCell);
            table.addCell(priceCell);
            table.addCell(discountCell);
            table.addCell(totalCell);


            float grandTotal = 0;


            // Iterate over the ListView items
            for (int i = 0; i < selectedItemsList.size(); i++) {
                String nameItem = selectedItemsList.get(i);
                String quantityItem = selectedItemsQuantity.get(i);
                String priceItem = selectedItemsPrices.get(i);
                String discountItem = selectedItemsDiscount.get(i);
                String totalItem = selectedItemsTotal.get(i);
                // Calculating Grand total logic
                // Remove non-numeric characters from the totalItem string
                String cleanTotalItem = totalItem.replaceAll("[^0-9.]", "");
                // Parse the cleaned string as a float
                float tt = Float.parseFloat(cleanTotalItem);

                grandTotal += tt;
                // Add the item details to the table
                table.addCell(nameItem);
                table.addCell(quantityItem);
                table.addCell(priceItem);
                table.addCell(discountItem);
                table.addCell(totalItem);
            }

            PdfPCell cell1 = new PdfPCell(new Phrase(""));
            PdfPCell cell2 = new PdfPCell(new Phrase(""));
            PdfPCell cell3 = new PdfPCell(new Phrase(""));
            PdfPCell cell4 = new PdfPCell(new Phrase("Total: "));
            PdfPCell cell5 = new PdfPCell(new Phrase(" " + grandTotal));

            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            table.addCell(cell5);

            document.add(table);


            // Close the document
            document.close();

            // Notify the user that the PDF is ready for download
            Toast.makeText(this, "PDF bill created. You can download it now.", Toast.LENGTH_SHORT).show();





        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PDF Creation", "Error creating PDF: " + e.getMessage(), e);
            // Handle any exceptions that occurred during PDF creation
            Toast.makeText(this, "PDF not created" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Retrieve the selected item from Activity A
            String selectedItem = data.getStringExtra("selectedItem");
            String selectedQuantity = data.getStringExtra("selectedQuantity");
            String selectedPrice = data.getStringExtra("selectedPrice");
            String selectedDiscount = data.getStringExtra("selectedDiscount");
            String selectedTotal = data.getStringExtra("selectedTotal");

            // Add the selected item to the selected items list
            selectedItemsList.add(selectedItem);
            selectedItemsQuantity.add(selectedQuantity);
            selectedItemsPrices.add(selectedPrice);
            selectedItemsDiscount.add(selectedDiscount);
            selectedItemsTotal.add(selectedTotal);

            adapter.notifyDataSetChanged();
        }
    }


    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete the item and refresh the adapter
                        deleteItem(position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User canceled the deletion
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void deleteItem(int position) {
        selectedItemsList.remove(position);
        selectedItemsQuantity.remove(position);
        selectedItemsPrices.remove(position);
        selectedItemsDiscount.remove(position);
        selectedItemsTotal.remove(position);

        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
    }

}
