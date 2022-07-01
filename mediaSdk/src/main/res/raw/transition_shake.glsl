precision mediump float;
varying vec2 textureCoordinate;
uniform float progress;
uniform sampler2D vTexture;
uniform sampler2D vTexture1;

void main(){
    vec2 block = floor(textureCoordinate.xy / vec2(16));
    vec2 uv_noise = block / vec2(64);
    uv_noise += floor(vec2(progress) * vec2(1200.0, 3500.0)) / vec2(64);
    vec2 dist = progress > 0.0 ? (fract(uv_noise) - 0.5) * 0.3 *(1.0 -progress) : vec2(0.0);
    vec2 red = textureCoordinate + dist * 0.2;
    vec2 green = textureCoordinate + dist * .3;
    vec2 blue = textureCoordinate + dist * .5;
    gl_FragColor= vec4(mix(texture2D(vTexture1,  red), texture2D(vTexture,  red), progress).r,mix(texture2D(vTexture1,  green),
    texture2D(vTexture,  green), progress).g,mix(texture2D(vTexture1,  blue), texture2D(vTexture,  blue), progress).b,1.0);
}