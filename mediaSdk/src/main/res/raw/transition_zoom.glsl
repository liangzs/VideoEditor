precision mediump float;
varying vec2 textureCoordinate;
uniform float progress;
uniform sampler2D vTexture;
uniform sampler2D vTexture1;
const float zoom_quickness=.8;// = 0.8

vec2 zoom(vec2 uv, float amount) {
    return 0.5 + ((uv - 0.5) * (1.0-amount));
}

void main(){
    float nQuick = clamp(zoom_quickness, 0.2, 1.0);
    vec2 zo= zoom(textureCoordinate, smoothstep(0.0, nQuick, progress));
    gl_FragColor= mix(
    texture2D(vTexture1, zo),
    texture2D(vTexture, textureCoordinate),
    smoothstep(nQuick-0.2, 1.0, progress)
    );
}