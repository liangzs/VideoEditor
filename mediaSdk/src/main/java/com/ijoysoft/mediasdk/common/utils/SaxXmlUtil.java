package com.ijoysoft.mediasdk.common.utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SaxXmlUtil {

    public static void main(String[] args) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //2.获取SAXparser实例
        SAXParser saxParser = null;
        try {
            saxParser = factory.newSAXParser();
            //创建Handel对象
            SAXDemoHandel handel = new SAXDemoHandel();
            saxParser.parse("E:\\workSpace\\videomaker\\download/music.xml", handel);
        } catch (
                ParserConfigurationException e) {
            e.printStackTrace();
        } catch (
                SAXException e) {
            e.printStackTrace();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    static class SAXDemoHandel extends DefaultHandler {
        //遍历xml文件开始标签
        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            System.out.println("sax解析开始");
        }

        //遍历xml文件结束标签
        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
            System.out.println("sax解析结束");
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if (qName.equals("general")) {
                System.out.println("============开始遍历general=============");
                //System.out.println(attributes.getValue("rollno"));
            } else if (qName.equals("item")) {
                System.out.print("节点名称:" + attributes.getValue("path") + "----");
//                attributes.getValue("path").
            }

        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if (qName.equals("general")) {
                System.out.println(qName + "遍历结束");
                System.out.println("============结束遍历general=============");
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            String value = new String(ch, start, length).trim();
            if (!value.equals("")) {
                System.out.println(value);
            }
        }
    }
}
