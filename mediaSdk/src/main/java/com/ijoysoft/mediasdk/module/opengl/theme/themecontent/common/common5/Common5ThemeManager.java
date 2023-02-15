package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common5;

public class Common5ThemeManager extends Common5ThemeManagerTemplate {



//    @Override
//    public void onDrawFrame(int currenPostion) {
//        super.onDrawFrame(currenPostion);
//        if (gifOriginFilter != null) {
//            gifOriginFilter.draw();
//        }
//        if (imageOriginFilter != null) {
//            imageOriginFilter.draw();
//        }
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (actionRender != null) {
//            actionRender.onDestroy();
//        }
//        if (gifOriginFilter != null) {
//            gifOriginFilter.onDestroy();
//            for (GifDecoder.GifFrame frame : singleGIF) {
//                frame.image.recycle();
//            }
//            singleGIF.clear();
//            gifOriginFilter = null;
//        }
//        if (imageOriginFilter != null) {
//            imageOriginFilter.onDestroy();
//            imageOriginFilter = null;
//        }
//    }
//
//
//
//
//    @Override
//    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
//        super.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight);
//        //对显示区域重新赋值
//        this.width = width;
//        this.height = height;
//        if (gifOriginFilter != null) {
//            if (width < height) {
//                gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 2.2f);
//            } else if (height < width) {
//                gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 3.2f);
//            } else {
//                gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 3.88f);
//            }
//        }
//        if (imageOriginFilter != null) {
//            imageOriginFilter.onSizeChanged(width, height);
//            ImageOriginFilter.adjustScaling(imageOriginFilter, width, height, bitmapWidth, bitmapHeight, AnimateInfo.ORIENTATION.TOP, 0, 1f);
//        }
//    }
//
//
//
//
//    GifOriginFilter gifOriginFilter = null;
//
//    ImageOriginFilter imageOriginFilter = null;
//
//    /**
//     * 素材高度
//     */
//    int bitmapWidth;
//
//    /**
//     * 素材宽度
//     */
//    int bitmapHeight;
//
//
//    List<GifDecoder.GifFrame> singleGIF = new CopyOnWriteArrayList<>();
//
//    @SuppressLint("ResourceType")
//    @Override
//    public void drawPrepare(MediaDrawer mediaDrawer, MediaItem mediaItem, int index) {
//        super.drawPrepare(mediaDrawer, mediaItem, index);
//        if (gifOriginFilter == null) {
//            gifOriginFilter = new GifOriginFilter();
//            gifOriginFilter.onCreate();
//            if (singleGIF.isEmpty()) {
//                singleGIF.addAll(mediaItem.getDynamicMitmaps().get(0));
//            }
//            gifOriginFilter.setFrames(singleGIF);
//            if (width < height) {
//                gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 2.2f);
//            } else if (height < width) {
//                gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 3.2f);
//            } else {
//                gifOriginFilter.adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM, 0f, 3.88f);
//            }
//        }
//        if (imageOriginFilter == null && index == 0) {
//            imageOriginFilter = new ImageOriginFilter();
//            imageOriginFilter.create();
//            Bitmap bitmap = mediaItem.getMimapBitmaps().get(0);
//            ImageOriginFilter.init(imageOriginFilter, bitmap, width, height);
//            bitmapWidth = bitmap.getWidth();
//            bitmapHeight = bitmap.getHeight();
//            ImageOriginFilter.adjustScaling(imageOriginFilter, width, height, bitmapWidth, bitmapHeight, AnimateInfo.ORIENTATION.TOP, 0, 1f);
//        }
//    }
 }
