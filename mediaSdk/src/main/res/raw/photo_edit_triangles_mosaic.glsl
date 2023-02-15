varying highp vec2 textureCoordinate;
precision highp float;

uniform sampler2D inputImageTexture;
uniform float strength;
vec2 tile_num = vec2(40.0, 20.0);

void main() {
    vec2 uv = textureCoordinate.xy;
    vec2 uv2 = floor(uv*tile_num)/tile_num;
    uv -= uv2;
    uv *= tile_num;
    uv2 = uv2 + vec2(step(1.0-uv.y, uv.x)/(2.0*tile_num.x), step(uv.x, uv.y)/(2.0*tile_num.y));
    gl_FragColor = texture2D(inputImageTexture, mix(textureCoordinate, uv2, strength));
}