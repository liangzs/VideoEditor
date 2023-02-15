package com.ijoysoft.mediasdk.module.opengl.theme;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理主题素材升级事件
 */
public class ThemeUpdate {
    private static class InstanceHolder {
        private static ThemeUpdate INSTANCE = new ThemeUpdate();
    }

    public static ThemeUpdate getInstance() {
        return ThemeUpdate.InstanceHolder.INSTANCE;
    }

    private List<ThemeVersion> updates;

    public ThemeUpdate() {
        updates = new ArrayList<>();
//        updates.add(new ThemeVersion("birthday14", ThemeEnum.BIRTHDAY_FOURTEEN.alias));
    }


    public ThemeVersion getUpdate(String currenVersion) {
        for (ThemeVersion version : updates) {
            if (version.currentVersion.equals(currenVersion)) {
                return version;
            }
        }
        return null;
    }

    public class ThemeVersion {
        String currentVersion;
        String lastVersion;

        public ThemeVersion(String lastVersion, String currentVersion) {
            this.lastVersion = lastVersion;
            this.currentVersion = currentVersion;
        }

        public String getCurrentVersion() {
            return currentVersion;
        }

        public void setCurrentVersion(String currentVersion) {
            this.currentVersion = currentVersion;
        }

        public String getLastVersion() {
            return lastVersion;
        }

        public void setLastVersion(String lastVersion) {
            this.lastVersion = lastVersion;
        }

//        @Override
//        public boolean equals(@Nullable Object obj) {
//            ThemeVersion t = (ThemeVersion) obj;
//            return this.currentVersion.equals(t.currentVersion);
//        }
    }
}
