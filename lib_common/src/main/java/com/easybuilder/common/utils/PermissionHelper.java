package com.easybuilder.common.utils;

import android.content.pm.PackageManager;
import android.os.Build;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author renshijie
 * @email renshijie@tsingcloud.com
 * @createTime 2024/1/11
 * @describe PermissionHelper 权限请求工具
 **/
@Deprecated(forRemoval = true, since = "v1.0.1")
public class PermissionHelper {
    private final ActivityResultLauncher<String> requestSinglePermissionLauncher;
    private final ActivityResultLauncher<String[]> requestPermissionsLauncher;
    private WeakReference<FragmentActivity> activityWeakReference;
    private PermissionCallback callback;

    public PermissionHelper(FragmentActivity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
        requestSinglePermissionLauncher = this.activityWeakReference.get().registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (callback != null)
                    callback.callback(result);
            }
        });

        requestPermissionsLauncher = this.activityWeakReference.get().registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                if (result != null) {
                    Map<String, Boolean> granted = null;//同意的权限
                    Map<String, Boolean> denied = null;//拒绝的权限
                    Set<String> keys = result.keySet();
                    for (String key : keys) {
                        if (Boolean.TRUE.equals(result.get(key))) {
                            if (granted == null)
                                granted = new HashMap<>();
                            granted.put(key, result.get(key));
                        } else {
                            if (denied == null)
                                denied = new HashMap<>();
                            denied.put(key, result.get(key));
                        }
                    }
                    boolean isAllGranted = denied == null;
                    boolean isAllDenied = granted == null;
                    //回调最终结果
                    if (callback != null)
                        callback.callback(granted, denied, isAllGranted, isAllDenied);
                }

            }
        });
    }

    public static PermissionHelper with(FragmentActivity activity) {
        return new PermissionHelper(activity);
    }

    /**
     * 检查权限
     *
     * @param permission
     * @return
     */
    public boolean hasPermission(String permission) {
        return activityWeakReference.get().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public String[] hasPermissions(String... permissions) {
        List<String> arr = new ArrayList<>();
        for (String permission : permissions) {
            if (hasPermission(permission)) {
                continue;
            }
            arr.add(permission);
        }
        return arr.toArray(new String[]{});
    }

    /**
     * 请求权限
     *
     * @param callback
     * @param permissions
     */
    public void requestPermission(PermissionCallback callback, String... permissions) {
        if (permissions == null) {
            return;
        }
        if (permissions.length == 0) {
            return;
        }
        this.callback = callback;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissions.length > 1) {
//                String[] ps = hasPermissions(permissions);
                String[] ps = permissions;
                if (ps != null && ps.length > 0) {
                    requestPermissionsLauncher.launch(ps);
                }
            } else {
                if (!hasPermission(permissions[0])) {
                    requestSinglePermissionLauncher.launch(permissions[0]);
                }
            }
        }

    }

    /**
     * 回调
     */
    public interface PermissionCallback {
        /**
         * 批量请求
         *
         * @param granted      成功的权限
         * @param denied       失败的权限
         * @param isAllGranted 是否全部成功
         * @param isAllDenied  是否全部失败
         */
        void callback(Map<String, Boolean> granted, Map<String, Boolean> denied, boolean isAllGranted, boolean isAllDenied);

        /**
         * 单权限请求回调
         *
         * @param result
         */
        void callback(Boolean result);
    }

}
