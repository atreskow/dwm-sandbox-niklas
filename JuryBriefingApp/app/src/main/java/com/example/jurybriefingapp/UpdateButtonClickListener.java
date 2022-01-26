package com.example.jurybriefingapp;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.File;
import java.net.URL;

public class UpdateButtonClickListener implements DialogInterface.OnClickListener
{
    Context context;
    ProgressDialog progressDialog;
    URL downloadUrl;

    public UpdateButtonClickListener(Context context, URL downloadURL)
    {
        this.context = context;
        this.progressDialog = new ProgressDialog(context);
        this.downloadUrl = downloadURL;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i)
    {

        progressDialog.setMessage("Downloading app");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgressNumberFormat("");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);

        DownloadFile downloadFile = new DownloadFile();
        downloadFile.execute();
    }


    private class DownloadFile extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids)
        {
            Uri apk_uri = Uri.parse(downloadUrl.toString());

            DownloadManager.Request request = new DownloadManager.Request(apk_uri);

            request.setTitle("Downloading app");
            request.setDescription("Please wait while downloading the updated app");

            request.setDestinationInExternalFilesDir(context, null, Constants.APK_NAME);

            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            long downloadID = downloadManager.enqueue(request);

            boolean downloading = true;

            while(downloading)
            {
                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(downloadID);

                Cursor cursor = downloadManager.query(q);
                cursor.moveToFirst();

                int bytes_downloaded = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                int bytes_total = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                int dl_progress = (int) ((bytes_downloaded * 100) / bytes_total);
                progressDialog.setProgress(dl_progress);

                if(cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL)
                {
                    downloading = false;
                    progressDialog.dismiss();

                    File apkFile = new File(context.getExternalFilesDir(null), Constants.APK_NAME);

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                    context.startActivity(intent);
                }

                cursor.close();
            }

            return null;
        }


        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog.show();
        }


        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
}