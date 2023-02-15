#extension GL_OES_EGL_image_external : require
precision mediump float;
varying vec2 textureCoordinate;
uniform samplerExternalOES vTexture;
varying float vAlpha;
void main() {
    gl_FragColor = texture2D( vTexture, textureCoordinate )*vAlpha;
}