#version 100
precision highp float;
uniform sampler2D vTexture;
varying highp vec2 textureCoordinate;
uniform vec2 iResolution;
vec3 hsv2rgb(vec3 c) {
    const vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

vec3 rgb2hsv(vec3 c) {
    const vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));

    float d = q.x - min(q.w, q.y);
    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + 0.001)), d / (q.x + 0.001), q.x);
}
void main()
{
    float blurFactor=8.0;
    vec3 colour;
    for (float i = -blurFactor / 2.0; i < blurFactor / 2.0; i++) {
        for (float j = -blurFactor / 2.0; j < blurFactor / 2.0; j++) {
            vec2 newFragCoord = textureCoordinate.xy*iResolution.xy + vec2(i*2.0, j*2.0);
            vec2 newUV = newFragCoord.xy / iResolution.xy;

            vec3 tex = texture2D(vTexture, newUV).rgb;
            colour.rgb += tex.rgb;
        }
    }
    colour.rgb /= blurFactor * blurFactor;
    vec3 a = rgb2hsv(colour);
    vec3 m = a-vec3(0.0, 0.0, 0.3);
    gl_FragColor = vec4(hsv2rgb(m), 1.0);
}