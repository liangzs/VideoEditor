varying highp vec2 textureCoordinate;
precision highp float;
uniform sampler2D inputImageTexture;
uniform float strength;

vec4 inkFilter() {
    vec4 color = texture2D(inputImageTexture, textureCoordinate);
    float resultAlpha=color.a*(1.0/(color.a+0.00001));
    float gray = color.r + color.g + color.b;
    float a = step(gray, 1.5*strength);
    vec4 newColor = (1.0-a)*vec4(1.0, 1.0, 1.0, 1.0) + a*vec4(0.0, 0.0, 0.0, 1.0);
    return mix(color, newColor, strength)*resultAlpha;
}
void main() {
    gl_FragColor = inkFilter();
}