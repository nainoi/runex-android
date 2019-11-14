package com.think.runex.java.Utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import com.think.runex.BuildConfig;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UriUtils {

    public String getRealPath(Context context, Uri uri) {
        if (context == null) {
            return null;
        }

        //DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            //ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                String[] docIdSet = DocumentsContract.getDocumentId(uri).split(":");
                if (docIdSet[0].toLowerCase(Locale.getDefault()).equals("primary")) {
                    return Environment.getExternalStorageDirectory() + "/" + docIdSet[1];
                } else if (uri.getAuthority() != null && uri.getAuthority().contains("com.android.externalstorage.documents")) {
                    return "/storage/" + docIdSet[0] + "/" + docIdSet[1];
                }
            }
            //DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                String id = DocumentsContract.getDocumentId(uri);
                if (id.startsWith("raw:")) {
                    return id.substring(4);
                }

                String[] contentUriPrefixesToTry = new String[]{
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads",
                        "content://downloads/all_downloads"};

                for (String contentUriPrefix : contentUriPrefixesToTry) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.parseLong(id));
                    try {
                        String path = getDataColumn(context, contentUri, null, null);
                        if (path != null && path.length() > 0) {
                            return path;
                        }
                    } catch (Throwable error) {
                        error.printStackTrace();
                    }
                }

                //path could not be retrieved using ContentResolver, therefore copy file to accessible cache using streams
                String fileName = getFileName(context, uri);
                File cacheDir = getDocumentCacheDir(context);
                File file = generateFileName(fileName, cacheDir);
                if (file != null) {
                    saveFileFromUri(context, uri, file.getAbsolutePath());
                    return file.getAbsolutePath();
                }
            }
            //MediaProvider
            else if (isMediaDocument(uri)) {
                String[] docIdSet = DocumentsContract.getDocumentId(uri).split(":");
                Uri contentUri = null;
                if (docIdSet[0].equals("image")) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if (docIdSet[0].equals("video")) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if (docIdSet[0].equals("audio")) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                String selection = "_id=?";
                String[] selectionArgs = new String[]{(docIdSet[1])};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }

        //MediaStore (and general)
        if (uri.getScheme() != null && uri.getScheme().toLowerCase(Locale.getDefault()).equals("content")) {
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
          /*  else if (isGoogleDrive(uri)) {
                Cursor cursor = null;
                try {
                    cursor = context.getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null) {
                        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                        cursor.moveToFirst();
                        String name = cursor.getString(nameIndex);
                        Long size = cursor.getLong(sizeIndex);

                        //Create Temp file
                        String time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
                        String fileName = "/IMG_" + time + ".jpg";

                        File directory = new File((Environment.getExternalStorageDirectory() + "/RUNEX"));
                        if (!directory.exists()) {
                            directory.mkdirs();
                        }

                        File tempFile = new File((directory.getPath() + fileName));

                        //Copy input stream to tempFile.
                        copyInputStreamToFile(context.getContentResolver().openInputStream(uri), tempFile);

                        if (tempFile.length() > 0) {
                            return Uri.fromFile(tempFile).getPath();
                        }
                    }
                } catch (Throwable error) {
                    error.printStackTrace();
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            } */
            else {
                return getDataColumn(context, uri, null, null);
            }
        }

        //File
        if (uri.getScheme() != null && uri.getScheme().toLowerCase(Locale.getDefault()).equals("file")) {
            return uri.getPath();
        }

        return null;
    }

    private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Files.FileColumns.DATA;
        String[] projection = new String[]{column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(columnIndex);
            }
        } catch (Throwable error) {
            error.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    private File getDocumentCacheDir(Context context) {
        File dir = new File(context.getCacheDir(), "documents");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    private String getFileName(Context context, Uri uri) {
        String fileName = "";
        String mimeType = context.getContentResolver().getType(uri);

        if (mimeType != null) {
            String path = uri.toString();
            if (path.length() > 0) {
                fileName = path.substring(path.lastIndexOf('/') + 1);
            } else {
                fileName = new File(path).getName();
            }
        } else {
            try {
                Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
                if (returnCursor != null) {
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    returnCursor.moveToFirst();
                    fileName = returnCursor.getString(nameIndex);
                    returnCursor.close();
                }
            } catch (Throwable error) {
                error.printStackTrace();
            }
        }

        return fileName;
    }

    private File generateFileName(String name, File directory) {
        File file = new File(directory, name);
        if (file.exists()) {
            String fileName = name;
            String extension = "";

            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) {
                fileName = name.substring(0, dotIndex);
                extension = name.substring(dotIndex);
            }

            int index = 0;
            while (file.exists()) {
                index++;
                file = new File(directory, fileName + "(" + index + ")" + extension);
            }
        }
        try {
            if (!file.createNewFile()) {
                return null;
            }
        } catch (Throwable error) {
            error.printStackTrace();
            return null;
        }

        return file;
    }

    public void saveFileFromUri(Context context, Uri uri, String destinationPath) {
        InputStream ins = null;
        BufferedOutputStream bos = null;
        try {
            ins = context.getContentResolver().openInputStream(uri);
            bos = new BufferedOutputStream(new FileOutputStream(destinationPath, false));
            byte[] buf = new byte[1024];
            if (ins != null) {
                ins.read(buf);
                do {
                    bos.write(buf);
                } while (ins.read(buf) != -1);
            }
        } catch (Throwable error) {
            error.printStackTrace();
        } finally {
            try {
                if (ins != null) {
                    ins.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Copy an InputStream to a File.
    private void copyInputStreamToFile(InputStream in, File file) {
        OutputStream out = null;

        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Ensure that the InputStreams are closed even if there's an exception.
            try {
                if (out != null) {
                    out.close();
                }

                // If you want to close the "in" InputStream yourself then remove this
                // from here but ensure that you close it yourself eventually.
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is local.
     */
    private boolean isLocalStorageDocument(Uri uri) {
        return (BuildConfig.APPLICATION_ID + ".provider").equals(uri.getAuthority());
    }

    private boolean isGoogleDrive(Uri uri) {
        if (uri.getAuthority() == null) return false;
        return uri.getAuthority().equals("com.google.android.apps.docs.storage") ||
                uri.getAuthority().equals("com.google.android.apps.docs.storage.legacy");

    }

    private boolean isGooglePhotosUri(Uri uri) {
        if (uri.getAuthority() == null) return false;
        return uri.getAuthority().equals("com.google.android.apps.photos.content");
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private boolean isExternalStorageDocument(Uri uri) {
        if (uri.getAuthority() == null) return false;
        return uri.getAuthority().equals("com.android.externalstorage.documents");
    }

    private boolean isDownloadsDocument(Uri uri) {
        if (uri.getAuthority() == null) return false;
        return uri.getAuthority().equals("com.android.providers.downloads.documents");
    }

    private boolean isMediaDocument(Uri uri) {
        if (uri.getAuthority() == null) return false;
        return uri.getAuthority().equals("com.android.providers.media.documents");
    }
}
