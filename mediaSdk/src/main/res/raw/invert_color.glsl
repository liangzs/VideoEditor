precision mediump float;
varying vec2 textureCoordinate;
uniform sampler2D inputImageTexture;
void main() {
    vec4 centralColor = texture2D(inputImageTexture, textureCoordinate);
    gl_FragColor = vec4((1.0 - centralColor.rgb), centralColor.w);
}