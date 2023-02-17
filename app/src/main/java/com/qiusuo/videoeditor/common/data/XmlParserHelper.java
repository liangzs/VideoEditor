package com.qiusuo.videoeditor.common.data;

import android.util.Xml;


import com.qiusuo.videoeditor.common.bean.MusicEntity;
import com.qiusuo.videoeditor.common.bean.MusicGroupEntity;
import com.qiusuo.videoeditor.common.bean.ThemeDownEntity;
import com.qiusuo.videoeditor.common.bean.ThemeGroupEntity;
import com.qiusuo.videoeditor.common.constant.DownloadPath;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlParserHelper {

    /**
     * assetTest
     *
     * @param is
     * @return
     * @throws Exception 分类增加修改点
     */
    public static ThemeGroupEntity pullxmlThemeGroup(InputStream is) throws Exception {
        ThemeGroupEntity themeGroupEntity = null;
        List<ThemeDownEntity> list = null;
        ThemeDownEntity downEntity = null;
        //创建xmlPull解析器
        XmlPullParser parser = Xml.newPullParser();
        ///初始化xmlPull解析器
        parser.setInput(is, "utf-8");
        //读取文件的类型
        int type = parser.getEventType();
        //无限判断文件类型进行读取
        while (type != XmlPullParser.END_DOCUMENT) {
            switch (type) {
                //开始标签
                case XmlPullParser.START_TAG:
                    if ("theme".equals(parser.getName())) {
                        themeGroupEntity = new ThemeGroupEntity();
                    } else if ("birthday".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("valentine".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("wedding".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("baby".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("holiday".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("greeting".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("celebration".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("love".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("travel".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("hot".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("rakshabandhan".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("onam".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("attitude".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("holi".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("beat".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("friend".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("common".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("christmas".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("newyear".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("food".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("daily".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("daily".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("food".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("sport".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("item".equals(parser.getName())) {
                        downEntity = new ThemeDownEntity();
                        downEntity.setIndex(Integer.valueOf(parser.getAttributeValue(null, "index")));
                        downEntity.setName(parser.getAttributeValue(null, "name"));
                        downEntity.setPath(parser.getAttributeValue(null, "path"));
                        downEntity.setSize(Integer.valueOf(parser.getAttributeValue(null, "size")));
                        downEntity.setMusicName(parser.getAttributeValue(null, "musicName"));
                        downEntity.setMusicPath(parser.getAttributeValue(null, "musicPath"));
                        list.add(downEntity);
                    }
                    break;
                //结束标签
                case XmlPullParser.END_TAG:
                    if ("birthday".equals(parser.getName())) {
                        themeGroupEntity.setBirthday(list);
                    } else if ("valentine".equals(parser.getName())) {
                        themeGroupEntity.setValentine(list);
                    } else if ("wedding".equals(parser.getName())) {
                        themeGroupEntity.setWedding(list);
                    } else if ("baby".equals(parser.getName())) {
                        themeGroupEntity.setBaby(list);
                    } else if ("holiday".equals(parser.getName())) {
                        themeGroupEntity.setHoliday(list);
                    } else if ("greeting".equals(parser.getName())) {
                        themeGroupEntity.setGreeting(list);
                    } else if ("celebration".equals(parser.getName())) {
                        themeGroupEntity.setCelebration(list);
                    } else if ("love".equals(parser.getName())) {
                        themeGroupEntity.setLove(list);
                    } else if ("travel".equals(parser.getName())) {
                        themeGroupEntity.setTravel(list);
                    } else if ("hot".equals(parser.getName())) {
                        themeGroupEntity.setHots(list);
                    } else if ("rakshabandhan".equals(parser.getName())) {
                        themeGroupEntity.setRakshaBandHan(list);
                    } else if ("onam".equals(parser.getName())) {
                        themeGroupEntity.setOnam(list);
                    } else if ("attitude".equals(parser.getName())) {
                        themeGroupEntity.setAttitude(list);
                    } else if ("beat".equals(parser.getName())) {
                        themeGroupEntity.setBeat(list);
                    } else if ("holi".equals(parser.getName())) {
                        themeGroupEntity.setHoli(list);
                    } else if ("friend".equals(parser.getName())) {
                        themeGroupEntity.setFriend(list);
                    } else if ("common".equals(parser.getName())) {
                        themeGroupEntity.setCommon(list);
                    } else if ("christmas".equals(parser.getName())) {
                        themeGroupEntity.setChristmas(list);
                    } else if ("newyear".equals(parser.getName())) {
                        themeGroupEntity.setNewYear(list);
                    } else if ("food".equals(parser.getName())) {
                        themeGroupEntity.setFood(list);
                    } else if ("daily".equals(parser.getName())) {
                        themeGroupEntity.setDaily(list);
                    } else if ("sport".equals(parser.getName())) {
                        themeGroupEntity.setSport(list);
                    }
                    break;
            }
            //继续往下读取标签类型
            type = parser.next();
        }

        //动态添加holi本地测试数据
        return themeGroupEntity;
    }


    public static ThemeGroupEntity pullxmlThemeGroup(String path) {
        try (InputStream inputStream = new FileInputStream(path)) {
            return pullxmlThemeGroup(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ThemeGroupEntity.EMPTY_THEME_GROUP_ENTITY;
    }


    public static MusicGroupEntity pullxmlMusic(String path) {
        try (InputStream inputStream = new FileInputStream(path)) {
            return pullxmlMusic(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static MusicGroupEntity pullxmlMusic(InputStream inputStream) {
        MusicGroupEntity musicGroupEntity = new MusicGroupEntity();
        String localPath;
        try {
            List<MusicEntity> list = null;
            MusicEntity musicEntity = null;
            //创建xmlPull解析器
            XmlPullParser parser = Xml.newPullParser();
            ///初始化xmlPull解析器
            parser.setInput(inputStream, "utf-8");
            //读取文件的类型
            int type = parser.getEventType();
            //无限判断文件类型进行读取
            while (type != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    //开始标签
                    case XmlPullParser.START_TAG:
                        if ("music".equals(parser.getName())) {
//                            musicGroupEntity = new MusicGroupEntity();
                        } else if ("general".equals(parser.getName())) {
                            list = new ArrayList<>();
                        } else if ("happy".equals(parser.getName())) {
                            list = new ArrayList<>();
                        } else if ("love".equals(parser.getName())) {
                            list = new ArrayList<>();
                        } else if ("soft".equals(parser.getName())) {
                            list = new ArrayList<>();
                        } else if ("children".equals(parser.getName())) {
                            list = new ArrayList<>();
                        } else if ("birthday".equals(parser.getName())) {
                            list = new ArrayList<>();
                        } else if ("dynamic".equals(parser.getName())) {
                            list = new ArrayList<>();
                        } else if ("epic".equals(parser.getName())) {
                            list = new ArrayList<>();
                        } else if ("item".equals(parser.getName())) {
                            musicEntity = new MusicEntity();
                            musicEntity.setType(Integer.valueOf(parser.getAttributeValue(null, "type")));
                            if (parser.getAttributeValue(null, "id") != null) {
                                musicEntity.setId(Integer.valueOf(parser.getAttributeValue(null, "id")));
                            }
                            if (parser.getAttributeValue(null, "copyright") != null) {
                                musicEntity.setCopyRight(parser.getAttributeValue(null, "copyright"));
                            }
                            musicEntity.setName(parser.getAttributeValue(null, "name"));
                            musicEntity.setPath(DownloadPath.BASE_PATH + parser.getAttributeValue(null, "path"));
                            //音频保存为无后缀形式
//                            localPath = PathUtil.MUSIC + parser.getAttributeValue(null, "path");
                            localPath = "" + parser.getAttributeValue(null, "path");
                            localPath = localPath.substring(0, localPath.lastIndexOf("."));
                            musicEntity.setLocalPath(localPath);
                            list.add(musicEntity);
                        }
                        break;
                    //结束标签
                    case XmlPullParser.END_TAG:
                        if ("general".equals(parser.getName())) {
                            musicGroupEntity.setGeneral(list);
                        } else if ("happy".equals(parser.getName())) {
                            musicGroupEntity.setHappy(list);
                        } else if ("love".equals(parser.getName())) {
                            musicGroupEntity.setLove(list);
                        } else if ("soft".equals(parser.getName())) {
                            musicGroupEntity.setSoft(list);
                        } else if ("children".equals(parser.getName())) {
                            musicGroupEntity.setChildren(list);
                        } else if ("birthday".equals(parser.getName())) {
                            musicGroupEntity.setBirthday(list);
                        } else if ("dynamic".equals(parser.getName())) {
                            musicGroupEntity.setDynamic(list);
                        } else if ("epic".equals(parser.getName())) {
                            musicGroupEntity.setEpic(list);
                        }
                }
                //继续往下读取标签类型
                type = parser.next();
            }
            return musicGroupEntity;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return musicGroupEntity;
    }

}
