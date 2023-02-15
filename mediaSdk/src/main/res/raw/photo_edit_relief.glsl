varying highp vec2 textureCoordinate;
precision highp float;

uniform vec3                iResolution;
uniform sampler2D inputImageTexture;
uniform float strength;

vec4 reliefFilter() {
    float colorOffset = 128.0/255.0;
    float dx = 1.0/iResolution.x;
    float dy = 1.0/iResolution.y;
    vec4 color;
    color = texture2D(inputImageTexture, textureCoordinate);
    float resultAlpha=color.a*(1.0/(color.a+0.00001));
    vec4 tempColor = texture2D(inputImageTexture, textureCoordinate + vec2(dx, dy));
    float r = abs(color.r - tempColor.r + colorOffset);
    float g = abs(color.g - tempColor.g + colorOffset);
    float b = abs(color.b - tempColor.b + colorOffset);
    return mix(color, vec4(r, g, b, color.a)*resultAlpha, strength);
}
void main() {
    gl_FragColor = reliefFilter();
}