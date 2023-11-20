package com.example.petshopuser.Helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

import com.example.petshopuser.common.BeginActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class ConnectionHelper {
    public static void handleConnectionSuspended(int cause, Context context) {
        switch (cause) {
            case GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST:
                showNetworkLostDialog(context);
                break;
            case GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED:
                showServiceDisconnectedDialog(context);
                break;
            default:
                // Xử lý các nguyên nhân khác nếu cần
                break;
        }
    }

    public static void handleConnectionFailed(ConnectionResult connectionResult, Context context) {
        showConnectionFailedDialog(connectionResult, context);
    }

    private static void showNetworkLostDialog(Context context) {
        // Hiển thị thông báo cho người dùng về mất kết nối mạng
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Mất kết nối mạng");
        builder.setMessage("Vui lòng kiểm tra kết nối mạng của bạn và thử lại.");
        builder.setPositiveButton("Wifi Setting", (dialog, which) -> {
            // Chuyển hướng đến màn hình cài đặt WiFi
            openWifiSettings(context);
            dialog.dismiss();
        });
        builder.setNegativeButton("Turn Off", (dialog, which) -> {
            // Đóng ứng dụng
            ((Activity) context).finish();
        });
        builder.show();
    }

    private static void showServiceDisconnectedDialog(Context context) {
        // Hiển thị thông báo cho người dùng về việc dịch vụ bị ngắt kết nối
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Dịch vụ ngắt kết nối");
        builder.setMessage("Dịch vụ kết nối đã ngắt. Vui lòng khởi động lại ứng dụng.");
        builder.setPositiveButton("OK", (dialog, which) -> {
            // Khởi động lại ứng dụng
            restartApp(context);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private static void showConnectionFailedDialog(ConnectionResult connectionResult, Context context) {
        // Hiển thị thông báo cho người dùng khi kết nối thất bại
        int errorCode = connectionResult.getErrorCode();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Kết nối thất bại");
        builder.setMessage("Mã lỗi: " + errorCode + ". Vui lòng thử lại sau.");
        builder.setPositiveButton("Wifi Setting", (dialog, which) -> {
            // Chuyển hướng đến màn hình cài đặt WiFi
            openWifiSettings(context);
            dialog.dismiss();
        });
        builder.setNegativeButton("Turn Off", (dialog, which) -> {
            // Đóng ứng dụng
            ((Activity) context).finish();
        });
        builder.show();

        builder.show();
    }

    private static void openWifiSettings(Context context) {
        // Tạo Intent để mở màn hình cài đặt WiFi
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);

        // Kiểm tra xem có ứng dụng nào có thể xử lý Intent này hay không
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            // Xử lý trường hợp không tìm thấy ứng dụng xử lý Intent
            Toast.makeText(context, "Không thể mở cài đặt WiFi.", Toast.LENGTH_SHORT).show();
        }
    }

    private static void restartApp(Context context) {
        Intent intent = new Intent(context, BeginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }
}
