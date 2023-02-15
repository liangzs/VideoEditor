#version 100
precision mediump float;
varying vec2 textureCoordinate;
uniform sampler2D vTexture;
uniform int isVertical;
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
    vec2 tex_offset =vec2(1.0/300.0, 1.0/300.0);
    vec4 orColor=texture2D(vTexture, textureCoordinate);
    float orAlpha=1.0f;
    float weight[5] = float[5](0.227027, 0.1945946, 0.1216216, 0.054054, 0.016216);
    //    float weight[2] = float[](0.054054, 0.016216);
    vec3 color=orColor.rgb*weight[0];
    if (isVertical>0)
    {
        for (int i=1;i<5;i++)
        {
            color+=texture2D(vTexture, textureCoordinate+vec2(tex_offset.x * float(i), 0.0)).rgb*weight[i];
            color+=texture2D(vTexture, textureCoordinate-vec2(tex_offset.x * float(i), 0.0)).rgb*weight[i];
        }
    }
    else
    {
        for (int i=1;i<5;i++)
        {
            color+=texture2D(vTexture, textureCoordinate+vec2(0.0, tex_offset.y * float(i))).rgb*weight[i];
            color+=texture2D(vTexture, textureCoordinate-vec2(0.0, tex_offset.y * float(i))).rgb*weight[i];
        }
    }
    //    gl_FragColor=vec4(color, orAlpha);
    vec3 a = rgb2hsv(color);
    vec3 m = a-vec3(0.0f, 0.0f, 0.04f);
    gl_FragColor = vec4(hsv2rgb(m), orAlpha);
}