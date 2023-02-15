#version 100
precision mediump float;
#define pi 3.14159265358;

varying vec2 scale;//1
uniform sampler2D vTexture;//4
varying float progress;//5
varying vec2 resolution;//6

void main() {
    vec2 xy1 = vec2(gl_FragCoord.xy/resolution.xy);
    vec2 xy2 = vec2(gl_FragCoord.xy/resolution.xy);

    float scaleProcess = 0.9 - progress * 0.06;

    xy1 -= 0.5;
    xy1 /= scale;
    xy1 *= scaleProcess;
    xy1 += 0.5;

    xy2 -= 0.5;
    xy2 /= scale;
    xy2 *= scaleProcess;
    xy2 += 0.5;

    xy1 = xy1 + 0.01 * progress;
    if (xy1.x < 0.0 || 1.0 < xy1.x || xy1.y < 0.0 || 1.0 < xy1.y) {
        gl_FragColor = vec4(0.0);
        return;
    }
    vec4 texColor1 = texture2D(vTexture,xy1);

    xy2 = xy2 - 0.01 * progress;
    if (xy2.x < 0.0 || 1.0 < xy2.x || xy2.y < 0.0 || 1.0 < xy2.y) {
        gl_FragColor = vec4(0.0);
        return;
    }
    vec4 texColor2 = texture2D(vTexture, xy2);

    texColor1.r = 0.0;
    texColor1.b *= 2.0;
    texColor1.g *= 2.0;

    texColor2.b = 0.0;
    texColor2.r *= 2.0;
    texColor2.g = 0.0;

    gl_FragColor = mix(texColor1, texColor2, 0.5);//Set the screen pixel to that color

}