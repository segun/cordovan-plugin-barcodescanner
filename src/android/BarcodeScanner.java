package ng.com.idempotent.plugin.barcodescanner;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.Intent;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import org.apache.cordova.LOG;
import android.content.ActivityNotFoundException;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CordovaPlugin;

public class BarcodeScanner extends CordovaPlugin {

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    CallbackContext callbackContext;
    
    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;

        if (action.equals("scan")) {
            this.scanQR();
            return true;

        } else {            
            return false;
        }
    }

    private void scanQR() {
        try {
            Intent intent = new Intent(ACTION_SCAN);            
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);            
        } catch (ActivityNotFoundException anfe) {
            showDialog(this.cordova.getActivity(), "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();            
        }            
    }

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);  
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");                
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {
                }                    
            }                
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }                
        });        
        return downloadDialog.show();        
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            LOG.d("Xendbit:Barcodescanner", resultCode);
            //if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");                
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                LOG.d("Xendbit:Barcodescanner", contents);
                LOG.d("Xendbit:Barcodescanner", format);
                this.callbackContext.sendPluginResult(PluginResult.Status.OK, contents);             
            //}                
        }            
    }        
}
