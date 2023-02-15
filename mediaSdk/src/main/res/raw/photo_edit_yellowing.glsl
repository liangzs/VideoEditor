precision highp float;
uniform sampler2D inputImageTexture;
varying highp vec2 textureCoordinate;
uniform float strength;

void main(){
    vec4 originalTextureColor = texture2D(inputImageTexture, textureCoordinate);
    float resultAlpha=originalTextureColor.a*(1.0/(originalTextureColor.a+0.00001));
    vec4 color = texture2D(inputImageTexture, textureCoordinate);
    float c = (color.r+color.g+color.b)/3.0;
    color.r = c;
    color.g = c;
    color.b = c;
    gl_FragColor = mix(originalTextureColor, mix(color, vec4(1.0, 1.0, 0.0, 1.0)*resultAlpha, 0.24), strength);
}