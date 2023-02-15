varying mediump vec2 textureCoordinate;
precision lowp float;

uniform sampler2D inputImageTexture;
uniform sampler2D curve;
uniform float strength;


vec3 bright(vec3 color, float T) {
    vec3 black = vec3(0., 0., 0.);
    return mix(black, color.rgb, T);
}
void main() {
    vec4 originalTexelColor = texture2D(inputImageTexture, textureCoordinate);
    float resultAlpha=originalTexelColor.a*(1.0/(originalTexelColor.a+0.00001));
    vec4 color = originalTexelColor;
    //亮度 0.-2.
    color.rgb = bright(color.rgb, 2.0);
    gl_FragColor = mix(originalTexelColor, color, strength);
}