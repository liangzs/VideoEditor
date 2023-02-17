package com.qiusuo.videoeditor.common.bean;

import com.ijoysoft.mediasdk.common.utils.LogUtils;

import java.util.Locale;
import java.util.Objects;

/**
 * 挂载主题的资源类
 */
public class ThemeResourceEntity {
    private int templateIndex;
    private String templateName;
    private String path;
    private ThemeResourceTitle title;

    public ThemeResourceEntity() {

    }

    public ThemeResourceEntity(int index, String templateName) {
        this.templateIndex = index;
        this.templateName = templateName;
    }

    public ThemeResourceEntity(String templateName) {
        this.templateName = templateName;
    }

    public int getTemplateIndex() {
        return templateIndex;
    }

    public void setTemplateIndex(int templateIndex) {
        this.templateIndex = templateIndex;
    }

    public String getTemplateName() {
        if (templateName == null) {
            LogUtils.v("", "" + path);
        }
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThemeResourceEntity that = (ThemeResourceEntity) o;
        return templateName.equals(that.templateName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(templateName);
    }

    public ThemeResourceTitle getTitle() {
        return title;
    }

    public void setTitle(ThemeResourceTitle title) {
        this.title = title;
    }

    public static class ThemeResourceTitle {
        private String EN;
        private String ZH;
        private String ZH_HK;

        public String getTitle() {
//            System.out.println(Locale.getDefault().getDisplayScript() + Locale.getDefault().getLanguage() + Locale.getDefault().getDisplayLanguage());
            if (Locale.getDefault().getLanguage().toLowerCase().contains("zh")) {
                if (Locale.getDefault().getDisplayScript().contains("繁體")) {
                    return ZH_HK;
                }
                return ZH;
            }
            return EN;
        }

        private String getLanguageEnv() {
            Locale l = Locale.getDefault();
            String language = l.getLanguage();
            String country = l.getCountry().toLowerCase();
            System.out.println("------------------languange:" + language + ",country:" + country);
            if ("zh".equals(language)) {
                if ("cn".equals(country)) {
                    language = "zh-CN";
                } else if ("tw".equals(country)) {
                    language = "zh-TW";
                }
            } else if ("pt".equals(language)) {
                if ("br".equals(country)) {
                    language = "pt-BR";
                } else if ("pt".equals(country)) {
                    language = "pt-PT";
                }
            }
            return language;
        }

        public ThemeResourceTitle(String EN, String ZH, String ZH_HK) {
            this.EN = EN;
            this.ZH = ZH;
            this.ZH_HK = ZH_HK;
        }

        public String getEN() {
            return EN;
        }

        public void setEN(String EN) {
            this.EN = EN;
        }

        public String getZH() {
            return ZH;
        }

        public void setZH(String ZH) {
            this.ZH = ZH;
        }

        public String getZH_HK() {
            return ZH_HK;
        }

        public void setZH_HK(String ZH_HK) {
            this.ZH_HK = ZH_HK;
        }
    }
}
