varying highp vec2 textureCoordinate;
precision highp float;
uniform sampler2D inputImageTexture;
uniform sampler2D inputImageTexture2;
uniform float strength;

void main() {
    vec2 uv = textureCoordinate.xy;
    vec4 bump = texture2D(inputImageTexture2, uv + 0.05);
    vec4 textureColorRes = texture2D(inputImageTexture, textureCoordinate);
    float resultAlpha=textureColorRes.a*(1.0/(textureColorRes.a+0.00001));
    vec2 vScale = vec2 (0.01, 0.01);
    vec2 newUV = uv + bump.xy * vScale.xy*strength;
    vec4 col = texture2D(inputImageTexture, newUV);
    gl_FragColor = vec4(col.xyz, 1.0)*resultAlpha;
}