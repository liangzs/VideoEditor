#extension GL_OES_EGL_image_external : require
#version 100
precision mediump float;
varying vec2 vTextureCoord;
uniform samplerExternalOES sTexture;
void main() {
      gl_FragColor = texture2D(sTexture, vTextureCoord);
}