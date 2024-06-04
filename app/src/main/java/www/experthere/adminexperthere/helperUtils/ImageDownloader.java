package www.experthere.adminexperthere.helperUtils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

public class ImageDownloader {

    public void downloadImage(Context context, String imageUrl, String title, String imageName, String description) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl));
        
        // Set title and description for the download notification (optional)
        request.setTitle(title);
        request.setDescription(description);
        
        // Set destination directory for the downloaded file
        request.setVisibleInDownloadsUi(true);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);


        String imgName = imageName+".jpg";

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, imgName);

        // Get the download service and enqueue the request
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);


        if (downloadManager != null) {
            downloadManager.enqueue(request);
        }
    }
}
