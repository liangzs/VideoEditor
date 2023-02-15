package com.ijoysoft.mediasdk.common.global;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.os.storage.StorageManager;

import com.ijoysoft.mediasdk.module.opengl.InnerBorder;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 除却一些初始化工作，也用于module调用主壳app的资源
 */
public class MediaSdk {
    private static class InstanceHolder {
        private static MediaSdk INSTANCE = new MediaSdk();
    }

    public static MediaSdk getInstance() {
        return MediaSdk.InstanceHolder.INSTANCE;
    }

    private Context context;
    private List<String> decoders;
    private List<MediaCodecInfo> codecInfos;
    private String extPath;
    public static final String ENCRYPT_KEY = "ijoysoft";

    /**
     * 一些参数的初始化
     *
     * @param context
     */
    public void init(Context context) {
        this.context = context;
        ConstantMediaSize.init(context);
        //初始化边框
        ThreadPoolMaxThread.getInstance().execute(InnerBorder.Companion::getIdMap);
    }

    public void getSupportDecoder() {
        int numCodecs = MediaCodecList.getCodecCount();
        for (int i = 0; i < numCodecs; i++) {
            MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
            if (!codecInfo.isEncoder()) {
                continue;
            }
            String[] types = codecInfo.getSupportedTypes();
            for (int j = 0; j < types.length; j++) {
                if (decoders.contains(types[j])) {
                    continue;
                } else {
                    decoders.add(types[j]);
                    codecInfos.add(codecInfo);
                    // MediaCodecInfo.CodecCapabilities caps = info.getCapabilitiesForType(types[j]);
                }
            }
        }
    }

    private void getExtendedMemoryPath() {
        StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        Class storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (removable) {
                    extPath = path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public List<String> getDecoders() {
        return decoders;
    }

    public String getExtPath() {
        return extPath;
    }

    public void setExtPath(String extPath) {
        this.extPath = extPath;
    }

    public List<MediaCodecInfo> getCodecInfos() {
        return codecInfos;
    }

    public Resources getResouce() {
        return context.getResources();
    }

    public Context getContext() {
        return context;
    }
}
