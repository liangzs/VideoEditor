package com.ijoysoft.mediasdk.module.mediacodec.decode;

/**
 * 补充{}默认给java层一个default
 */
public interface IDecodeStateListenr {
    default void decoderPrepare(IDecode iDecode) {
    }

    void decoderFinish(IDecode iDecode);

    default void decoderPause(IDecode iDecode) {
    }

    default void decoderRunning(IDecode iDecode) {
    }

    default void decoderError(IDecode iDecode, String error) {
    }

    default void decoderDestroy(IDecode iDecode) {
    }

    default void readyPlay() {

    }

    void updateNextFrame();


    public interface AudioDecodeListener extends IDecodeStateListenr {
        void audioReadyPlay();

        @Override
        default void decoderFinish(IDecode iDecode) {

        }

        @Override
        default void updateNextFrame() {

        }
    }
}